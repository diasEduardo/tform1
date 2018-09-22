package trabalho1formais;

import java.util.HashMap;
import java.util.Map;
import trabalho1formais.view.View;
import trabalho1formais.model.grammar.*;
import trabalho1formais.model.automaton.*;

/**
 *
 * @author nathan
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

    public Grammar getGrammar(String id) {
        return (Grammar) regularMap.get(id);
    }

    public boolean grammarExists(String id) {
        return regularMap.containsKey(id);
    }

    public void removeGrammar(String id) {
        view.removeRegularList(id+" - "+regularMap.get(id).getType());
        regularMap.remove(id);
    }
    
    public void displayError(String msg){
        view.displayError(msg);
    }

}
