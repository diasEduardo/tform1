/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais;

/**
 *
 * @author Eduardo
 */
public abstract class Regular {
        // typeof GR/ER/AF
	private String type;

	
	public Regular(String type){
		this.type = type;
	}
	
	// Titulo
	public String getType(){
		return this.type;
	}
	
	

	
	
}
