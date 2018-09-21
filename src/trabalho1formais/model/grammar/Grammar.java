package trabalho1formais.model.grammar;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final String allowedSimbols = 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz0123456789&";
    private static final String allowedExtraSimbols = "->|";
    private static final String pattern = "([A-Z][0-9]?->(([a-z0-9]([A-Z][0-9]?)|[a-z0-9&])(\\|))*([a-z0-9]([A-Z][0-9]?)(?![0-9]?->)|[a-z0-9&]))";
    
    public Grammar(String id, ArrayList<String> Vn, ArrayList<Character> Vt, 
            ArrayList<Production> productions,String initialSimbol, 
            String serializedGrammar) {
        
        this.id = id;
        this.Vn = Vn;
        this.Vt = Vt;
        this.productions = productions;
        this.serializedGrammar = serializedGrammar;
    }
    
    
    public Grammar(String id, String serializedGrammar) {   
        this.id = id;
        this.Vn = new ArrayList<String>();
        this.Vt = new ArrayList<Character>();
        this.productions = new ArrayList<Production>();
        this.serializedGrammar = serializedGrammar;
    }
    
    public void addProduction(String current, char generated, String next) {
        productions.add(new Production(current, generated, next));
    }
    
    public String getSerializedGrammar() {
        return serializedGrammar;
    }

    public void setSerializedGrammar(String serializedGrammar) {
        this.serializedGrammar = serializedGrammar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void addNonTerminal(String nonterminal) {
        Vn.add(nonterminal);
    }
    
    public void addTerminal(Character terminal) {
        Vt.add(terminal);
    }
    
    public String getInitialSimbol() {
        return initialSimbol;
    }

    public void setInitialSimbol(String initialSimbol) {
        this.initialSimbol = initialSimbol;
    }
    	
    public static Grammar parseGrammarInput(String titulo, String grammarString){
		grammarString = grammarString.replaceAll("\\s*", "");
		
		if(!grammarString.matches(pattern+"+")) {
                    return null;
                }
			
		Grammar G = new Grammar(titulo, grammarString);
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(grammarString);
		grammarString = "";
		
		String linha, vn, tmpVt, tmpVn;
		String[] split;
		
                while(matcher.find()){
			linha = matcher.group();
			grammarString += linha+"\n";
			
			split = linha.split("->");
			vn = split[0]; 
			G.addNonTerminal(vn);
			
                        if(G.getInitialSimbol() == null) {
                            G.setInitialSimbol(vn);
                        }
				
			
			split = split[1].split("\\|");
			
                        for(String elements : split){
				if(elements.length() > 0){
					tmpVt = elements.substring(0, 1);
					if(elements.length() > 1)
						tmpVn = elements.substring(1, elements.length());
					else
						tmpVn = "$";
					
					G.addTerminal(tmpVt.charAt(0));
					G.addProduction(vn, tmpVt.charAt(0), tmpVn);
				}
			}
		}
                
		G.setSerializedGrammar(grammarString);
		
		return G;
	}
}
