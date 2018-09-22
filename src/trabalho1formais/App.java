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
    private HashMap<String, Grammar> grammarMap;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        this.view = new View(this);
        this.view.show(true);
        grammarMap = new HashMap<String, Grammar>();
    }

    public void addNewGrammar(String id, String grammar) {
        Grammar newgrammar = Grammar.parseGrammarInput(id, grammar);

        if (newgrammar != null) {
            if (!grammarMap.containsKey(id)) {
                view.updateGrammarNRegxList(id);
            }

            grammarMap.put(id, newgrammar);
        } else {
            view.displayError("Gramática Incorreta.");
        }
    }

    public Grammar getGrammar(String id) {
        return grammarMap.get(id);
    }

    public boolean grammarExists(String id) {
        return grammarMap.containsKey(id);
    }

    public void removeGrammar(String id) {
        grammarMap.remove(id);
        view.removeGrammarNRegxList(id);
    }
    
    public void displayError(String msg){
        view.displayError(msg);
    }

}
