package trabalho1formais;

import trabalho1formais.view.View;
import trabalho1formais.model.grammar.*;
import trabalho1formais.model.automaton.*;
/**
 *
 * @author nathan
 */
public class App {
    private static View view;
   
    public static void main(String[] args) {
       new App();
    }
    
    public App() {
        this.view = new View(this);
        this.view.show(true);
    }
    
    public void addNewGrammar(String id, String grammar) {
        Grammar newgrammar = Grammar.parseGrammarInput(id, grammar);
        System.out.println(newgrammar.getId());
        System.out.println(newgrammar.getInitialSimbol());
        System.out.println(newgrammar.getSerializedGrammar());
    }
}
