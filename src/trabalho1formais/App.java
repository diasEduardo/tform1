package trabalho1formais;

import trabalho1formais.view.View;
/**
 *
 * @author nathan
 */
public class App {
    public static View view;
   
    public static void main(String[] args) {
        view = new View();
        view.show(true);
    }
    
}
