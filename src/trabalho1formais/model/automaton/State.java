
package trabalho1formais.model.automaton;

import java.util.ArrayList;

/**
 *
 * @author nathan
 */
public class State {
    private String name;
    private boolean marked;
    private ArrayList<Integer> followpos;
    
    public State(String name) {
        this.name = name;
    }
    
    public State(ArrayList<Integer> f) {
    	String sName = "";
    	for(int i = 0;i<f.size();i++) {
    		sName = sName + f.get(i);
    	}
    	followpos = f;
        this.name = sName;
        marked = false;
    }
    
    
    public boolean getMarked(){
    	return marked;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
            return this.name;
    }

	public void setMarked() {
		marked = true;
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Integer> getFollowpos() {
		return followpos;
	}
}
