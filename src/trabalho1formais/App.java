package trabalho1formais;

import java.util.ArrayList;
import model.Regular;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import trabalho1formais.view.View;
import trabalho1formais.model.grammar.*;
import trabalho1formais.model.automaton.*;
import trabalho1formais.model.regex.Regex;

/**
 *
 * @author nathan S->aA|bB A->a|aA B->bB|b
 *
 */
public class App {

    private static View view;
    private HashMap<String, Regular> regularMap;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        this.view = new View(this);
        this.view.show(true);
        regularMap = new HashMap<String, Regular>();
    }

    private void convertGrammarToAT(Grammar grammar) {
        Automaton at;
        if (!regularMap.containsKey(grammar.getId() + "#AFND")) {
            at = Grammar.convertToAutomaton(grammar);
            regularMap.put(at.getId(), at);
            view.updateRegularList(at.getId() + " - " + at.getType());
        } else {
            at = getAutomaton(grammar.getId() + "#AFND");
        }

        view.updateTable(Automaton.toTable(at));

    }

    private void convertRegexToAT(Regex regex) {
//        TODO
    	Automaton at;
    	
    	if (!regularMap.containsKey(regex.getId() + "#AFND")) {
            at = Regex.convertToAutomaton(regex);
            regularMap.put(at.getId(), at);
            view.updateRegularList(at.getId() + " - " + at.getType());
        } else {
            at = getAutomaton(regex.getId() + "#AFND");
        }

        view.updateTable(Automaton.toTable(at));
    	
    	
    }

    public void determinize(String id) {
        Regular reg = regularMap.get(id);
        if (reg.getType().equals("AFND")) {
            Automaton afd = Automaton.determinize((Automaton) reg);
            if (!regularMap.containsKey(afd.getId())) {
                view.updateRegularList(afd.getId() + " - " + afd.getType());
            }
            regularMap.put(afd.getId(), afd);
            view.updateTable(Automaton.toTable(afd));

        }
    }

    public void minimize(String id) {
        Regular reg = regularMap.get(id);
        if ((reg.getType().equals("AFD") || reg.getType().equals("AFND"))) {
            Automaton af = (Automaton) reg;
            if (!af.isAFD()) {
                determinize(af.getId());
                af = (Automaton) regularMap.get(af.getId() + "#AFD");
            }
            Automaton afd =af;
            if (!af.isIsMim()) {
                afd = Automaton.minimize(af);
            }
            if (!regularMap.containsKey(afd.getId())) {
                view.updateRegularList(afd.getId() + " - " + afd.getType());
            }
            regularMap.put(afd.getId(), afd);
            view.updateTable(Automaton.toTable(afd));

        }
    }

    public void addNewGrammar(String id, String grammar) {
        Grammar newgrammar = Grammar.parseGrammarInput(id, grammar);

        if (newgrammar != null) {
            if (!regularMap.containsKey(id)) {
                view.updateRegularList(id + " - " + newgrammar.getType());
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
                view.updateRegularList(id + " - " + newregex.getType());
            }

            regularMap.put(id, newregex);
        } else {
            view.displayError("Expressão Regular Incorreta.");
        }
    }

    public void addNewAF(String id, DefaultTableModel tableModel) {
        Automaton newAutomaton = Automaton.parseAutomatonInput(id, tableModel);

        if (newAutomaton != null) {
            if (!regularMap.containsKey(id)) {
                view.updateRegularList(id + " - " + newAutomaton.getType());
            }

            regularMap.put(id, newAutomaton);
        } else {
            view.displayError("Automato Incorreta.");
        }
    }

    public void convertToAutomaton(String id) {
        Regular reg = regularMap.get(id);

        if (reg.getType().equals("GR")) {
            convertGrammarToAT((Grammar) reg);
        } else if (reg.getType().equals("ER")) {
            convertRegexToAT((Regex) reg);
        }
    }

    public Grammar getGrammar(String id) {
        return (Grammar) regularMap.get(id);
    }

    public Regex getRegex(String id) {
        return (Regex) regularMap.get(id);
    }

    public Automaton getAutomaton(String id) {
        return (Automaton) regularMap.get(id);
    }

    public boolean alreadyExists(String id) {
        return regularMap.containsKey(id);
    }

    public void removeID(String id) {
        view.removeRegularList(id + " - " + regularMap.get(id).getType());
        regularMap.remove(id);
    }

    public void displayError(String msg) {
        view.displayError(msg);
    }

}
