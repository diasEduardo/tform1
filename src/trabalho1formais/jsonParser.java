/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import static java.time.Clock.system;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import model.Regular;
import trabalho1formais.model.automaton.Automaton;
import trabalho1formais.model.automaton.State;
import trabalho1formais.model.automaton.Transitions;
import trabalho1formais.model.grammar.Grammar;
import trabalho1formais.model.regex.Regex;

/**
 *
 * @author eduardo
 */
public class jsonParser {
    
    public static String jsonPath = "jsons/";

    public static void objectToJSON(Regular obj) {
        Gson gson = new Gson();
        String type = obj.getType();
        String fileName = "error";
        if (type.equals("GR")) {
            Grammar object = (Grammar) obj;
            object.setJsonType("GR");
            fileName = object.getId() + "-" + object.getType();
        } else if (type.equals("ER")) {
            Regex object = (Regex) obj;
            object.setJsonType("ER");
            fileName = object.getId() + "-" + object.getType();
        } else if (type.equals("AFD") || type.equals("AFND")) {
            Automaton object = (Automaton) obj;
            object.setJsonType("AF");
            fileName = object.getId() + "-" + object.getType();
        }

        try {
            // 1. Java object to JSON, and save into a file
            gson.toJson(obj, new FileWriter(jsonPath.concat(fileName) + ".json", true));
        } catch (IOException ex) {
            Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 2. Java object to JSON, and assign to a String
        String jsonInString = gson.toJson(obj);
        System.out.println(jsonInString);
        FileWriter arquivo;
        try {
            arquivo = new FileWriter(fileName + ".json");
            arquivo.write(jsonInString);
            arquivo.close();
        } catch (IOException ex) {
            Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Regular jsonToObject(String path) {
        BufferedReader br = null;
        Gson gson = new Gson();
        Regular reg = null;
        
        try {
            String json = "";
            File file = new File(path);
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                json += st.replaceAll("\n", "");
            }
            
            if (json.contains("\"jsonType\":\"GR\"")) {
                reg = gson.fromJson(json, Grammar.class);
            } else if (json.contains("\"jsonType\":\"ER\"")) {
                reg = gson.fromJson(json, Regex.class);
            } else if (json.contains("\"jsonType\":\"AF\"")) {
                /*
                por algum motivo o gson não consegue tratar as transições, mesmo que o json criado esteja em um formato valido
                 */
                JsonParser parser = new JsonParser();
                //cria um obj json para fazer o parser gson     
                Transitions tempTransitions = new Transitions();
                String jsonInString = gson.toJson(tempTransitions);
                JsonObject tempTransitionsJson = (JsonObject) parser.parse(jsonInString);
                //substitui a sub arvore de transitions
                JsonObject atJson = (JsonObject) parser.parse(json);
                JsonElement transitionsJson = atJson.get("transitions");
                atJson.remove("transitions");
                atJson.add("transitions", tempTransitionsJson);
                reg = gson.fromJson(atJson, Automaton.class);
                //obtem as transições e faz o link entre estados
                Automaton at = (Automaton) reg;
                tempTransitions = parseTransitions(transitionsJson, at);
                at.setTransitions(tempTransitions);
                at.setInitialState(at.getState(at.getInitialState().getName()));
                ArrayList<State> finalStates = new ArrayList<State>();
                for (State old : at.getFinalStates()) {
                    State s = at.getState(old.getName());
                    finalStates.add(s);
                }
                at.setFinalStates(finalStates);
                reg = at;
                int a2 = 1;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return reg;
    }

    private static Transitions parseTransitions(JsonElement transitionsJson, Automaton at) {
        Transitions transitions = new Transitions();
        JsonObject obj = transitionsJson.getAsJsonObject();
        for (Entry<String, JsonElement> root : obj.entrySet()) {
            String class1 = root.getKey();
            JsonElement transitions1 = root.getValue();
            JsonObject obj1 = transitions1.getAsJsonObject();
            for (Entry<String, JsonElement> level1 : obj1.entrySet()) {
                String current = level1.getKey();
                State currentS = at.getState(current);
                JsonElement alphaNext = level1.getValue();
                JsonObject obj2 = alphaNext.getAsJsonObject();
                for (Entry<String, JsonElement> level2 : obj2.entrySet()) {
                    String alpha = level2.getKey();
                    JsonElement next = level2.getValue();
                    String a = next.toString().replaceAll("\"name\":", "").replaceAll("[\\{\\}\\]\\[\\\"]", "");
                    for (String nextState : a.split(",")) {
                        System.out.print(current + " -");
                        System.out.print(alpha + " - ");
                        System.out.print(nextState + "\n");
                        State nextS = at.getState(nextState);
                        transitions.addTransition(currentS, alpha.toCharArray()[0], nextS);
                    }
                }
            }
        }
        return transitions;
    }

}
