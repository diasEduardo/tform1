/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.automaton;

/**
 *
 * @author nathan
 */


public class ViewTable {
    private String[] column;
    private Object [][] data;

    public String[] getColumn() {
        return column;
    }

    public void setColumn(String[] column) {
        this.column = column;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }
    
    public ViewTable(String[] column, Object[][] rows) {
        this.column = column;
        this.data = rows;
    }
}
