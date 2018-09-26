/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.automaton;

import java.util.ArrayList;
import java.util.HashMap;
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
   
    public static ViewTable toTable(Automaton at) {
        ArrayList<String> column = new ArrayList<>();
        Object[][] data;
        column.add("Estados");
        
        for (Character alfa: at.getAlphabet()) {
            column.add(alfa.toString());
        }
        data = new Object[at.getStates().size()][column.size()];
        int i = 0, j = 0;
        
        for (State state: at.getStates()){
            String rowState = "";
            if (state.equals(at.initialState)) {
                rowState += "->";
            } else if (at.finalStates.contains(state)){
                rowState += "*";
            }
            
            data[i][j] = rowState.concat(state.getName());
            j++;
            
            for (String alfa: column.subList(1, column.size())) {
                ArrayList<State> combinedState = at.getTransitions().getTransition(state)
                        .get(alfa.charAt(0));
                if (combinedState != null ){
                    String aux = "";
                    aux = combinedState.stream()
                            .map((s) -> s.getName() + ",")
                            .reduce(aux, String::concat);
                   
                    data[i][j] = aux.substring(0, aux.length()-1);
                } else {
                    data[i][j] = "";
                }
                j++;
            }
            
            j= 0;
            i++;
        }
        
        return new ViewTable(column.toArray(new String[0]), data);
    }
}
