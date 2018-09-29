/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.automaton;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import model.Regular;

/**
 *
 * @author nathan
 */
public class Automaton extends Regular {

    private ArrayList<State> states;
    private ArrayList<Character> alphabet;
    private Transitions transitions;
    private State initialState;
    private ArrayList<State> finalStates;
    private String id;
    //private static int automatosCount = 0;

    public Automaton(ArrayList<State> states, ArrayList<Character> alphabet,
            Transitions transitions, State initialState, ArrayList<State> finalStates, String id, String type) {
        super(type);
        this.id = id;
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

        for (Character alfa : at.getAlphabet()) {
            column.add(alfa.toString());
        }
        data = new Object[at.getStates().size()][column.size()];
        int i = 0, j = 0;

        for (State state : at.getStates()) {
            String rowState = "";
            if (state.equals(at.initialState)) {
                rowState += "->";
            } 
            if (at.finalStates.contains(state)) {
                rowState += "*";
            }

            data[i][j] = rowState.concat(state.getName());
            j++;

            for (String alfa : column.subList(1, column.size())) {
                try {
                    ArrayList<State> combinedState = at.getTransitions().getTransition(state).get(alfa.charAt(0));
                    if (combinedState != null) {
                        String aux = "";
                        aux = combinedState.stream()
                                .map((s) -> s.getName() + ",")
                                .reduce(aux, String::concat);

                        data[i][j] = aux.substring(0, aux.length() - 1);
                    } else {
                        data[i][j] = "";
                    }
                } catch (Exception e) {
                    data[i][j] = "";
                } finally {
                    j++;
                }
            }

            j = 0;
            i++;
        }

        return new ViewTable(column.toArray(new String[0]), data);
    }

    public static Automaton parseAutomatonInput(String id, DefaultTableModel tableModel) {
        int nColumn = tableModel.getColumnCount();
        if (nColumn < 2) {
            return null;
        }

        int nRow = tableModel.getRowCount();
        if (nRow < 1) {
            return null;
        }
        ArrayList<Character> alphabet = new ArrayList<Character>();
        for (int i = 1; i < nColumn; i++) {
            alphabet.add(tableModel.getColumnName(i).toCharArray()[0]);
        }
        State initialState = null;
        ArrayList<State> states = new ArrayList<>();
        ArrayList<State> finalStates = new ArrayList<>();

        for (int i = 0; i < nRow; i++) {
            String stateName = (String) tableModel.getValueAt(i, 0);
            if (stateName == null) {
                stateName = "";
            }
            String nameClear = stateName.replace(" ", "");
            nameClear = nameClear.replace("*", "");
            nameClear = nameClear.replace("->", "");
            if (nameClear.length() < 1) {
                for (int j = 1; j < nColumn; j++) {
                    String transaction = (String) tableModel.getValueAt(i, j);
                    if (transaction != null && !"".equals(transaction)) {
                        return null;
                    }
                }
                continue;
            }

            State current = new State(nameClear);
            states.add(current);

            if (stateName.contains("*")) {
                finalStates.add(current);
            }
            if (stateName.contains("->")) {
                if (initialState != null) {
                    return null;
                }
                initialState = current;
            }

        }
        if (initialState == null || finalStates.size() < 1) {
            return null;
        }
        String AFType = "AFD";
        Transitions transitions = new Transitions();

        for (int i = 0; i < nRow; i++) {
            String stateName = (String) tableModel.getValueAt(i, 0);
            if (stateName == null) {
                stateName = "";
            }
            String nameClear = stateName.replace(" ", "");
            nameClear = nameClear.replace("*", "");
            nameClear = nameClear.replace("->", "");
            
            if (!containsState(nameClear, states)) {
                return null;
            }
            State current = getState(nameClear,states);
            for (int j = 1; j < nColumn; j++) {
                char[] alpha = tableModel.getColumnName(j).toCharArray();
                String transaction = (String) tableModel.getValueAt(i, j);
                if (transaction == null || "".equals(transaction)) {
                    continue;
                }
                String transactionClear = transaction.replace(" ", "");
                transactionClear = transactionClear.replace("{", "");
                transactionClear = transactionClear.replace("}", "");
                String[] split = transactionClear.split(",");
                if (split.length > 1) {
                    AFType = "AFND";
                }
                for (String item : split) {
                    if (item == null || "".equals(item) || !containsState(item, states) ) {
                        return null;
                    }
                    State next = getState(item,states);
                    
                    transitions.addTransition(current, alpha[0], next);
                }

            }
        }
        Automaton at = new Automaton(states, alphabet,
                transitions, initialState, finalStates, id, AFType);

        return at;
    }

    private static boolean containsState(String name, ArrayList<State> states) {
        for (State item : states) {
            if (name.equals(item.getName())) {
                return true;
            }
        }
        return false;
    }
    private static State getState(String name, ArrayList<State> states) {
        for (State item : states) {
            if (name.equals(item.getName())) {
                return item;
            }
        }
        return null;
    }

}
