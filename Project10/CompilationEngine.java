package Project10;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class CompilationEngine {
    private FileWriter file_Writer;
    private JackTokenizer jack_Token;
    private boolean first_Routine;

    public CompilationEngine(File inFile, File outFile) {
        try {
            file_Writer = new FileWriter(outFile);
            jack_Token = new JackTokenizer(inFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        first_Routine = true;
    }
    public void Class_Compile() {
        try {
            jack_Token.proceed();
            file_Writer.write("<class>\n");
            file_Writer.write("<keyword> class </keyword>\n");
            jack_Token.proceed();
            file_Writer.write("<identifier> " + jack_Token.getIdentifier() + " </identifier>\n");
            jack_Token.proceed();
            file_Writer.write("<symbol> { </symbol>\n");
            Class_Variable_Declar_Compile();
            SubRoutine_Compile();
            file_Writer.write("<symbol> } </symbol>\n");
            file_Writer.write("</class>\n");
            file_Writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Class_Variable_Declar_Compile() {
        jack_Token.proceed();
        try {
            while (jack_Token.getKeyWord().equals("static") || jack_Token.getKeyWord().equals("field")) {
                file_Writer.write("<classVarDec>\n");
                file_Writer.write("<keyword> " + jack_Token.getKeyWord() + " </keyword>\n");
                jack_Token.proceed();
                if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                    file_Writer.write("<identifier> " + jack_Token.getIdentifier() + "</identifier>\n");
                }
                else {
                    file_Writer.write("<keyword> " + jack_Token.getKeyWord() + "</keyword>\n");
                }
                jack_Token.proceed();
                file_Writer.write("<identifier> " + jack_Token.getIdentifier() + "</identifier>\n");
                jack_Token.proceed();
                if (jack_Token.getSymbol() == ',') {
                    file_Writer.write("<symbol> , </symbol>\n");
                    jack_Token.proceed();
                    file_Writer.write(("<identifier> " + jack_Token.getIdentifier() + "</identifier>\n"));
                    jack_Token.proceed();
                }
                file_Writer.write("<symbol> ; </symbol>\n");
                jack_Token.proceed();
                file_Writer.write("</classVarDec>\n");
            }

            if (jack_Token.getKeyWord().equals("function") || jack_Token.getKeyWord().equals("method") || jack_Token.getKeyWord().equals("constructor")) {
                jack_Token.decrement_Pointer();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SubRoutine_Compile() {
        boolean containsSubRoutines = false;
        jack_Token.proceed();
        try {
            if (jack_Token.getSymbol() == '}' && jack_Token.getTokenType().equals("SYMBOL")) {
                return;
            }
            if ((jack_Token.getKeyWord().equals("function") && (first_Routine) || jack_Token.getKeyWord().equals("method") || jack_Token.getKeyWord().equals("constructor"))) {
                first_Routine = false;
                file_Writer.write("<subroutineDec>\n");
                containsSubRoutines = true;
            }
            if (jack_Token.getKeyWord().equals("function") || jack_Token.getKeyWord().equals("method") || jack_Token.getKeyWord().equals("constructor")) {
                containsSubRoutines = true;
                file_Writer.write("<keyword> " + jack_Token.getKeyWord() + " </keyword>\n");
                jack_Token.proceed();
            }
            if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                file_Writer.write("<identifier> " + jack_Token.getIdentifier() + " </identifier>\n");
                jack_Token.proceed();
            }
            else if (jack_Token.getTokenType().equals("KEYWORD")) {
                file_Writer.write("<keyword> " + jack_Token.getKeyWord() + "</keyword>\n");
                jack_Token.proceed();
            }
            if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                file_Writer.write("<identifier> " + jack_Token.getIdentifier() + " </identifier>\n");
                jack_Token.proceed();
            }
            if (jack_Token.getSymbol() == '(') {
                file_Writer.write("<symbol> ( </symbol>\n");
                file_Writer.write("<parameterList>");
                file_Writer.write(" ");
                Parameters_Compile();
                file_Writer.write("</parameterList>\n");
                file_Writer.write("<symbol> ) </symbol>\n");

            }
            jack_Token.proceed();
            if (jack_Token.getSymbol() == '{') {
                file_Writer.write("<subroutineBody>\n");
                file_Writer.write("<symbol> { </symbol>\n");
                jack_Token.proceed();
            }
            while (jack_Token.getKeyWord().equals("var") && (jack_Token.getTokenType().equals("KEYWORD"))) {
                file_Writer.write("<varDec>\n ");
                jack_Token.decrement_Pointer();
                VariableDec_Compile();
                file_Writer.write(" </varDec>\n");
            }
            file_Writer.write("<statements>\n");
            Statements_Compile();
            file_Writer.write("</statements>\n");
            file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
            if (containsSubRoutines) {
                file_Writer.write("</subroutineBody>\n");
                file_Writer.write("</subroutineDec>\n");
                first_Routine = true;
            }
            SubRoutine_Compile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Parameters_Compile() {
        jack_Token.proceed();
        try {
            while (!(jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ')')) {
                if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                    file_Writer.write("<identifier> " + jack_Token.getIdentifier() + " </identifier>\n");
                    jack_Token.proceed();
                } else if (jack_Token.getTokenType().equals("KEYWORD")) {
                    file_Writer.write("<keyword> " + jack_Token.getKeyWord() + "</keyword>\n");
                    jack_Token.proceed();
                }
                else if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == ',')) {
                    file_Writer.write("<symbol> , </symbol>\n");
                    jack_Token.proceed();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void VariableDec_Compile() {
        jack_Token.proceed();
        try {

            if (jack_Token.getKeyWord().equals("var") && (jack_Token.getTokenType().equals("KEYWORD"))) {
                file_Writer.write("<keyword> var </keyword>\n");
                jack_Token.proceed();
            }
            if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                file_Writer.write("<identifier> " + jack_Token.getIdentifier() + "</identifier>\n");
                jack_Token.proceed();
            }
            else if (jack_Token.getTokenType().equals("KEYWORD")) {
                file_Writer.write("<keyword> " + jack_Token.getKeyWord() + " </keyword>\n");
                jack_Token.proceed();
            }
            if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                file_Writer.write("<identifier> " + jack_Token.getIdentifier() + "</identifier>\n");
                jack_Token.proceed();
            }
            if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == ',')) {
                file_Writer.write("<symbol> , </symbol>\n");
                jack_Token.proceed();
                file_Writer.write(("<identifier> " + jack_Token.getIdentifier() + "</identifier>\n"));
                jack_Token.proceed();
            }
            if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == ';')) {
                file_Writer.write("<symbol> ; </symbol>\n");
                jack_Token.proceed();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Statements_Compile() {
        try {
            if (jack_Token.getSymbol() == '}' && (jack_Token.getTokenType().equals("SYMBOL"))) {
                return;
            } 
            else if (jack_Token.getKeyWord().equals("do") && (jack_Token.getTokenType().equals("KEYWORD"))) {
                file_Writer.write("<doStatement>\n ");
                Do_Compilation();
                file_Writer.write((" </doStatement>\n"));
            } 
            else if (jack_Token.getKeyWord().equals("let") && (jack_Token.getTokenType().equals("KEYWORD"))) {
                file_Writer.write("<letStatement>\n ");
                Let_Compile();
                file_Writer.write((" </letStatement>\n"));
            } 
            else if (jack_Token.getKeyWord().equals("if") && (jack_Token.getTokenType().equals("KEYWORD"))) {
                file_Writer.write("<ifStatement>\n ");
                If_Compile();
                file_Writer.write((" </ifStatement>\n"));
            } 
            else if (jack_Token.getKeyWord().equals("while") && (jack_Token.getTokenType().equals("KEYWORD"))) {
                file_Writer.write("<whileStatement>\n ");
                While_Compile();
                file_Writer.write((" </whileStatement>\n"));
            } 
            else if (jack_Token.getKeyWord().equals("return") && (jack_Token.getTokenType().equals("KEYWORD"))) {
                file_Writer.write("<returnStatement>\n ");
                Return_Compile();
                file_Writer.write((" </returnStatement>\n"));
            }
            jack_Token.proceed();
            Statements_Compile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Do_Compilation() {
        try {
            if (jack_Token.getKeyWord().equals("do")) {
                file_Writer.write("<keyword> do </keyword>\n");
            }
            Call_Compilation();
            jack_Token.proceed();
            file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void Call_Compilation() {
        jack_Token.proceed();
        try {
            file_Writer.write("<identifier> " + jack_Token.getIdentifier() + "</identifier>\n");
            jack_Token.proceed();
            if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == '.')) {
                file_Writer.write("<symbol> " + jack_Token.getSymbol() + "</symbol>\n");
                jack_Token.proceed();
                file_Writer.write("<identifier> " + jack_Token.getIdentifier() + " </identifier>\n");
                jack_Token.proceed();
                file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
                file_Writer.write("<expressionList>\n");
                file_Writer.write(" ");
                Expression_List_Compile();
                file_Writer.write("</expressionList>\n");
                jack_Token.proceed();
                file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");


            }
            else if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == '(')) {
                file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
                file_Writer.write("<expressionList>\n");
                Expression_List_Compile();
                file_Writer.write("</expressionList>\n");
                jack_Token.proceed();
                file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Let_Compile() {
        try {
            file_Writer.write("<keyword>" + jack_Token.getKeyWord() + "</keyword>\n");
            jack_Token.proceed();
            file_Writer.write("<identifier> " + jack_Token.getIdentifier() + " </identifier>\n");
            jack_Token.proceed();
            if ((jack_Token.getTokenType().equals("SYMBOL")) && (jack_Token.getSymbol() == '[')) {
                file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
                Expression_Compile();
                jack_Token.proceed();
                if ((jack_Token.getTokenType().equals("SYMBOL")) && ((jack_Token.getSymbol() == ']'))) {
                    file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
                }
                jack_Token.proceed();
            }
            file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
            Expression_Compile();
            file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
            jack_Token.proceed();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void While_Compile() {
        try {
            file_Writer.write("<keyword>" + jack_Token.getKeyWord() + "</keyword>\n");
            jack_Token.proceed();
            file_Writer.write("<symbol>" + jack_Token.getSymbol() + "</symbol>\n");
            Expression_Compile();
            jack_Token.proceed();
            file_Writer.write("<symbol>" + jack_Token.getSymbol() + "</symbol>\n");
            jack_Token.proceed();
            file_Writer.write("<symbol>" + jack_Token.getSymbol() + "</symbol>\n");
            file_Writer.write("<statements>\n");
            Statements_Compile();
            file_Writer.write("</statements>\n");
            file_Writer.write("<symbol>" + jack_Token.getSymbol() + "</symbol>\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Return_Compile() {
        try {
            file_Writer.write("<keyword> return </keyword>\n");
            jack_Token.proceed();
            if (!((jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ';'))) {
                jack_Token.decrement_Pointer();
                Expression_Compile();
            }
            if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ';') {
                file_Writer.write("<symbol> ; </symbol>\n");
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void If_Compile() {
        try {
            file_Writer.write("<keyword> if </keyword>\n");
            jack_Token.proceed();
            file_Writer.write("<symbol> ( </symbol>\n");
            Expression_Compile();
            file_Writer.write("<symbol> ) </symbol>\n");
            jack_Token.proceed();
            file_Writer.write("<symbol> { </symbol>\n");
            jack_Token.proceed();
            file_Writer.write("<statements>\n");
            Statements_Compile();
            file_Writer.write("</statements>\n");
            file_Writer.write("<symbol> } </symbol>\n");
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("KEYWORD") && jack_Token.getKeyWord().equals("else")) {
                file_Writer.write("<keyword> else </keyword>\n");
                jack_Token.proceed();
                file_Writer.write("<symbol> { </symbol>\n");
                jack_Token.proceed();
                file_Writer.write("<statements>\n");
                Statements_Compile();
                file_Writer.write("</statements>\n");
                file_Writer.write("<symbol> } </symbol>\n");
            } 
            else {
                jack_Token.decrement_Pointer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Expression_Compile() {
        try {
            file_Writer.write("<expression>\n");
            Term_Compile();
            while (true) {
                jack_Token.proceed();
                if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.Is_Operation()) {
                    if (jack_Token.getSymbol() == '<') {
                        file_Writer.write("<symbol> &lt; </symbol>\n");
                    } 
                    else if (jack_Token.getSymbol() == '>') {
                        file_Writer.write("<symbol> &gt; </symbol>\n");
                    } 
                    else if (jack_Token.getSymbol() == '&') {
                        file_Writer.write("<symbol> &amp; </symbol>\n");
                    } 
                    else {
                        file_Writer.write("<symbol> " + jack_Token.getSymbol() + " </symbol>\n");
                    }
                    Term_Compile();
                } else {
                    jack_Token.decrement_Pointer();
                    break;
                }
            }
            file_Writer.write("</expression>\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Term_Compile() {
        try {
            file_Writer.write("<term>\n");
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("IDENTIFIER")) {
                String prevIdentifier = jack_Token.getIdentifier();
                jack_Token.proceed();
                if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == '[') {
                    file_Writer.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    file_Writer.write("<symbol> [ </symbol>\n");
                    Expression_Compile();
                    jack_Token.proceed();
                    file_Writer.write("<symbol> ] </symbol>\n");
                }
                else if (jack_Token.getTokenType().equals("SYMBOL") && (jack_Token.getSymbol() == '(' || jack_Token.getSymbol() == '.')) {
                    jack_Token.decrement_Pointer();
                    jack_Token.decrement_Pointer();
                    Call_Compilation();

                } 
                else {
                    file_Writer.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    jack_Token.decrement_Pointer();
                }
            } else {
                if (jack_Token.getTokenType().equals("INT_CONST")) {
                    file_Writer.write("<integerConstant> " + jack_Token.getIntegerValue() + " </integerConstant>\n");

                }
                else if (jack_Token.getTokenType().equals("STRING_CONST")) {
                    file_Writer.write("<stringConstant> " + jack_Token.getStringValue() + " </stringConstant>\n");
                }
                else if (jack_Token.getTokenType().equals("KEYWORD") && (jack_Token.getKeyWord().equals("this") || jack_Token.getKeyWord().equals("null")
                        || jack_Token.getKeyWord().equals("false") || jack_Token.getKeyWord().equals("true"))) {
                    file_Writer.write("<keyword> " + jack_Token.getKeyWord() + " </keyword>\n");
                }
                else if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == '(') {
                    file_Writer.write("<symbol>" + jack_Token.getSymbol() + "</symbol>\n");
                    Expression_Compile();
                    jack_Token.proceed();
                    file_Writer.write("<symbol> " + jack_Token.getSymbol() + "</symbol>\n");
                }
                else if (jack_Token.getTokenType().equals("SYMBOL") && (jack_Token.getSymbol() == '-' || jack_Token.getSymbol() == '~')) {
                    file_Writer.write("<symbol> " + jack_Token.getSymbol() + "</symbol>\n");
                    Term_Compile();
                }
            }
            file_Writer.write("</term>\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Expression_List_Compile() {
        jack_Token.proceed();
        if (jack_Token.getSymbol() == ')' && jack_Token.getTokenType().equals("SYMBOL")) {
            jack_Token.decrement_Pointer();
        } 
        else {
            jack_Token.decrement_Pointer();
            Expression_Compile();
        }
        while (true) {
            jack_Token.proceed();
            if (jack_Token.getTokenType().equals("SYMBOL") && jack_Token.getSymbol() == ',') {
                try {
                    file_Writer.write("<symbol> , </symbol>\n");
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
                Expression_Compile();
            } 
            else {
                jack_Token.decrement_Pointer();
                break;
            }
        }
    }
}