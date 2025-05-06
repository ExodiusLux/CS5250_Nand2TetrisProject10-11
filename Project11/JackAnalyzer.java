package Project11;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class JackAnalyzer {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the name of the file to be compiled: ");
        String jackFile = scan.nextLine();
        File jackFileDir = new File(jackFile);
        ArrayList<File> files = new ArrayList<>();
        if (jackFileDir.isFile() && jackFile.endsWith(".jack")) {
            files.add(jackFileDir);
        } 
        else if (jackFileDir.isDirectory()) {
            files = getJackFiles(jackFileDir);
        }

        for (File file : files) {
            String fileOutName = file.toString().substring(0, file.toString().length() - 5) + ".vm";
            File fileOutFile = new File(fileOutName);
            CompilationEngine compilationEngine = new CompilationEngine(file, fileOutFile);
            compilationEngine.class_Compile();
        }
        scan.close();
    }
    public static ArrayList<File> getJackFiles(File jackFileDir) {
        File[] files = jackFileDir.listFiles();
        ArrayList<File> fResults = new ArrayList<>();
        if (files != null) for (File file : files) {
            if (file.getName().endsWith(".jack")) fResults.add(file);
        }
        return fResults;
    }
}