package Project11;
import java.util.HashMap;
public class SymbolTable {
    private HashMap<String, Symbol> class_Table;
    private HashMap<String, Symbol> method_Table;
    private HashMap<String, Integer> index_Table;
    public SymbolTable() {
        class_Table = new HashMap<>();
        method_Table = new HashMap<>();
        index_Table = new HashMap<>();
        index_Table.put("static", 0);
        index_Table.put("field", 0);
        index_Table.put("argument", 0);
        index_Table.put("var", 0);
    }
    public void subroutine_Start() {
        method_Table.clear();
        index_Table.put("argument", 0);
        index_Table.put("var", 0);
    }
    public void define(String string_Name, String string_Type, String string_Kind) {
        int index = index_Table.get(string_Kind);
        Symbol symbol = new Symbol(string_Type, string_Kind, index);
        index++;
        index_Table.put(string_Kind, index);
        if (string_Kind.equals("argument") || string_Kind.equals("var")) {
            method_Table.put(string_Name, symbol);
        }
        else if (string_Kind.equals("static") || string_Kind.equals("field")) {
            class_Table.put(string_Name, symbol);
        }
    }
    public int variable_count(String string_Kind) {
        return index_Table.get(string_Kind);
    }
    public String getKindOf(String strName) {
        String kind;
        if (method_Table.containsKey(strName)) {
            kind = method_Table.get(strName).getKind();
        } 
        else if (class_Table.containsKey(strName)) {
            kind = class_Table.get(strName).getKind();
        } 
        else {
            kind = "none";
        }
        return kind;
    }
    public String getTypeOf(String strName) {
        String type;
        if (method_Table.containsKey(strName)) {
            type = method_Table.get(strName).getType();

        }
        else if (class_Table.containsKey(strName)) {
            type = class_Table.get(strName).getType();
        } 
        else {
            type = "";
        }
        return type;
    }
    public int getIndexOf(String strName) {
        Symbol symbol = null;
        int index;
        if (method_Table.containsKey(strName)) {
            symbol = method_Table.get(strName);
        } 
        else if (class_Table.containsKey(strName)) {
            symbol = class_Table.get(strName);
        }
        if (symbol == null) {
            index = -1;
        } 
        else {
            index = symbol.getNumber();
        }
        return index;
    }
}