/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.grammar;

/**
 *
 * @author nathan
 */
public class Production {
    private String current;
	private char generated;
	private String next;
	
	public Production(String current, char generated, String next) {
		this.current = current;
		this.generated = generated;
		this.next = next;
	}

	public String getCurrent() {
		return current;
	}
	
	public void setCurrent(String current) {
		this.current = current;
	}
	
	public char getGenerated() {
		return generated;
	}
	
	public void setGenerated(char generated) {
		this.generated = generated;
	}

	public String getNext() {
		return next;
	}
	
	public void setNext(String next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Production [current=" + current + ", generated=" + generated
				+ ", next=" + next + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Production other = (Production) obj;
		if( this.current.equals(other.getCurrent()) && 
				this.generated == other.getGenerated() &&
				this.next.equals(other.getNext()) )
			return true;
		
		return false;
	}
}
