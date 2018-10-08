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
    private boolean isAFD;

    private boolean isMim;
    private boolean isEmpty;
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
        isAFD = false;
        isMim = false;
        isEmpty = false;
    }

    public boolean isIsMim() {
        return isMim;
    }

    public void setIsMim(boolean isMim) {
        this.isMim = isMim;
    }

    public boolean isIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isAFD() {
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

    public HashMap<State, Set<State>> getEpsilonFechos() {
        HashMap<State, Set<State>> result = new HashMap<>();
        Set<State> set;
        for (State s : states) {
            set = new HashSet<>();
            set.add(s);

            for (State s2 : transitions.getNextStates(s, epilsonSimbol)) {
                if (!s2.equals(s)) {
                    set.add(s2);
                }

            }

            result.put(s, set);
        }

        return result;
    }

    public State getState(String name) {
        for (State s : states) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public ArrayList<State> getNotFinalStates() {
        ArrayList<State> result = new ArrayList<>();

        for (State s : states) {
            if (!this.getFinalStates().contains(s)) {
                result.add(s);
            }
        }

        return result;
    }

    public ArrayList<State> getAllNextStates(State current) {
        ArrayList<State> result = new ArrayList<>();

        for (char c : alphabet) {
            result.addAll(transitions.getNextStates(current, c));
        }

        return result;
    }
    public ArrayList<State> getNextStates(State current,char t) {
        ArrayList<State> result = new ArrayList<>();

        for (char c : alphabet) {
        	if(c == t)
        		result.addAll(transitions.getNextStates(current, c));
        }

        return result;
    }

    public ArrayList<State> getAllPreviousStates(State current) {
        ArrayList<State> result = new ArrayList<>();

        for (State s : states) {
            for (State st : getAllNextStates(s)) {
                if (st.getName().equals(current.getName())) {
                    result.add(s);
                }
            }

        }

        return result;
    }

    private boolean hasInnerState(State s) {
        for (State s1 : this.getStates()) {
            if (s1.getName().equals(s.getName())) {
                return true;
            }
        }

        return false;
    }

    private void erraseTransitionsWithSimble(char simbol) {
        if (alphabet.contains(simbol)) {
            transitions.removeTransitionsBySimbol(simbol);
            alphabet.remove((Character) simbol);
        }
    }

    private static void convertToAFD(Automaton af, Automaton afd,
            HashMap<State, Set<State>> fechos, Set<State> states, boolean firstIteraction) {

        State current = new State(states.toString());

        if (afd.hasInnerState(current)) {
            return;
        }

        Set<State> newStates = new HashSet<>();
        Set<Set<State>> lastAdded = new HashSet<>();

        afd.getStates().add(current);

        if (firstIteraction) {
            afd.setInitialState(current);
        }

        for (State s : states) {
            if (af.getFinalStates().contains(s)) {
                afd.getFinalStates().add(current);
            }
        }

        for (char c : afd.getAlphabet()) {
            for (State s : states) {
                for (State s1 : af.getTransitions().getNextStates(s, c)) {
                    for (State s2 : af.getStates()) {
                        if (s2.getName().equals(s1.getName())) {
                            newStates.addAll(fechos.get(s2));
                        }
                    }
                }

            }

            if (!newStates.isEmpty()) {
                State sNew = new State(newStates.toString());
                afd.getTransitions().addTransition(current, c, sNew);
                lastAdded.add(new HashSet<>(newStates));
            }

            newStates.clear();
        }

        for (Set<State> s : lastAdded) {
            convertToAFD(af, afd, fechos, s, false);
        }
    }

    public static Automaton determinize(Automaton af) {
        if (af.isAFD()) {
            return af;
        }

        HashMap<State, Set<State>> fechos = af.getEpsilonFechos();

        Automaton afd = new Automaton(new ArrayList<>(), af.getAlphabet(),
                new Transitions(), null, new ArrayList<>(), af.getId() + "#AFD", "AFD");

        afd.erraseTransitionsWithSimble(epilsonSimbol);

        convertToAFD(af, afd, fechos,
                fechos.get(af.getInitialState()), true);

        afd.setAFD();
        return afd;
    }

    private static int getEquivalentSet(ArrayList<ArrayList<State>> eqSets,
            State state) {

        for (int i = 0; i < eqSets.size(); i++) {
            for (State s : eqSets.get(i)) {
                if (s.getName().equals(state.getName())) {
                    return i;
                }
            }
        }

        return -1;
    }

    private static ArrayList<ArrayList<State>> calculeEqSets(Automaton min,
            ArrayList<ArrayList<State>> eqSets) {
        ArrayList<ArrayList<State>> newEqSets = new ArrayList<>();
        ArrayList<State> tmp;
        boolean toAdd;
        State morto = new State("M");
        eqSets.get(1).add(morto);
        for (ArrayList<State> set : eqSets) {
            for (int i = 0; i < set.size(); i++) {
                if (getEquivalentSet(newEqSets, set.get(i)) == -1) {
                    tmp = new ArrayList<>();
                    tmp.add(set.get(i));
                    for (int k = i + 1; k < set.size(); k++) {
                        toAdd = true;
                        for (char c : min.getAlphabet()) {
                            ArrayList<State> nextStatesi = min.getTransitions().getNextStates(set.get(i), c);
                            ArrayList<State> nextStatesk = min.getTransitions().getNextStates(set.get(k), c);
                            if (nextStatesi.size() == 0) {
                                nextStatesi.add(morto);
                            }
                            if (nextStatesk.size() == 0) {
                                nextStatesk.add(morto);
                            }
                            if (getEquivalentSet(eqSets, nextStatesi.get(0))
                                    != getEquivalentSet(eqSets, nextStatesk.get(0))) {
                                toAdd = false;
                            }

                        }

                        if (toAdd) {
                            tmp.add(set.get(k));
                        }

                    }

                    newEqSets.add(tmp);
                }
            }
        }
        for (ArrayList<State> Arraylist : newEqSets) {
            Arraylist.remove(morto);
        }

        if (eqSets.size() == newEqSets.size()) {
            return newEqSets;
        } else {
            return calculeEqSets(min, newEqSets);
        }
    }

    private static Set<State> getFertileStates(Automaton afd) {
        if (!afd.isAFD()) {
            afd = determinize(afd);
        }

        Set<State> fertileStates = new HashSet<>();
        Set<State> toAdd;

        fertileStates.addAll(afd.getFinalStates());

        do {
            toAdd = new HashSet<>();
            for (State s : fertileStates) {
                toAdd.addAll(afd.getAllPreviousStates(s));
            }
        } while (fertileStates.addAll(toAdd));

        return fertileStates;
    }

    public static Automaton minimize(Automaton afd) {
        if (afd.isMim) {
            return afd;
        }

        if (!afd.isAFD()) {
            afd = determinize(afd);
        }
        afd = new Automaton(afd.getStates(), afd.getAlphabet(), afd.getTransitions(), afd.getInitialState(), afd.getFinalStates(), afd.getId(), afd.getType());

        Set<State> fertile = getFertileStates(afd);
        ArrayList<State> toRemove = new ArrayList<>();

        for (State s : afd.getStates()) {
            boolean contains = false;
            for (State st : fertile) {
                String stName = st.getName().replace("[[", "[").replace("]]", "]");
                if (stName.equals(s.getName())) {
                    contains = true;
                }
            }
            if (!contains) {
                toRemove.add(s);
            }
        }

        if (!toRemove.isEmpty()) {
            for (State s : toRemove) {
                afd.getTransitions().removeTransitionsByState(s);
                afd.getStates().remove(s);
                afd.getFinalStates().remove(s);

                if (s.equals(afd.getInitialState())) {
                    afd.setInitialState(null);
                }
            }
        }

        if (afd.getInitialState() == null) {
            afd.setId(afd.getId() + "#vazia");
            afd.setIsEmpty(true);

            return afd;
        }

        ArrayList<ArrayList<State>> equivalentsSets = new ArrayList<>();

        equivalentsSets.add(afd.getFinalStates());
        ArrayList<State> tmp = afd.getNotFinalStates();

        if (tmp.size() > 0) {
            equivalentsSets.add(tmp);
        }

        equivalentsSets = calculeEqSets(afd, equivalentsSets);

        Automaton min = new Automaton(new ArrayList<>(), afd.getAlphabet(),
                new Transitions(), null, new ArrayList<>(), afd.getId(), afd.getType());

        HashMap<State, ArrayList<State>> states = new HashMap<>();

        State tmpState;
        ArrayList<State> set;

        for (int i = 0; i < equivalentsSets.size(); i++) {
            set = equivalentsSets.get(i);

            if (!set.get(0).getName().contains("[ERRO]")) {
                tmpState = new State("Q" + i);
                states.put(tmpState, set);
            }
        }

        int nSet;
        State tmpState2;

        for (State s : states.keySet()) {
            min.getStates().add(s);
            for (State s2 : states.get(s)) {
                if (afd.getFinalStates().contains(s2)) {
                    min.getFinalStates().add(s);
                }

                if (afd.getInitialState().equals(s2)) {
                    min.setInitialState(s);
                }

            }
        }

        for (State s : states.keySet()) {
            tmpState = states.get(s).get(0);
            for (char c : min.getAlphabet()) {
                ArrayList<State> nextStates = afd.getTransitions().getNextStates(tmpState, c);
                nSet = -1;
                if (nextStates.size() > 0) {
                    nSet = getEquivalentSet(equivalentsSets,
                            afd.getTransitions().getNextStates(tmpState, c).get(0));
                }
                if (nSet != -1) {
                    tmpState2 = min.getState("Q" + nSet);
                    if (tmpState2 != null) {
                        min.getTransitions().addTransition(s, c, tmpState2);
                    }

                }
            }
        }

        min.setId(afd.getId() + "#Minimo");
        min.setIsMim(true);

        return min;
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
            State current = getState(nameClear, states);
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
                    if (item == null || "".equals(item) || !containsState(item, states)) {
                        return null;
                    }
                    State next = getState(item, states);

                    transitions.addTransition(current, alpha[0], next);
                }

            }
        }
        Automaton at = new Automaton(states, alphabet, transitions, initialState, finalStates, id, AFType);

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
    
    public static Automaton union(Automaton a,Automaton b) {
    	State iniState = new State("novoS");
    	
    	ArrayList<State> new_states = new ArrayList();
    	new_states.add(iniState);
    	new_states.addAll(a.getStates());
    	new_states.addAll(b.getStates());
    	
        
        Transitions new_transitions = new Transitions();
        new_transitions.addTransitions(a.getTransitions());
        new_transitions.addTransitions(b.getTransitions());
        new_transitions.addTransition(iniState, epilsonSimbol, a.getInitialState());
        new_transitions.addTransition(iniState, epilsonSimbol, b.getInitialState());
        
        
        
        ArrayList<State> fStates = new ArrayList();
        fStates.addAll(a.getFinalStates());
        fStates.addAll(b.getFinalStates());
        String new_id = a.getId()+'U'+b.getId();

    	
        Automaton at =  new Automaton(new_states, a.getAlphabet(),new_transitions, iniState, fStates,new_id,"AFND");
    	return at;
    }
    
    
    public static Automaton intersection(Automaton afd1, Automaton afd2){
		afd1 = complement(afd1);
		afd2 = complement(afd2);
		Automaton inter = union(afd1, afd2);
		inter = complement(inter);
		return inter;//minimize(inter);
	}
    
    public void addTransition(State current, char trigger, State next){
		if(!states.contains(current) || 
				!alphabet.contains(trigger))
			return;
		
		transitions.addTransition(current, trigger, next);
	}
    public void addState(State s){
		if(!states.contains(s))
			states.add(s);
	}
    
    private static Automaton complete(Automaton afd){
		/*if(afd.getExtras().contains("Complete"))
			return afd;*/
		
		afd = determinize(afd);
		
		State err = new State("[ERRO]");
		boolean edited = false;
		
		for(State s : afd.getStates()){
			for(char c : afd.getAlphabet()){
				if(afd.getNextStates(s, c).isEmpty()){
					afd.addTransition(s, c, err);
					edited = true;
				}
			}
		}
		
		if(edited){
			afd.addState(err);
			for(char c : afd.getAlphabet())
				afd.addTransition(err, c, err);
		}
		return afd;
	}

    
    public static Automaton complement(Automaton af){
		/*if(af.getExtras().contains("AFD_Comp"))
			return af;*/
		
		Automaton comp;
		
		if(!af.isAFD())
			comp = determinize(af);
		else
			comp = af;
		
		comp = complete(comp);
		comp.setFinalStates(comp.getNotFinalStates());
		
		return comp;
	}

}
