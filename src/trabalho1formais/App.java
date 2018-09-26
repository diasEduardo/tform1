package trabalho1formais;

import java.util.ArrayList;
import model.Regular;
import java.util.HashMap;
import java.util.Map;
import trabalho1formais.view.View;
import trabalho1formais.model.grammar.*;
import trabalho1formais.model.automaton.*;
import trabalho1formais.model.regex.Regex;

/**
 *
 * @author nathan
 */
public class App {

    private static View view;
    private HashMap<String, Regular> regularMap;

    public static void main(String[] args) {
        new App();
//        test
        State q0 = new State("q0");
        State q1 = new State("q1");
        ArrayList<State> states = new ArrayList<State>();
        ArrayList<State> statesFinal = new ArrayList<State>();
        states.add(q0);
        states.add(q1);
        statesFinal.add(q1);
        ArrayList<Character> alphabet = new ArrayList<Character>();
        alphabet.add('a');
        alphabet.add('b');
        Transitions t = new Transitions();
        t.addTransition(q0, 'b', q0);
        t.addTransition(q0, 'a', q1);
        t.addTransition(q1, 'a', q1);
        t.addTransition(q0, 'a', q0);
        Automaton at = new Automaton(states, alphabet, 
                t,q0, statesFinal);
      
        view.updateTable(Automaton.toTable(at));
    }

    public App() {
        this.view = new View(this);
        this.view.show(true);
        regularMap = new HashMap<String, Regular>();
    }

    public void addNewGrammar(String id, String grammar) {
        Grammar newgrammar = Grammar.parseGrammarInput(id, grammar);

        if (newgrammar != null) {
            if (!regularMap.containsKey(id)) {
                view.updateRegularList(id+" - "+newgrammar.getType());
            }

            regularMap.put(id, newgrammar);
        } else {
            view.displayError("Gramática Incorreta.");
        }
    }
    
    public void addNewRegex(String id, String regex) {
        Regex newregex = Regex.parseRegexInput(id, regex);

        if (newregex != null) {
            if (!regularMap.containsKey(id)) {
                view.updateRegularList(id+" - "+newregex.getType());
            }

            regularMap.put(id, newregex);
        } else {
            view.displayError("Expressão Regular Incorreta.");
        }
    }

    public Grammar getGrammar(String id) {
        return (Grammar) regularMap.get(id);
    }
    
    public Regex getRegex(String id) {
        return (Regex) regularMap.get(id);
    }

    public boolean alreadyExists(String id) {
        return regularMap.containsKey(id);
    }

    public void removeID(String id) {
        view.removeRegularList(id+" - "+regularMap.get(id).getType());
        regularMap.remove(id);
    }
    
    public void displayError(String msg){
        view.displayError(msg);
    }

}
