/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.editors.navigation.macros.CodeGenerator)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adjusted slightly for Cordova projects (i.e. renamed classes, etc.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.storyboard.macros;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.cordovastudio.editors.storyboard.Utilities;
import org.cordovastudio.editors.storyboard.model.*;

public class CodeGenerator {
    public final Module module;
    public final CordovaStoryboardModel navigationModel;

    public CodeGenerator(CordovaStoryboardModel navigationModel, Module module) {
        this.navigationModel = navigationModel;
        this.module = module;
    }


    // Was already commented out before beginning my work. Don't know why. –– C. Timm
  /*
  Map<String, PsiElement> bindings = new HashMap<String, PsiElement>();
  bindings.put("$menuItem", factory.createIdentifier("hello"));
  bindings.put("$f", factory.createIdentifier("goodbye"));
  bindings.put("$consume", factory.createExpressionFromText("true", body));
  PsiElement newCode = Instantiation.instantiate(installMenuItemClick, bindings);
  */


    private ActivityState getAssociatedActivityState(MenuState menuState) {
        for (Transition t : navigationModel.getTransitions()) {
            if (t.getDestination().getState() == menuState) {
                State state = t.getSource().getState();
                if (state instanceof ActivityState) {
                    return (ActivityState) state;

                }
            }
        }
        assert false;
        return null;
    }

    public void implementTransition(final Transition transition) {
        Project project = module.getProject();
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        final PsiElementFactory factory = facade.getElementFactory();
        final CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
        final Macros macros = Macros.getInstance(module.getProject());

        State sourceState = transition.getSource().getState();
        State destinationState = transition.getDestination().getState();
        if (sourceState instanceof MenuState && destinationState instanceof ActivityState) {
            MenuState menuState = (MenuState) sourceState;
            final ActivityState newActivity = (ActivityState) destinationState;
            final ActivityState originatingActivity = getAssociatedActivityState(menuState);
            final PsiClass psiClass = Utilities.getPsiClass(module, originatingActivity.getClassName());
            if (psiClass != null) {
                new WriteCommandAction<Void>(project, "Add navigation transition", psiClass.getContainingFile()) {
                    @Override
                    protected void run(Result<Void> result) {
                        PsiMethod signature = factory.createMethodFromText("public boolean onPrepareOptionsMenu(Menu menu){ }", psiClass);
                        PsiMethod method = psiClass.findMethodBySignature(signature, false);
                        if (method == null) {
                            method = factory.createMethodFromText(
                                    "@Override public boolean onPrepareOptionsMenu(Menu menu){boolean result=super.onPrepareOptionsMenu(menu);return result;}",
                                    psiClass);
                            psiClass.add(method);
                            method = psiClass.findMethodBySignature(signature, false); // the previously assigned method is not resolved somehow
                        }
                        String parameterName = method.getParameterList().getParameters()[0].getName();
                        PsiCodeBlock body = method.getBody();
                        PsiStatement[] statements = body.getStatements();
                        PsiStatement lastStatement = statements[statements.length - 1];
                        MultiMatch macro = macros.installMenuItemOnGetMenuItemAndLaunchActivityMacro;
                        MultiMatch.Bindings<String> bindings = new MultiMatch.Bindings<String>();
                        bindings.put("$consume", "true");
                        bindings.put("$menuItem", "$menu", parameterName);
                        bindings.put("$menuItem", "$id", "R.id." + transition.getSource().getViewName());
                        bindings.put("$f", "context", originatingActivity.getClassName() + ".this");
                        bindings.put("$f", "activityClass", newActivity.getClassName() + ".class");
                        String newCode = macro.instantiate(bindings);
                        PsiStatement newStatement = factory.createStatementFromText(newCode + ";", body);
                        body.addBefore(newStatement, lastStatement);
                        codeStyleManager.reformat(method);
                    }
                }.execute();
            }
        }
        if (sourceState instanceof ActivityState && destinationState instanceof MenuState) {
            ActivityState activityState = (ActivityState) sourceState;
            final MenuState menuState = (MenuState) destinationState;
            final PsiClass psiClass = Utilities.getPsiClass(module, activityState.getClassName());
            if (psiClass != null) {
                new WriteCommandAction<Void>(project, "Add navigation transition", psiClass.getContainingFile()) {
                    @Override
                    protected void run(Result<Void> result) {
                        PsiMethod signature = factory.createMethodFromText("boolean onCreateOptionsMenu(Menu menu){}", psiClass);
                        PsiMethod method = psiClass.findMethodBySignature(signature, false);
                        if (method == null) {
                            method = factory.createMethodFromText("@Override public boolean onCreateOptionsMenu(Menu menu) { return true;}", psiClass);
                            psiClass.add(method);
                            method = psiClass.findMethodBySignature(signature, false); // // the previously assigned method is not resolved somehow
                        }
                        String parameterName = method.getParameterList().getParameters()[0].getName();
                        PsiCodeBlock body = method.getBody();
                        PsiStatement[] statements = body.getStatements();
                        PsiStatement lastStatement = statements[statements.length - 1];
                        String newStatementText = "getMenuInflater().inflate(R.menu.$XmlResourceName, $parameterName);";
                        newStatementText = newStatementText.replace("$XmlResourceName", menuState.getXmlResourceName());
                        newStatementText = newStatementText.replace("$parameterName", parameterName);
                        PsiStatement newStatement = factory.createStatementFromText(newStatementText, body);
                        body.addBefore(newStatement, lastStatement);
                        codeStyleManager.reformat(method);
                    }
                }.execute();
            }
        }
        if (sourceState instanceof ActivityState && destinationState instanceof ActivityState) {
            final ActivityState sourceActivityState = (ActivityState) sourceState;
            final ActivityState newActivity = (ActivityState) destinationState;
            final PsiClass psiClass = Utilities.getPsiClass(module, sourceActivityState.getClassName());
            if (psiClass != null) {
                new WriteCommandAction<Void>(project, "Add navigation transition", psiClass.getContainingFile()) {
                    @Override
                    protected void run(Result<Void> result) {
                        PsiMethod signature = factory.createMethodFromText("void onCreate(Bundle savedInstanceState){}", psiClass);
                        PsiMethod method = psiClass.findMethodBySignature(signature, false);
                        if (method == null) {
                            method = factory.createMethodFromText("@Override " +
                                    "public void onCreate(Bundle savedInstanceState) {" +
                                    "super.onCreate(savedInstanceState);}", psiClass);
                            psiClass.add(method);
                            method = psiClass.findMethodBySignature(signature, false); // // the previously assigned method is not resolved somehow
                        }
                        PsiCodeBlock body = method.getBody();
                        PsiStatement[] statements = body.getStatements();
                        PsiStatement lastStatement = statements[statements.length - 1];
                        String newCode = "findViewById($id).setOnClickListener(new View.OnClickListener() { " +
                                "  @Override" +
                                "  public void onClick(View v) {" +
                                "    $context.startActivity(new Intent($context, $activityClass));" +
                                "  }" +
                                "})";
                        newCode = newCode.replaceAll("\\$id", "R.id." + transition.getSource().getViewName()); // todo improve
                        newCode = newCode.replaceAll("\\$context", sourceActivityState.getClassName() + ".this");
                        newCode = newCode.replaceAll("\\$activityClass", newActivity.getClassName() + ".class");
                        PsiStatement newStatement = factory.createStatementFromText(newCode + ";", body);
                        body.addAfter(newStatement, lastStatement);
                        codeStyleManager.reformat(method);
                    }
                }.execute();
            }
        }
    }
}