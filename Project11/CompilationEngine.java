package Project11;
import java.io.File;

public class CompilationEngine {
    private JackTokenizer jack_Token;
    private SymbolTable symbol_Table;
    private VMWriter vm_File_Writer;
    private String class_Name = "";
    private String subroutine_Name = "";
    private int label_Index;

    public CompilationEngine(File in_File, File out_File) {
        jack_Token = new JackTokenizer(in_File);
        symbol_Table = new SymbolTable();
        vm_File_Writer = new VMWriter(out_File);
        label_Index = 0;
    }

    public void class_Compile() {
        jack_Token.proceed();
        jack_Token.proceed();
        class_Name = jack_Token.getIdentifier();
        jack_Token.proceed();
        Class_Variable_Declar_Compile();
        SubRoutine_Compile();
        vm_File_Writer.close();
    }

    public void Class_Variable_Declar_Compile() {
        jack_Token.proceed();
        while (jack_Token.getKeyWord().equals("static") || jack_Token.getKeyWord().equals("field")) {
            String type;
            String string_Kind;
            if (jack_Token.getKeyWord().equals("static")) {
                string_Kind = "static";
            }
            else {
                string_Kind = "field";
            }
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                type = jack_Token.getIdentifier();
            }
            else {
                type = jack_Token.getKeyWord();
            }
            jack_Token.proceed();
            symbol_Table.define(jack_Token.getIdentifier(), type, string_Kind);
            jack_Token.proceed();
            while (jack_Token.getSymbol() == ',') {
                jack_Token.proceed();
                symbol_Table.define(jack_Token.getIdentifier(), type, string_Kind);
                jack_Token.proceed();
            }
            jack_Token.proceed();
        }
        if (jack_Token.getKeyWord().equals("function") || jack_Token.getKeyWord().equals("method") || jack_Token.getKeyWord().equals("constructor")) {
            jack_Token.decrement_Pointer();
            return;
        }
    }

    public void SubRoutine_Compile() {
        jack_Token.proceed();
        if (jack_Token.getSymbol() == '}' && jack_Token.getTokenType().equals("SYMBOL")) {
            return;
        }
        String strKeyword = "";
        if (jack_Token.getKeyWord().equals("function") || jack_Token.getKeyWord().equals("method") || jack_Token.getKeyWord().equals("constructor")) {
            strKeyword = jack_Token.getKeyWord();
            symbol_Table.subroutine_Start();
            if (jack_Token.getKeyWord().equals("method")) {
                symbol_Table.define("this", class_Name, "argument");
            }
            jack_Token.proceed();
        }
        String string_Type = "";
        if (jack_Token.getTokenType().equals("KEYWORD") && jack_Token.getKeyWord().equals("void")) {
            string_Type = "void";
            jack_Token.proceed();
        } 
        else if (jack_Token.getTokenType().equals("KEYWORD") && (jack_Token.getKeyWord().equals("int") || jack_Token.getKeyWord().equals("boolean") || jack_Token.getKeyWord().equals("char"))) {
            string_Type = jack_Token.getKeyWord();
            jack_Token.proceed();
        }
        else {
            string_Type = jack_Token.getIdentifier();
            jack_Token.proceed();
        }
        if (jack_Token.getTokenType().equals("IDENTIFIER")) {
            subroutine_Name = jack_Token.getIdentifier();
            jack_Token.proceed();
        }
        if (jack_Token.getSymbol() == '(') {
            Parameters_Compile();
        }
        jack_Token.proceed();
        if (jack_Token.getSymbol() == '{') {
            jack_Token.proceed();
        }
        while (jack_Token.getKeyWord().equals("var") && (jack_Token.getTokenType().equals("KEYWORD"))) {
            jack_Token.decrement_Pointer();
            VariableDec_Compile();
        }
        String strFunction = "";
        if (class_Name.length() != 0 && subroutine_Name.length() != 0) {
            strFunction += class_Name + "." + subroutine_Name;
        }
        vm_File_Writer.function_Write(strFunction, symbol_Table.variable_count("var"));
        if (strKeyword.equals("method")) {
            vm_File_Writer.push_Write("argument", 0);
            vm_File_Writer.pop_Write("pointer", 0);
        } 
        else if (strKeyword.equals("constructor")) {
            vm_File_Writer.push_Write("constant", symbol_Table.variable_count("field"));
            vm_File_Writer.call_Write("Memory.alloc", 1);
            vm_File_Writer.pop_Write("pointer", 0);
        }
        Statements_Compile();
        SubRoutine_Compile();
    }

    public void Parameters_Compile() {
        jack_Token.proceed();
        String type = "";
        String name = "";
        boolean has_Param_Bool = false;
        while (!(jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ')')) {
            if (jack_Token.getTokenType().equals("KEYWORD")) {
                has_Param_Bool = true;
                type = jack_Token.getKeyWord();
            } 
            else if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                type = jack_Token.getIdentifier();
            }
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                name = jack_Token.getIdentifier();
            }
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ',') {
                symbol_Table.define(name, type, "argument");
                jack_Token.proceed();
            }
        }
        if (has_Param_Bool) {
            symbol_Table.define(name, type, "argument");
        }
    }

    public void VariableDec_Compile() {
        jack_Token.proceed();
        String type = "";
        String name = "";
        if (jack_Token.getKeyWord().equals("var") && (jack_Token.getTokenType().equals("KEYWORD"))) {
            jack_Token.proceed();
        }
        if (jack_Token.getTokenType().equals("IDENTIFIER")) {
            type = jack_Token.getIdentifier();
            jack_Token.proceed();
        }
        else if (jack_Token.getTokenType().equals("KEYWORD")) {
            type = jack_Token.getKeyWord();
            jack_Token.proceed();
        }
        if (jack_Token.getTokenType().equals("IDENTIFIER")) {
            name = jack_Token.getIdentifier();
            jack_Token.proceed();
        }
        symbol_Table.define(name, type, "var");
        while ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == ',')) {
            jack_Token.proceed();
            name = jack_Token.getIdentifier();
            symbol_Table.define(name, type, "var");
            jack_Token.proceed();
        }
        if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == ';')) {
            jack_Token.proceed();
        }
    }
    public void Statements_Compile() {
        if (jack_Token.getSymbol() == '}' && (jack_Token.getTokenType().equals("SYMBOL"))) {
            return;
        } 
        else if (jack_Token.getKeyWord().equals("do") && (jack_Token.getTokenType().equals("KEYWORD"))) {
            Do_Compilation();
        } 
        else if (jack_Token.getKeyWord().equals("let") && (jack_Token.getTokenType().equals("KEYWORD"))) {
            Let_Compile();
        } 
        else if (jack_Token.getKeyWord().equals("if") && (jack_Token.getTokenType().equals("KEYWORD"))) {
            If_Compile();
        } 
        else if (jack_Token.getKeyWord().equals("while") && (jack_Token.getTokenType().equals("KEYWORD"))) {
            While_Compile();
        } 
        else if (jack_Token.getKeyWord().equals("return") && (jack_Token.getTokenType().equals("KEYWORD"))) {
            Return_Compile();
        }
        jack_Token.proceed();
        Statements_Compile();
    }
    public void Do_Compilation() {
        Call_Compilation();
        jack_Token.proceed();
        vm_File_Writer.pop_Write("temp", 0);
    }

    private void Call_Compilation() {
        jack_Token.proceed();
        String first = jack_Token.getIdentifier();
        int num_Arguments = 0;
        jack_Token.proceed();
        if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == '.')) {
            String objectName = first;
            jack_Token.proceed();
            jack_Token.proceed();
            first = jack_Token.getIdentifier();
            String strType = symbol_Table.getTypeOf(objectName);
            if (strType.equals("")) {
                first = objectName + "." + first;
            } 
            else {
                num_Arguments = 1;
                vm_File_Writer.push_Write(symbol_Table.getKindOf(objectName), symbol_Table.getIndexOf(objectName));
                first = symbol_Table.getTypeOf(objectName) + "." + first;
            }
            num_Arguments += Expression_List_Compile();
            jack_Token.proceed();
            vm_File_Writer.call_Write(first, num_Arguments);
        }
        else if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == '(')) {
            vm_File_Writer.push_Write("pointer", 0);
            num_Arguments = Expression_List_Compile() + 1;
            jack_Token.proceed();
            vm_File_Writer.call_Write(class_Name + "." + first, num_Arguments);
        }
    }

    public void Let_Compile() {
        jack_Token.proceed();
        String string_Var_Name = jack_Token.getIdentifier();
        jack_Token.proceed();
        boolean array_bool = false;
        if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == '[')) {
            array_bool = true;
            vm_File_Writer.push_Write(symbol_Table.getKindOf(string_Var_Name), symbol_Table.getIndexOf(string_Var_Name));
            Expression_Compile();
            jack_Token.proceed();
            vm_File_Writer.arithmetic_Write("add");
            jack_Token.proceed();
        }
        Expression_Compile();
        jack_Token.proceed();
        if (array_bool) {
            vm_File_Writer.pop_Write("temp", 0);
            vm_File_Writer.pop_Write("pointer", 1);
            vm_File_Writer.push_Write("temp", 0);
            vm_File_Writer.pop_Write("that", 0);
        } 
        else {
            vm_File_Writer.pop_Write(symbol_Table.getKindOf(string_Var_Name), symbol_Table.getIndexOf(string_Var_Name));
        }
    }

    public void While_Compile() {
        String second_Label = "LABEL_" + label_Index++;
        String first_Label = "LABEL_" + label_Index++;
        vm_File_Writer.label_Write(first_Label);
        jack_Token.proceed();
        Expression_Compile();
        jack_Token.proceed();
        vm_File_Writer.arithmetic_Write("not");
        vm_File_Writer.if_Write(second_Label);
        jack_Token.proceed();
        Statements_Compile();
        vm_File_Writer.goto_Write(first_Label);
        vm_File_Writer.label_Write(second_Label);
    }

    public void Return_Compile() {
        jack_Token.proceed();
        if (!((jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ';'))) {
            jack_Token.decrement_Pointer();
            Expression_Compile();
        } 
        else if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ';') {
            vm_File_Writer.push_Write("constant", 0);
        }
        vm_File_Writer.return_Write();
    }

    public void If_Compile() {
        String string_Label_Else = "LABEL_" + label_Index++;
        String String_Label_End = "LABEL_" + label_Index++;
        jack_Token.proceed();
        Expression_Compile();
        jack_Token.proceed();
        vm_File_Writer.arithmetic_Write("not");
        vm_File_Writer.if_Write(string_Label_Else);
        jack_Token.proceed();
        Statements_Compile();
        vm_File_Writer.goto_Write(String_Label_End);
        vm_File_Writer.label_Write(string_Label_Else);
        jack_Token.proceed();
        if (jack_Token.getTokenType().equals("KEYWORD") && jack_Token.getKeyWord().equals("else")) {
            jack_Token.proceed();
            jack_Token.proceed();
            Statements_Compile();
        } else {
            jack_Token.decrement_Pointer();
        }
        vm_File_Writer.label_Write(String_Label_End);
    }

    public void Expression_Compile() {
        Term_Compile();
        while (true) {
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.is_Operation()) {
                if (jack_Token.getSymbol() == '<') {
                    Term_Compile();
                    vm_File_Writer.arithmetic_Write("lt");
                } 
                else if (jack_Token.getSymbol() == '>') {
                    Term_Compile();
                    vm_File_Writer.arithmetic_Write("gt");
                } 
                else if (jack_Token.getSymbol() == '&') {
                    Term_Compile();
                    vm_File_Writer.arithmetic_Write("and");
                } 
                else if (jack_Token.getSymbol() == '+') {
                    Term_Compile();
                    vm_File_Writer.arithmetic_Write("add");
                } 
                else if (jack_Token.getSymbol() == '-') {
                    Term_Compile();
                    vm_File_Writer.arithmetic_Write("sub");
                } 
                else if (jack_Token.getSymbol() == '*') {
                    Term_Compile();
                    vm_File_Writer.call_Write("Math.multiply", 2);
                } 
                else if (jack_Token.getSymbol() == '/') {
                    Term_Compile();
                    vm_File_Writer.call_Write("Math.divide", 2);
                } 
                else if (jack_Token.getSymbol() == '=') {
                    Term_Compile();
                    vm_File_Writer.arithmetic_Write("eq");
                } 
                else if (jack_Token.getSymbol() == '|') {
                    Term_Compile();
                    vm_File_Writer.arithmetic_Write("or");
                }
            } 
            else {
                jack_Token.decrement_Pointer();
                break;
            }
        }
    }

    public void Term_Compile() {
        jack_Token.proceed();
        if (jack_Token.getTokenType().equals("IDENTIFIER")) {
            String prevIdentifier = jack_Token.getIdentifier();
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == '[') {
                vm_File_Writer.push_Write(symbol_Table.getKindOf(prevIdentifier), symbol_Table.getIndexOf(prevIdentifier));
                Expression_Compile();
                jack_Token.proceed();
                vm_File_Writer.arithmetic_Write("add");
                vm_File_Writer.pop_Write("pointer", 1);
                vm_File_Writer.push_Write("that", 0);
            }
            else if (jack_Token.getTokenType().equals("SYMBOL") && (jack_Token.getSymbol() == '(' || jack_Token.getSymbol() == '.')) {
                jack_Token.decrement_Pointer();
                jack_Token.decrement_Pointer();
                Call_Compilation();
            } 
            else {
                jack_Token.decrement_Pointer();
                vm_File_Writer.push_Write(symbol_Table.getKindOf(prevIdentifier), symbol_Table.getIndexOf(prevIdentifier));
            }
        } 
        else {
            if (jack_Token.getTokenType().equals("INT_CONST")) {
                vm_File_Writer.push_Write("constant", jack_Token.getIntegerValue());
            }
            else if (jack_Token.getTokenType().equals("STRING_CONST")) {
                String string_Token = jack_Token.getStringValue();
                vm_File_Writer.push_Write("constant", string_Token.length());
                vm_File_Writer.call_Write("String.new", 1);
                for (int i = 0; i < string_Token.length(); i++) {
                    vm_File_Writer.push_Write("constant", (int) string_Token.charAt(i));
                    vm_File_Writer.call_Write("String.appendChar", 2);
                }
            }
            else if (jack_Token.getTokenType().equals("KEYWORD") && jack_Token.getKeyWord().equals("this")) {
                vm_File_Writer.push_Write("pointer", 0);
            }
            else if (jack_Token.getTokenType().equals("KEYWORD") && (jack_Token.getKeyWord().equals("null") || jack_Token.getKeyWord().equals("false"))) {
                vm_File_Writer.push_Write("constant", 0);
            }
            else if (jack_Token.getTokenType().equals("KEYWORD") && jack_Token.getKeyWord().equals("true")) {
                vm_File_Writer.push_Write("constant", 0);
                vm_File_Writer.arithmetic_Write("not");
            }
            else if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == '(') {
                Expression_Compile();
                jack_Token.proceed();
            }
            else if (jack_Token.getTokenType().equals("SYMBOL") && (jack_Token.getSymbol() == '-' || jack_Token.getSymbol() == '~')) {
                char symbol = jack_Token.getSymbol();
                Term_Compile();
                if (symbol == '-') {
                    vm_File_Writer.arithmetic_Write("neg");
                } 
                else if (symbol == '~') {
                    vm_File_Writer.arithmetic_Write("not");
                }
            }
        }

    }

    public int Expression_List_Compile() {
        int num_Arguments = 0;
        jack_Token.proceed();
        if (jack_Token.getSymbol() == ')' && jack_Token.getTokenType().equals("SYMBOL")) {
            jack_Token.decrement_Pointer();
        } 
        else {
            num_Arguments = 1;
            jack_Token.decrement_Pointer();
            Expression_Compile();
        }
        while (true) {
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ',') {
                Expression_Compile();
                num_Arguments++;
            } 
            else {
                jack_Token.decrement_Pointer();
                break;
            }
        }
        return num_Arguments;
    }
}