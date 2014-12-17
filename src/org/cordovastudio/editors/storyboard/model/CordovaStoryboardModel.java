/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.navigation.NavigationModel)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adjusted slightly for Cordova projects storyboards (i.e. renamed classes, etc.)
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
package org.cordovastudio.editors.storyboard.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CordovaStoryboardModel {
    public static class Event {
        public enum Operation {INSERT, UPDATE, DELETE}

        public final Operation operation;
        public final Class<?> operandType;

        public Event(@NotNull Operation operation, @NotNull Class operandType) {
            this.operation = operation;
            this.operandType = operandType;
        }

        public static Event of(@NotNull Operation operation, @NotNull Class operandType) {
            return new Event(operation, operandType);
        }

        public static Event insert(@NotNull Class operandType) {
            return of(Operation.INSERT, operandType);
        }

        public static Event update(@NotNull Class operandType) {
            return of(Operation.UPDATE, operandType);
        }

        public static Event delete(@NotNull Class operandType) {
            return of(Operation.DELETE, operandType);
        }
    }

    private final EventDispatcher<Event> listeners = new EventDispatcher<Event>();

    private final ArrayList<State> states = new ArrayList<State>();
    private final ArrayList<Transition> transitions = new ArrayList<Transition>();
    private final Map<State, Point> stateToLocation = new HashMap<State, Point>();

    // todo change return type to List<State>
    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public Map<State, Point> getStateToLocation() {
        return stateToLocation;
    }

    private static Entry<State, Point> toEntry(Map.Entry<State, Point> entry) {
        return new Entry<State, Point>(entry.getKey(), entry.getValue());
    }

    public Collection<Entry<State, Point>> getLocations() {
        Collection<Entry<State, Point>> result = new ArrayList<Entry<State, Point>>();
        for (Map.Entry<State, Point> entry : stateToLocation.entrySet()) {
            result.add(toEntry(entry));
        }
        return result;
    }

    public void setLocations(Collection<Entry<State, Point>> locations) {
        for (Entry<State, Point> entry : locations) {
            stateToLocation.put(entry.key, entry.value);
        }
    }

    public void addState(State state) {
        if (states.contains(state)) {
            return;
        }
        states.add(state);
        listeners.notify(Event.insert(State.class));
    }

    public void removeState(State state) {
        states.remove(state);
        for (Transition t : new ArrayList<Transition>(transitions)) {
            if (t.getSource().getState() == state || t.getDestination().getState() == state) {
                remove(t);
            }
        }
        listeners.notify(Event.delete(State.class));
    }

    private void updateStates(State state) {
        if (!states.contains(state)) {
            states.add(state);
        }
    }

    public boolean add(Transition transition) {
        boolean result = transitions.add(transition);
        // todo remove this
        updateStates(transition.getSource().getState());
        updateStates(transition.getDestination().getState());
        listeners.notify(Event.insert(Transition.class));
        return result;
    }

    public boolean remove(Transition transition) {
        boolean result = transitions.remove(transition);
        listeners.notify(Event.delete(Transition.class));
        return result;
    }

    public Map<String, ActivityState> getActivities() {
        Map<String, ActivityState> activities = new HashMap<String, ActivityState>();
        for (State state : states) {
            if (state instanceof ActivityState) {
                ActivityState activityState = (ActivityState) state;
                activities.put(activityState.getClassName(), activityState);
            }
        }
        return activities;
    }

    public Map<String, MenuState> getMenus() {
        Map<String, MenuState> menus = new HashMap<String, MenuState>();
        for (State state : states) {
            if (state instanceof MenuState) {
                MenuState menuState = (MenuState) state;
                menus.put(menuState.getXmlResourceName(), menuState);
            }
        }
        return menus;
    }

    public void clear() {
        states.clear();
        transitions.clear();
        listeners.notify(Event.delete(Object.class));
    }

    public EventDispatcher<Event> getListeners() {
        return listeners;
    }

    // todo either bury the superclass's API or re-implement all of its destructive methods to post an update event
}
