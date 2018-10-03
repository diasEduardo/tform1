package trabalho1formais.model.grammar;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Regular;
import trabalho1formais.model.automaton.Automaton;
import trabalho1formais.model.automaton.State;
import trabalho1formais.model.automaton.Transitions;

/**
 * Definição de uma Gramatica
 *
 * @author nathan
 */
public class Grammar extends Regular {

    private ArrayList<String> Vn;
    private ArrayList<Character> Vt;
    private ArrayList<Production> productions;
    private String initialSimbol;
    private String serializedGrammar;
    private String id;
    private static final String allowedSimbols
            = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz0123456789&";
    private static final String allowedExtraSimbols = "->|";
    private static final String pattern = "([A-Z][0-9]?->(([a-z0-9]([A-Z][0-9]?)|[a-z0-9&])(\\|))*([a-z0-9]([A-Z][0-9]?)(?![0-9]?->)|[a-z0-9&]))";

    public Grammar(String id, ArrayList<String> Vn, ArrayList<Character> Vt,
            ArrayList<Production> productions, String initialSimbol,
            String serializedGrammar) {
        super("GR");
        this.id = id;
        this.Vn = Vn;
        this.Vt = Vt;
        this.productions = productions;
        this.serializedGrammar = serializedGrammar;
    }

    public Grammar(String id, String serializedGrammar) {
        super("GR");
        this.id = id;
        this.Vn = new ArrayList<String>();
        this.Vt = new ArrayList<Character>();
        this.productions = new ArrayList<Production>();
        this.serializedGrammar = serializedGrammar;
    }

    public void addProduction(String current, char generated, String next) {
        productions.add(new Production(current, generated, next));
    }
    
    public ArrayList<Production> getProductions(String currentSimbol){
        ArrayList<Production> prods = new ArrayList<>();
        for (Production p : productions){
                if(p.getCurrent().equals(currentSimbol)) {
                    prods.add(p);
                }                     
        }
        return prods;
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
        if (!Vn.contains(nonterminal))
            Vn.add(nonterminal);
    }

    public void addTerminal(Character terminal) {
        if (!Vt.contains(terminal))
            Vt.add(terminal);
    }

    public String getInitialSimbol() {
        return initialSimbol;
    }

    public void setInitialSimbol(String initialSimbol) {
        this.initialSimbol = initialSimbol;
    }

    public String getType() {
        return super.getType();
    }

    public static Grammar parseGrammarInput(String titulo, String grammarString) {
        grammarString = grammarString.replaceAll("\\s*", "");

        if (!grammarString.matches(pattern + "+")) {
            return null;
        }

        Grammar grammar = new Grammar(titulo, grammarString);
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(grammarString);
        grammarString = "";

        String linha, vn, tmpVt, tmpVn;
        String[] split;

        while (matcher.find()) {
            linha = matcher.group();
            grammarString += linha + "\n";

            split = linha.split("->");
            vn = split[0];
            grammar.addNonTerminal(vn);

            if (grammar.getInitialSimbol() == null) {
                grammar.setInitialSimbol(vn);
            }

            split = split[1].split("\\|");

            for (String elements : split) {
                if (elements.length() > 0) {
                    tmpVt = elements.substring(0, 1);
                    if (elements.length() > 1) {
                        tmpVn = elements.substring(1, elements.length());
                    } else {
                        tmpVn = "$";
                    }

                    grammar.addTerminal(tmpVt.charAt(0));
                    grammar.addProduction(vn, tmpVt.charAt(0), tmpVn);
                }
            }
        }

        grammar.setSerializedGrammar(grammarString);

        return grammar;
    }
    
    public ArrayList<String> getVn() {
        return Vn;
    }

    public ArrayList<Character> getVt() {
        return Vt;
    }
    
    public static Automaton convertToAutomaton(Grammar grammar) {
        Transitions transitions = new Transitions();
        State initialState = new State(grammar.getInitialSimbol());
        ArrayList<State> finalStates;
        String id = grammar.id;
	ArrayList<State> automatonStates = new ArrayList<>();
        ArrayList<State> automatonFStates = new ArrayList<>();	
	State sFinal = new State("F");
	automatonStates.add(sFinal);
        automatonFStates.add(sFinal);
	
        for(String vn : grammar.getVn()){
                State s = new State(vn);
                automatonStates.add(s);
                
                if(vn.equals(grammar.getInitialSimbol())) {
                    initialState = s;
                }
                        
                for(Production p : grammar.getProductions(vn)){
                        if(p.getNext().equals("$"))
                                transitions.addTransition(s, p.getGenerated(), sFinal);
                        else
                                transitions.addTransition(s, p.getGenerated(), new State(p.getNext()));

                        if(vn.equals(grammar.getInitialSimbol()) && p.getGenerated() == '&')
                                automatonFStates.add(s);
                }
        }
        
        Automaton at =  new Automaton(automatonStates, grammar.getVt(), 
                transitions, initialState, automatonFStates,id+"#AFND","AFND");
        
        return at;
    }

    
}
