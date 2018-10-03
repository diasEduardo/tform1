/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Regular;
import trabalho1formais.model.automaton.Automaton;
import trabalho1formais.model.grammar.Grammar;
import trabalho1formais.model.regex.Regex;

/**
 *
 * @author eduardo
 */
public class jsonParser {

    public static void toJSON(Regular obj) {
        Gson gson = new Gson();
        String type = obj.getType();
        String fileName = "error";
        if (type.equals("GR")) {
            Grammar object = (Grammar) obj;
            fileName = object.getId() + "-" + object.getType();
        } else if (type.equals("ER")) {
            Regex object = (Regex) obj;
            fileName = object.getId() + "-" + object.getType();
        } else if (type.equals("AFD") || type.equals("AFND")) {
            Automaton object = (Automaton) obj;
            fileName = object.getId() + "-" + object.getType();
        }

        try {
            // 1. Java object to JSON, and save into a file
            gson.toJson(obj, new FileWriter(fileName + ".json"));
        } catch (IOException ex) {
            Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 2. Java object to JSON, and assign to a String
        String jsonInString = gson.toJson(obj);
        System.out.println(jsonInString);
        FileWriter arquivo;
        try {
            arquivo = new FileWriter(fileName + ".json", false);
            arquivo.write(jsonInString);
            arquivo.close();
        } catch (IOException ex) {
            Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
