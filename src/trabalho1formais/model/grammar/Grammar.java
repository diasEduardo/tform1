package trabalho1formais.model.grammar;
import java.util.ArrayList;

/**
 * Definição de uma Gramatica
 * @author nathan
 */
public class Grammar {
    private ArrayList<String> Vn;
    private ArrayList<Character> Vt;
    private ArrayList<Production> productions;
    private String initialSimbol;
    private String serializedGrammar;
    private String id;
    
    public Grammar(String id, ArrayList<String> Vn, ArrayList<Character> Vt, 
            ArrayList<Production> productions,String initialSimbol, 
            String serializedGrammar) {
        this.id = id;
        this.Vn = Vn;
        this.Vt = Vt;
        this.productions = productions;
        this.serializedGrammar = serializedGrammar;
    }
}
