/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
    private boolean isAFD;
    private static int automatosCount = 0;
    
    public Automaton(ArrayList<State> states, ArrayList<Character> alphabet, 
            Transitions transitions, State initialState,  ArrayList<State> finalStates) {
        super("AUTOMATON");
        automatosCount++;
        this.id = "Automato".concat(automatosCount + "");
        this.alphabet = alphabet;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.states = states;
        this.transitions = transitions;
        isAFD = false;
    }
    
    public boolean isIsAFD() {
        return isAFD;
    }
    
    public void setAFD() {
        this.isAFD = true;
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
                try {
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
                } catch (Exception e) {
                    data[i][j] = "";
                } finally {
                    j++;
                }                          
            }
            
            j= 0;
            i++;
        }
        
        return new ViewTable(column.toArray(new String[0]), data);
    }
    
    public HashMap<State, Set<State>> getEpsilonFechos(){
        HashMap<State, Set<State>> result = new HashMap<>();
        Set<State> set;
        for(State s : states){
            set = new HashSet<>();
            set.add(s);
            
            for(State s2 : transitions.getNextStates(s, epilsonSimbol)){
                if(!s2.equals(s)) {
                    set.add(s2);
                }

            }

            result.put(s, set);
        }
        
        return result;
    }
    
    private boolean hasInnerState(State s) {
        for (State s1: this.getStates()) {
            if (s1.getName().equals(s.getName()))
                return true;
        }
        
        return false;
    }
    
    public void erraseTransitionsWithSimble(char simbol) {
        if(alphabet.contains(simbol)){
            transitions.removeTransitionsBySimbol(simbol);
            alphabet.remove((Character) simbol);
        }
    }
    
    private static void convertToAFD(Automaton af, Automaton afd,
        HashMap<State, Set<State>> fechos, Set<State> states, boolean firstIteraction) {

        State current = new State(states.toString());

        if(afd.hasInnerState(current)) {
            return;
        }
                
        Set<State> newStates = new HashSet<>();
        Set<Set<State>> lastAdded = new HashSet<>();

        afd.getStates().add(current);
        
        if(firstIteraction) {
            afd.setInitialState(current);
        }
                
        for(State s : states) {
            if(af.getFinalStates().contains(s)) {
                afd.getFinalStates().add(current);
            }
        }
            
        for(char c : afd.getAlphabet()){
            for(State s : states) {
                for(State s1 : af.getTransitions().getNextStates(s, c)) {
                    for(State s2 : af.getStates()) {
                        if (s2.getName().equals(s1.getName())){
                            newStates.addAll(fechos.get(s2));
                        }
                    }                   
                 }

            }

            if(!newStates.isEmpty()){
                State sNew = new State(newStates.toString());
                afd.getTransitions().addTransition(current, c, sNew);
                lastAdded.add(new HashSet<>(newStates));
            }

            newStates.clear();
        }

        for(Set<State> s : lastAdded) {
            convertToAFD(af, afd, fechos, s, false);
        }           
    }
    
    public static Automaton determinize(Automaton af) {
        if (af.isIsAFD()) {
            return af;
        }
        
        HashMap<State, Set<State>> fechos = af.getEpsilonFechos();

        Automaton afd = new Automaton(new ArrayList<>(), af.getAlphabet(), 
				new Transitions(), null, new ArrayList<>());
        	
        afd.erraseTransitionsWithSimble(epilsonSimbol);

        convertToAFD(af, afd, fechos, 
                        fechos.get(af.getInitialState()), true);

        afd.setAFD();
        afd.setId(af.getId()+ "#Minimo");
        return afd;
    }
}
