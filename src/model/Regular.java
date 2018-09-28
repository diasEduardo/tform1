/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Eduardo
 */
public abstract class Regular {
        // typeof GR/ER/AF
	private String type;
        public static char epilsonSimbol = 'ε';
	
	public Regular(String type){
		this.type = type;
	}
	
	
	public String getType(){
		return this.type;
	}
	
	

	
	
}
