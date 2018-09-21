package trabalho1formais;

import java.util.ArrayList;
import trabalho1formais.view.View;
import trabalho1formais.model.grammar.*;
import trabalho1formais.model.automaton.*;
/**
 *
 * @author nathan
 */
public class App {
    private static View view;
    private ArrayList<Grammar> grammarList;
   
    public static void main(String[] args) {
       new App();
    }
    
    public App() {
        this.view = new View(this);
        this.view.show(true);
        grammarList = new ArrayList<>();
    }
    
    public void addNewGrammar(String id, String grammar) {
        Grammar newgrammar = Grammar.parseGrammarInput(id, grammar);
        if (newgrammar != null) {
            grammarList.add(newgrammar);
            view.updateGrammarNRegxList(id);
        } else {
            view.displayError("Gramática Incorreta.");
        }      
    }
}
