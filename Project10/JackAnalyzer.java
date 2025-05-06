package Project10;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class JackAnalyzer {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the name of the file to be compiled: ");
        String jack_File = scan.nextLine();
        File jack_File_Directory = new File(jack_File);
        File file_Out;
        String file_Name_Out = "";
        ArrayList<File> files = new ArrayList<>();
        if (jack_File_Directory.isFile() && jack_File.endsWith(".jack")) {
            files.add(jack_File_Directory);
            file_Name_Out = jack_File.substring(0, jack_File.length() - 5);
        }
        else if (jack_File_Directory.isDirectory()) {
            files = getJackFiles(jack_File_Directory);
            file_Name_Out = jack_File;
        }
        file_Name_Out = file_Name_Out + ".xml";
        file_Out = new File(file_Name_Out);
        FileWriter file_writer = null;
        try {
            file_writer = new FileWriter(file_Out);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        for (File file : files) {
            String file_Out_Name = file.toString().substring(0, file.toString().length() - 5) + ".xml";
            File file_Out_File = new File(file_Out_Name);
            CompilationEngine Compilation_Engine = new CompilationEngine(file, file_Out_File);
            Compilation_Engine.Class_Compile();
        }
        try {
            file_writer.close();
            scan.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<File> getJackFiles(File jack_File_Directory) {
        File[] files = jack_File_Directory.listFiles();
        ArrayList<File> final_Results = new ArrayList<>();
        if (files != null) for (File file : files) {
            if (file.getName().endsWith(".jack")) final_Results.add(file);
        }
        return final_Results;
    }
}
