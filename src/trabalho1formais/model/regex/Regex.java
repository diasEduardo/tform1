/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1formais.model.regex;

import trabalho1formais.Regular;

/**
 *
 * @author nathan
 */
public class Regex extends Regular {

    private String regex, id;
    public static final String allowedSimbols = "abcdefghijklmnopqrstuvwxyz0123456789&";
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

}
