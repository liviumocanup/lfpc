package implementation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputPath = "src/implementation/input.txt";
        String content = fileToString(inputPath);
        printTokens(content);
    }

    private static String fileToString(String path) throws IOException {
        return Files.readString(Paths.get(path), StandardCharsets.US_ASCII);
    }

    private static void printTokens(String inputString) throws IOException {
        Lexer lexer = new Lexer(inputString);
        List<Token> tokens = lexer.getTokens();

        FileWriter myWriter = new FileWriter("output.txt");
        int i=0;
        for (Token token : tokens) {
            if(i==5) {
                System.out.println();
                myWriter.write("\n");
                i = 0;
            }
            System.out.print(token+" ");
            myWriter.write(token.toString()+" ");
            i++;
        }
        myWriter.close();
    }
}
