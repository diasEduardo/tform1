/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.regex;

import java.util.ArrayList;

import model.Regular;
import trabalho1formais.model.automaton.Automaton;
import trabalho1formais.model.automaton.State;
import trabalho1formais.model.automaton.Transitions;

/**
 *
 * @author nathan
 */
public class Regex extends Regular {

    private String regex, id;
    public static final String allowedSimbols = "abcdefghijklmnopqrstuvwxyz0123456789&#";
    public static final String allowedOps = "()*+.|?";

    public Regex(String id, String regex) {
        super("ER");
        this.regex = regex;
        this.id = id;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegex() {
        return regex;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return super.getType();
    }

    public static Regex parseRegexInput(String id, String regex) {
        regex = regex.replaceAll("\\s*", "");

        if (isValid(regex)) {
            regex = regex.replaceAll("(\\([a-z0-9\\.\\|\\*\\?]+\\))\\?", "\\($1|&\\)");
            regex = regex.replaceAll("(\\([a-z0-9\\.\\|\\*\\?]+\\))\\+", "$1$1\\*");
            return new Regex(id, regex);
        } else {
            return null;
        }
    }

    private static boolean isValid(String regex) {
        int parenthesisCount = 0;

        if (regex.contains("()")) {
            return false;
        }

        for (int i = 0; i < regex.length(); i++) {
            char ci = regex.charAt(i);
            System.out.println(ci);
            if (allowedOps.indexOf(ci) != -1) {
                if (ci == '(') {
                    ++parenthesisCount;
                } else if (ci == ')') {
                    --parenthesisCount;
                } else if (ci == '?' || ci == '+' || ci == '*') {
                    if (i == 0) {
                        return false;
                    }
                    char cL1 = regex.charAt(i - 1);
                    if (allowedOps.indexOf(ci) != -1 && cL1 != ')') {
                        return false;
                    }
                } else if (ci == '|' || ci == '.') {
                    if (i == 0 || i >= regex.length()) {
                        return false;
                    }
                    char cL1 = regex.charAt(i - 1);
                    char cM1 = regex.charAt(i + 1);
                    if ((cL1 == '(' || cL1 == '.' || cL1 == '|') || ((allowedOps.indexOf(cM1) != -1) && cM1 != '(')) {
                        return false;
                    }
                }
            } else if (allowedSimbols.indexOf(ci) == -1) {
                return false;
            }
        }
        return parenthesisCount == 0;
    }
    public static Automaton convertToAutomaton(Regex exreg) {
    	int cont_state = 0;
    	String exp = exreg.getRegex();
    	ArrayList<Character> alfa = getDic(exp);
    	exp = exp + "#";
    	ArrayList<Character> dic = getDic(exp);
    	
    	
    	Tree tree = new Tree(exp);
    	tree.calcFollowpos();
    	ArrayList<Node> leaves = tree.getListLeaves();
    	
    	ArrayList<Integer> state_set = tree.firstpos(tree.getRoot());
    	
        State initialState = new State(state_set);
        ArrayList<State> Dstates = new ArrayList();
        Transitions transitions = new Transitions();
        ArrayList<State> finalStates = new ArrayList();
        
        Dstates.add(initialState);
        
        int index = -1;
        int index_follow = -1;
        
        char c_transition;
        char c_leave;
        boolean isFinal;
        
        ArrayList<Integer> state_followpos = new ArrayList();
        ArrayList<Integer> new_followpos = new ArrayList();
        State newState;
        
        while(thereisUnmarked(Dstates)){
        	index = indexUnmarked(Dstates);
        	Dstates.get(index).setMarked();
        	state_followpos = Dstates.get(index).getFollowpos();
        	
        	for(int i = 0; i<dic.size();i++) {
        		c_transition = dic.get(i);
        		state_set = new ArrayList();
        		isFinal = false;
        		
        		for(int j = 0;j < state_followpos.size();j++) {
        			index_follow = state_followpos.get(j);
        			c_leave = leaves.get(index_follow).getC();
        			
        			if(c_transition == c_leave) {
        				new_followpos = leaves.get(index_follow).getFollowpos();
        				
        				for(int k = 0; k < new_followpos.size();k++) {
        			
        					if(!state_set.contains(new_followpos.get(k))) {
        						state_set.add(new_followpos.get(k));
        						
        						if(leaves.get(new_followpos.get(k)).getC() == '#') {
            						isFinal = true;
            					}
        					}
        				}
        			}
        		}
        		
        		if(!state_set.isEmpty()) {
        			newState = new State(state_set);
        			if(!contemState(Dstates,newState)) {
        				Dstates.add(newState);
        				if(isFinal) {
        					finalStates.add(newState);
        				}
        			}
        			
        			transitions.addTransition(Dstates.get(index), c_transition, newState);
        			
        		}
        	}
        }
        
        
        Automaton at =  new Automaton(Dstates, alfa,transitions, initialState, finalStates,exp+"#AFD","AFD");
    	return at;
    }
    
    public static boolean contemState(ArrayList<State> dStates,State newState) {
    	
    	for(int i =0; i<dStates.size();i++) {
    		if(newState.getFollowpos().equals(dStates.get(i).getFollowpos())) {
    			return true;
    		}
    	}
    	
    	
    	
    	return false;
    }
    
    public static ArrayList<Character> getDic(String s){
    	char alphabeto[] = s.toCharArray();
    	ArrayList<Character> dic = new ArrayList();
    	
    	
    	for(int i =0; i<alphabeto.length;i++) {
			if(!dic.contains(alphabeto[i]))
				dic.add(alphabeto[i]);
	
		}
    	return dic;
 
    }
    
    public static boolean thereisUnmarked(ArrayList<State> s) {
    	for(int i =0;i<s.size();i++) {
    		if(!s.get(i).getMarked())
    			return true;
    	}
    	return false;
    }
    
    public static int indexUnmarked(ArrayList<State> s) {
    	for(int i =0;i<s.size();i++) {
    		if(!s.get(i).getMarked())
    			return i;
    	}
    	return -1;
    }
    
    public static boolean isOperator(char c, boolean withParentheses){
		if(!withParentheses)
			return allowedOps.substring(2, allowedOps.length()).indexOf(c) != -1;
		else
			return allowedOps.indexOf(c) != -1;
	}
	
	public static boolean isBinaryOperator(char c){
		return (c=='|' || c=='.');
	}
	
	public static boolean isUnaryOperator(char c){
		return (c=='?' || c=='+' || c=='*');
	}

}
