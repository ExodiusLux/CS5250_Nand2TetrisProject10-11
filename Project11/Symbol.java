package Project11;
public class Symbol {
    private String type;
    private String kind;
    private int number;

    public Symbol(String string_Type, String string_Kind, int int_number) {
        type = string_Type;
        kind = string_Kind;
        number = int_number;
    }
    public String getType() {
        return type;
    }
    public String getKind() {
        return kind;
    }
    public int getNumber() {
        return number;
    }
}