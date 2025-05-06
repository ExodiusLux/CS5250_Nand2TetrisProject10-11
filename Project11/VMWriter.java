package Project11;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class VMWriter {
    private FileWriter file_writer;
    public VMWriter(File file_Out) {
        try {
            file_writer = new FileWriter(file_Out);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void push_Write(String string_Segment, int Index) {
        if (string_Segment.equals("var")) {
            string_Segment = "local";
        }
        if (string_Segment.equals("field")) {
            string_Segment = "this";
        }
        try {
            file_writer.write("push " + string_Segment + " " + Index + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void pop_Write(String string_Segment, int Index) {
        if (string_Segment.equals("var")) {
            string_Segment = "local";
        }
        if (string_Segment.equals("field")) {
            string_Segment = "this";
        }
        try {
            file_writer.write("pop " + string_Segment + " " + Index + "\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void arithmetic_Write(String string_Command) {
        try {
            file_writer.write(string_Command + "\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void label_Write(String string_Label) {
        try {
            file_writer.write("label " + string_Label + "\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goto_Write(String string_Label) {
        try {
            file_writer.write("goto " + string_Label + "\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void if_Write(String string_Label) {
        try {
            file_writer.write("if-goto " + string_Label + "\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void call_Write(String string_Name, int num_Args) {
        try {
            file_writer.write("call " + string_Name + " " + num_Args + "\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void function_Write(String string_Name, int num_Locals) {
        try {
            file_writer.write("function " + string_Name + " " + num_Locals + "\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void return_Write() {
        try {
            file_writer.write("return\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        try {
            file_writer.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}