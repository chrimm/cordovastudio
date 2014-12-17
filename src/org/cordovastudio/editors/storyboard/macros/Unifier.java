/*
 * Copyright (C) 2013 The Android Open Source Project
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

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiIdentifierImpl;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class Unifier {
    public static final PsiElement UNBOUND = new PsiIdentifierImpl("<Unbound>");
    public static final String STATEMENT_SENTINEL = "$";
    // A stand-in method name used to make statement wildcards: wild cards that match statements - e.g. $f.$()
    public static final String STATEMENTS_SENTINEL = "$$";
    // A stand-in method name used to match blocks: wild cards that match a set of statements - e.g. $f.$$()
    private static final boolean DEBUG = false;
    private int indent = 0;

    @Nullable
    public static Map<String, PsiElement> match(PsiMethod method, PsiElement element) {
        return new Unifier().unify(method.getParameterList(), method.getBody().getStatements()[0].getFirstChild(), element);
    }

  /*
  public static Map<String, PsiElement> matchStatement(PsiMethod method, PsiElement element) {
    return new Unifier().unify(method.getParameterList(), method.getBody().getStatements()[0], element);
  }
  */

    private String indent() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            b.append("  ");
        }
        return b.toString();
    }

    @Nullable
    public Map<String, PsiElement> unify(PsiMethod macro, PsiElement candidate) {
        return unify(macro.getParameterList(), macro.getBody(), candidate);
    }

    @Nullable
    public Map<String, PsiElement> unify(PsiParameterList parameterList, PsiElement body, PsiElement candidate) {
        Matcher myMatcher = new Matcher(candidate);
        for (PsiParameter parameter : parameterList.getParameters()) {
            myMatcher.bindings.put(parameter.getName(), UNBOUND);
        }
        body.accept(myMatcher);
        Map<String, PsiElement> bindings = myMatcher.getBindings();
        if (DEBUG) System.out.println("Unifier: bindings = " + bindings);
        return bindings;
    }

    private class Matcher extends JavaElementVisitor {
        Map<String, PsiElement> bindings = new HashMap<String, PsiElement>();
        Map<String, String> parameterBindings = new HashMap<String, String>();
        private boolean valid = true;
        private PsiElement candidate;

        private Matcher(PsiElement candidate) {
            this.candidate = candidate;
        }

        private boolean equals(PsiIdentifier identifier1, PsiElement identifier2) {
            return identifier2 instanceof PsiIdentifier && identifier1.getText().equals(identifier2.getText());
        }

        @Override
        public void visitParameter(PsiParameter parameter) {
            String name = parameter.getName();
            if (parameterBindings.get(name) != null) {
                assert false;
            }
            parameterBindings.put(name, ((PsiParameter) candidate).getName());
        }

        private boolean isBindable(String text) {
            return bindings.containsKey(text) || parameterBindings.containsKey(text);
        }

        @Override
        public void visitIdentifier(PsiIdentifier identifier) {
            String text = identifier.getText();
            if (isBindable(text)) {
                bindings.put(text, candidate);
            } else {
                if (!equals(identifier, candidate)) {
                    if (DEBUG) System.out.println(indent() + identifier + " != " + candidate);
                    valid = false;
                }
            }
        }

        /*
            PSI trees seem to have some minor non-deterministic behaviors when applied to annotations.
            Specifically, the AnnotationParamListElement and PsiAnnotationParamListImpl to be exchanged
            in certain cases. So the output below for an example. For now, ignore annotations.

                      PsiAnnotation : PsiAnnotation
                        PsiJavaToken:AT : PsiJavaToken:AT
                        PsiJavaCodeReferenceElement:Override : PsiJavaCodeReferenceElement:Override
                          PsiReferenceParameterList : PsiReferenceParameterList
                      PsiAnnotationParameterList (class) != PsiAnnotationParameterList: (class)
                      class com.intellij.psi.impl.source.tree.java.AnnotationParamListElement != class com.intellij.psi.impl.source.tree.java.PsiAnnotationParamListImpl
                    *** annotation matching failed ***
                    @Override (class) != @Override (class)
                    class com.intellij.psi.impl.source.tree.java.PsiAnnotationImpl != class com.intellij.psi.impl.source.tree.java.PsiAnnotationImpl
        */
        @Override
        public void visitAnnotation(PsiAnnotation annotation) {
      /*
      super.visitAnnotation(annotation);
      if (!valid) {
        System.out.println(indent() + "*** annotation matching failed ***");
        System.out.println(indent() + annotation.getText() + " (class) != " + candidate.getText() + " (class)");
        System.out.println(indent() + annotation.getClass() + " != " + candidate.getClass());
        //valid = true;
      }
      */
        }

        @Override
        public void visitMethodCallExpression(PsiMethodCallExpression expression) {
            String targetName = expression.getFirstChild().getFirstChild().getText();
            String methodName = expression.getFirstChild().getLastChild().getText();
            if (methodName.equals(STATEMENT_SENTINEL)) {
                bindings.put(targetName, candidate);
            } else {
                super.visitMethodCallExpression(expression);
            }
        }

        /**
         * @see com.intellij.psi.JavaElementVisitor#visitReferenceExpression(com.intellij.psi.PsiReferenceExpression)
         */
        @Override
        public void visitReferenceExpression(PsiReferenceExpression expression) {
            String text = expression.getText();
            if (isBindable(text)) {
                bindings.put(text, candidate);
            } else {
                visitReferenceElement(expression);
            }
        }

        @Override
        public void visitStatement(PsiStatement statement) {
            PsiElement expression = statement.getFirstChild();
            if (expression instanceof PsiMethodCallExpression) {
                String targetName = expression.getFirstChild().getFirstChild().getText();
                String methodName = expression.getFirstChild().getLastChild().getText();
                if (methodName.equals(STATEMENTS_SENTINEL)) {
                    bindings.put(targetName, candidate.getParent());
                    candidate = candidate.getParent().getLastChild();
                    return;
                }
            }
            visitElement(statement);
        }

        @Override
        public void visitElement(PsiElement template) {
            if (template.getClass() != candidate.getClass()) {
                if (DEBUG) System.out.println(indent() + template + ".getClass() != " + candidate + ".getClass()");
                if (DEBUG) System.out.println(indent() + template.getClass() + " != " + candidate.getClass());
                valid = false;
                return;
            }
            indent++;
            PsiElement tmp = candidate;

            if (DEBUG) System.out.println(indent() + template + " : " + candidate);

            PsiElement child = template.getFirstChild();
            candidate = candidate.getFirstChild();
            while (valid && (child != null) && (candidate != null)) {
                child.accept(this);
                child = child.getNextSibling();
                candidate = candidate.getNextSibling();
            }

            candidate = tmp;
            indent--;
        }

        @Nullable
        private Map<String, PsiElement> getBindings() {
            return valid ? bindings : null;
        }
    }
}
