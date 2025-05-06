package Project11;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
public class JackTokenizer {
    private Scanner scan;
    private static ArrayList<String> keyWords;
    private static String symbols;
    private static String operations;
    private ArrayList<String> tokens;
    private String jack_code;
    private String token_type;
    private String key_word;
    private char symbol;
    private String identifier;
    private String string_Val;
    private int integer_value;
    private static ArrayList<String> libraries;
    private int pointer;
    private boolean first_Boolean;
    public JackTokenizer(File file) {
        try {
            scan = new Scanner(new FileReader(file));
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jack_code = "";
        while (scan.hasNextLine()) {
            String string_Line = scan.nextLine();
            while (string_Line.equals("") || hasComments(string_Line)) {
                if (hasComments(string_Line)) {
                    string_Line = remove_Comments(string_Line);
                }
                if (string_Line.trim().equals("")) {
                    if (scan.hasNextLine()) {
                        string_Line = scan.nextLine();
                    } 
                    else {
                        break;
                    }
                }
            }
            jack_code += string_Line.trim();
        }
        tokens = new ArrayList<String>();
        while (jack_code.length() > 0) {
            while (jack_code.charAt(0) == ' ') {
                jack_code = jack_code.substring(1);
            }
            for (int i = 0; i < keyWords.size(); i++) {
                if (jack_code.startsWith(keyWords.get(i).toString() + " ")) {
                    String keyword = keyWords.get(i).toString();
                    tokens.add(keyword);
                    jack_code = jack_code.substring(keyword.length());
                }
            }
            if (symbols.contains(jack_code.substring(0, 1))) {
                char symbol = jack_code.charAt(0);
                tokens.add(Character.toString(symbol));
                jack_code = jack_code.substring(1);
            }
            else if (Character.isDigit(jack_code.charAt(0))) {
                String value = jack_code.substring(0, 1);
                jack_code = jack_code.substring(1);
                while (Character.isDigit(jack_code.charAt(0))) {
                    value += jack_code.substring(0, 1);
                    jack_code = jack_code.substring(1);
                }
                tokens.add(value);
            }
            else if (jack_code.substring(0, 1).equals("\"")) {
                jack_code = jack_code.substring(1);
                String string = "\"";
                while ((jack_code.charAt(0) != '\"')) {
                    string += jack_code.charAt(0);
                    jack_code = jack_code.substring(1);
                }
                string = string + "\"";
                tokens.add(string);
                jack_code = jack_code.substring(1);
            }
            else if (Character.isLetter(jack_code.charAt(0)) || (jack_code.substring(0, 1).equals("_"))) {
                String string_Identifier = jack_code.substring(0, 1);
                jack_code = jack_code.substring(1);
                while ((Character.isLetter(jack_code.charAt(0))) || (jack_code.substring(0, 1).equals("_"))) {
                    string_Identifier += jack_code.substring(0, 1);
                    jack_code = jack_code.substring(1);
                }
                tokens.add(string_Identifier);
            }
            first_Boolean = true;
            pointer = 0;
        }
    }

    static {
        operations = "+-*/&|<>=";
        symbols = "{}()[].,;+-*/&|<>=-~";

        keyWords = new ArrayList<String>();
        keyWords.add("class");
        keyWords.add("constructor");
        keyWords.add("function");
        keyWords.add("method");
        keyWords.add("field");
        keyWords.add("static");
        keyWords.add("var");
        keyWords.add("int");
        keyWords.add("char");
        keyWords.add("boolean");
        keyWords.add("void");
        keyWords.add("true");
        keyWords.add("false");
        keyWords.add("null");
        keyWords.add("this");
        keyWords.add("do");
        keyWords.add("if");
        keyWords.add("else");
        keyWords.add("while");
        keyWords.add("return");
        keyWords.add("let");

        libraries = new ArrayList<String>();
        libraries.add("Array");
        libraries.add("Math");
        libraries.add("String");
        libraries.add("Array");
        libraries.add("Output");
        libraries.add("Screen");
        libraries.add("Keyboard");
        libraries.add("Memory");
        libraries.add("Sys");
        libraries.add("Square");
        libraries.add("SquareGame");
    }
    public boolean has_Tokens() {
        boolean has_More = false;
        if (pointer < tokens.size() - 1) {
            has_More = true;
        }
        return has_More;
    }
    public void proceed() {
        if (has_Tokens()) {
            if (!first_Boolean) {
                pointer++;
            }
            else if (first_Boolean) {
                first_Boolean = false;
            }
            String current_Item = tokens.get(pointer);
            if (keyWords.contains(current_Item)) {
                token_type = "KEYWORD";
                key_word = current_Item;
            } 
            else if (symbols.contains(current_Item)) {
                symbol = current_Item.charAt(0);
                token_type = "SYMBOL";
            } 
            else if (Character.isDigit(current_Item.charAt(0))) {
                integer_value = Integer.parseInt(current_Item);
                token_type = "INT_CONST";
            } 
            else if (current_Item.substring(0, 1).equals("\"")) {
                token_type = "STRING_CONST";
                string_Val = current_Item.substring(1, current_Item.length() - 1);
            } 
            else if ((Character.isLetter(current_Item.charAt(0))) || (current_Item.charAt(0) == '_')) {
                token_type = "IDENTIFIER";
                identifier = current_Item;
            }
        } 
        else {
            return;
        }
    }
    public void decrement_Pointer() {
        if (pointer > 0) {
            pointer--;
        }
    }
    private boolean hasComments(String string_Line) {
        boolean has_Comments_bool = false;
        if (string_Line.contains("//") || string_Line.contains("/*") || string_Line.trim().startsWith("*")) {
            has_Comments_bool = true;
        }
        return has_Comments_bool;
    }
    private String remove_Comments(String strLine) {
        String string_No_Comments = strLine;
        if (hasComments(strLine)) {
            int offSet;
            if (strLine.trim().startsWith("*")) {
                offSet = strLine.indexOf("*");
            } else if (strLine.contains("/*")) {
                offSet = strLine.indexOf("/*");
            } else {
                offSet = strLine.indexOf("//");
            }
            string_No_Comments = strLine.substring(0, offSet).trim();
        }
        return string_No_Comments;
    }
    public String getTokenType() {
        return token_type;

    }
    public String getKeyWord() {
        return key_word;
    }
    public char getSymbol() {
        return symbol;
    }
    public String getIdentifier() {
        return identifier;
    }
    public int getIntegerValue() {
        return integer_value;
    }
    public String getStringValue() {
        return string_Val;
    }
    public boolean is_Operation() {
        for (int i = 0; i < operations.length(); i++) {
            if (operations.charAt(i) == symbol) {
                return true;
            }
        }
        return false;
    }
}