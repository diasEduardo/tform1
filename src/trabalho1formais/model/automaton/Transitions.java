package trabalho1formais.model.automaton;

import java.util.ArrayList;
import java.util.HashMap;

public class Transitions {
	
	private HashMap<State, HashMap<Character, ArrayList<State>>> 
		transitions;
	
	public Transitions(){
		this.transitions = new HashMap<>();
	}
	
	/**
	 * Construtor que tenta copiar todas as transições da
	 * outra classe Transitions passada.
	 * 
	 * @param t		Transitions que se quer copiar.
	 */
	public Transitions(Transitions t){
		this.transitions = new HashMap<>();
		HashMap<State, HashMap<Character, ArrayList<State>>>
			tmp = t.getTransitions();
		
		for(State s : tmp.keySet()){
			transitions.put(new State(s.getName()), new HashMap<>());
			for(char c : tmp.get(s).keySet())
				transitions.get(s).put(c, new ArrayList<>(tmp.get(s).get(c)));
		}
	}
	
	public Transitions(
			HashMap<State, HashMap<Character, ArrayList<State>>> transitions) {
		this.transitions = transitions;
	}
	
	public void addTransition(State current, char trigger, State next){
		if(!transitions.containsKey(current))
			transitions.put(current, new HashMap<>());
		if(!transitions.get(current).containsKey(trigger))
			transitions.get(current).put(trigger, new ArrayList<>());
		
		transitions.get(current).get(trigger).add(next);
	}
	
	public void addTransitions(Transitions t){
		HashMap<State, HashMap<Character, ArrayList<State>>>
			tmp = t.getTransitions();
		
		for(State s : tmp.keySet()){
			transitions.put(new State(s.getName()), new HashMap<>());
			for(char c : tmp.get(s).keySet())
				transitions.get(s).put(c, new ArrayList<>(tmp.get(s).get(c)));
		}
	}
	
	public ArrayList<State> getNextStates(State current, char trigger){
		if(!transitions.containsKey(current) || 
				!transitions.get(current).containsKey(trigger))
			return new ArrayList<>();
		return transitions.get(current).get(trigger);
	}
	
	public void removeTransitionsBySimbol(char simbol){
		for(State key : transitions.keySet()){
			if(transitions.get(key).containsKey(simbol))
				transitions.get(key).remove(simbol);
		}
	}
	
	public void removeTransitionsByState(State s){
		transitions.remove(s);
		for(State s2 : transitions.keySet())
			for(char c : transitions.get(s2).keySet())
				transitions.get(s2).get(c).remove(s);
	}
	
	public HashMap<State, HashMap<Character, ArrayList<State>>> getTransitions(){
		return this.transitions;
	}
        
        public HashMap<Character, ArrayList<State>> getTransition(State state){
		return this.transitions.get(state);
	}

	@Override
	public String toString() {
		return "Transitions [transitions=" + transitions + "]";
	}
}
