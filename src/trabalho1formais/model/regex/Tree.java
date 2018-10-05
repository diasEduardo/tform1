package trabalho1formais.model.regex;

import java.util.ArrayList;
import java.util.Stack;

import trabalho1formais.model.regex.Regex;

public class Tree {
	private Node root;
	private ArrayList<Node> listLeaves;
	
	public Tree(String regEx) {
		root = createSubTrees(null, regEx);
		listLeaves = new ArrayList<>();
		addLeavesInOrder();
		costuraEmOrderRec(root);
		
		//System.out.println(listLeaves);
		//printPreOrderRec(root);
	}
	
	public Node getRoot(){
		return this.root;
	}
	
	public ArrayList<Node> getListLeaves(){
		return this.listLeaves;
	}

	private Node createSubTrees(Node n, String regEx) {
		Node newNodo = new Node();
		SubTree sub = SubTree.getInstance();
		
		regEx = removeExternalParentheses(regEx);
		int raiz = sub.positionOfRoot(regEx);
		
		if(raiz == -1 && regEx.length() > 1)
			return createSubTrees(n, regEx);
		else if(raiz == -1 && regEx.length() == 1)
			return new Node(regEx.charAt(0), n);
		
		newNodo.setC(regEx.charAt(raiz));
		newNodo.setPai(n);
		
		String erEsq = regEx.substring(0, raiz);
		String erDir = regEx.substring(raiz+1, regEx.length());
		
		if(erEsq.length() > 1)
			newNodo.setFilhoEsq(createSubTrees(newNodo, erEsq));
		else if(erEsq.length() == 1)
			newNodo.setFilhoEsq(new Node(erEsq.charAt(0), newNodo));
		
		if(erDir.length() > 1)
			newNodo.setFilhoDir(createSubTrees(newNodo, erDir));
		else if(erDir.length() == 1)
			newNodo.setFilhoDir(new Node(erDir.charAt(0), newNodo));
		
		return newNodo;
	}
	
	private String removeExternalParentheses(String regEx){
		String tmp = regEx;
		Stack<Character> stackParentheses = new Stack<>();
		char cTmp;
		
		if(tmp.charAt(0) == '(' && tmp.charAt(regEx.length()-1) == ')'){
			if(tmp.charAt(0) == '(' && tmp.charAt(tmp.length()-1) == ')')
				tmp = "[" +tmp.substring((1), tmp.length()-1)+ "]";
				
			for (int i = 0; i < tmp.length(); i++) {
				cTmp = tmp.charAt(i);
				if(cTmp == '[')
					stackParentheses.push('[');
				else if(cTmp == ']' && stackParentheses.peek() == '[')
					stackParentheses.pop();
				else if(cTmp == '(' && 
						(stackParentheses.peek() == '(' || 
						stackParentheses.peek() == '['))
					stackParentheses.push('(');
				else if(cTmp == ')' && stackParentheses.peek() == '(')
					stackParentheses.pop();
			}
			
			if(stackParentheses.isEmpty())
				return tmp.substring(1, tmp.length()-1);
			else
				return regEx;
		}else
			return regEx;
	}
	
	private void costuraEmOrderRec(Node root){
		if(root == null)
			return;
		
		costuraEmOrderRec(root.getFilhoEsq());
		
		if(!Regex.isBinaryOperator(root.getC())){
			Node pai = root.getPai();
			if(pai != null){
				while(pai.isCosturado()){
					pai = pai.getPai();
					if(pai == null){
						root.setCostura(new Node('$', null));//node lambda
						return;
					}
				}
				root.setCostura(pai);
				pai.setIsCosturado(true);
			}else
				root.setCostura(new Node('$', null));
		}
		
		costuraEmOrderRec(root.getFilhoDir());
	}
	
	private void addLeavesInOrder(){
		Stack<Node> stackNodes = new Stack<>();
		Node n = root;
		int num = 1;

		while(!stackNodes.isEmpty() || n != null){
			if(n != null){
				stackNodes.push(n);
				n = n.getFilhoEsq();
			}else{
				n = stackNodes.pop();
				if(!Regex.isOperator(n.getC(),false)){
					n.setNumero(num);
					listLeaves.add(n);
					++num;
				}
				n = n.getFilhoDir();
			}
		}
		
	}
	
	public void printPreOrderRec(Node root){
		if(root != null){
			System.out.println("E: "+root.getFilhoEsq()+" - R: "+root+" - D: "+root.getFilhoDir()+" - Cost: "+root.getCostura());
			printPreOrderRec(root.getFilhoEsq());
			printPreOrderRec(root.getFilhoDir());
		}
	}
	public boolean nullable(Node root) {
		boolean ans = false;
		if(root.getC()=='?' || root.getC()=='*') {
			ans = true;
		}
		else if(root.getC()=='|'){
			if(nullable(root.getFilhoDir()) || nullable(root.getFilhoEsq()))
				ans = true;
		}
		else if(root.getC()=='.'){
			if(nullable(root.getFilhoDir()) && nullable(root.getFilhoEsq()))
				ans = true;
		}
		return ans;
	}
	
	public ArrayList<Integer> firstpos(Node root){
		
		  ArrayList<Integer> ans = new ArrayList();
		  ArrayList<Integer> aux = new ArrayList();

		  if(!Regex.isOperator(root.getC(), false)){
			  if(!ans.contains(root.getNumero()))
				  ans.add(root.getNumero());
		  }
		  else if(root.getC() == '|'){
		    ans = firstpos(root.getFilhoDir());
		    aux = firstpos(root.getFilhoEsq());

		    for(int i =0; i < aux.size();i++){
		    	if(!ans.contains(aux.get(i)))
		    		ans.add(aux.get(i));
		    }

		  }
		  else if(root.getC() == '.'){
		    ans = firstpos(root.getFilhoEsq());

		    if( nullable(root.getFilhoEsq()) ){
		      aux = firstpos(root.getFilhoDir());

		      for(int i =0; i < aux.size();i++){
		    	  if(!ans.contains(aux.get(i)))
			    		ans.add(aux.get(i));
		        }
		    }
		  }
		  else if( root.getC() == '*' || root.getC() == '?' ){
		    ans = firstpos(root.getFilhoEsq());
		  }

		  return ans;
		}

		public ArrayList<Integer> lastpos(Node root){
		  ArrayList<Integer> ans = new ArrayList();
		  ArrayList<Integer> aux = new ArrayList();

		  if(!Regex.isOperator(root.getC(), false)){
			  if(!ans.contains(root.getNumero()))
				  ans.add(root.getNumero());
		}
		  else if(root.getC() == '|'){
		    ans = lastpos(root.getFilhoDir());
		    aux = lastpos(root.getFilhoEsq());

		    for(int i =0; i < aux.size();i++){
		    	if(!ans.contains(aux.get(i)))
		    		ans.add(aux.get(i));
		    }

		  }
		  else if(root.getC() == '.'){
		    ans = lastpos(root.getFilhoDir());

		    if( nullable(root.getFilhoDir()) ){
		      aux = lastpos(root.getFilhoEsq());

		      for(int i =0; i < aux.size();i++){
		    	  if(!ans.contains(aux.get(i)))
			    		ans.add(aux.get(i));
		        }
		    }
		  }
		  else if( root.getC() == '*' || root.getC() == '?' ){
		    ans = lastpos(root.getFilhoEsq());
		  }

		  return ans;
		}
		
	public void calcFollowpos() {
		followpos(this.root);
	}
		
	public void followpos(Node root) {
		
		if(root != null){
			
			
			if(root.getC()== '.') {
				ArrayList<Integer> last = lastpos(root.getFilhoEsq());
				for(int i =0;i < last.size();i++) {
					listLeaves.get(last.get(i)).addFollowpos(firstpos(root.getFilhoDir()));
				}
			}
			else if(root.getC() == '*') {
				ArrayList<Integer> last = lastpos(root);
				for(int i =0;i < last.size();i++) {
					listLeaves.get(last.get(i)).addFollowpos(firstpos(root));
				}
			}
			
			followpos(root.getFilhoEsq());
			followpos(root.getFilhoDir());
		}
		
	}
}