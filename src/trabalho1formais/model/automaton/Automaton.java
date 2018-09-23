/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.automaton;

import java.util.ArrayList;
import model.Regular;

/**
 *
 * @author nathan
 */
public class Automaton extends Regular{
    
    private ArrayList<State> states;
    private ArrayList<Character> alphabet;
    private Transitions transitions;
    private State initialState;
    private ArrayList<State> finalStates;
    private String id;
    private static int automatosCount = 0;
    
    public Automaton(ArrayList<State> states, ArrayList<Character> alphabet, 
            Transitions transitions, State initialState,  ArrayList<State> finalStates) {
        super("AUTOMATON");
        automatosCount++;
        this.id = "Automato ".concat(automatosCount + "");
        this.alphabet = alphabet;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.states = states;
        this.transitions = transitions;
    }
    
    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    public ArrayList<Character> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(ArrayList<Character> alphabet) {
        this.alphabet = alphabet;
    }

    public Transitions getTransitions() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public ArrayList<State> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(ArrayList<State> finalStates) {
        this.finalStates = finalStates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
