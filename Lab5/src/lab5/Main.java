package lab5;

import lab5.Parsing.*;
import lab5.PrepareGrammar.*;

import java.io.*;
import java.util.*;

public class Main {
    static HashSet<String> nonTerminalSet = new HashSet<>();
    static HashSet<String> terminalSet = new HashSet<>();
    static HashMap<String, HashSet<String>> productionRules = new HashMap<>();
    static String startingSymbol = null;

    public static void main(String[] args) throws IOException {
        String pathToFile = "src\\lab5\\input.txt";
        readFile(pathToFile);
        String word = "ababacdabae";

        Grammar grammar = new Grammar(startingSymbol, nonTerminalSet, terminalSet, productionRules);
        System.out.println("Given Grammar:");
        System.out.println(grammar+"\n");


        System.out.println("Eliminating Left Recursion:");
        RemoveLeftRecursion.rlr(grammar);
        System.out.println(grammar+"\n");


        System.out.println("Eliminating Left Factoring:");
        RemoveLeftFactoring.rlf(grammar);
        System.out.println(grammar+"\n");


        System.out.println("Creating Parsing.First:");
        First.initializeFirst(grammar);
        System.out.println(grammar.first+"\n");


        System.out.println("Creating Parsing.Follow:");
        Follow.createFollow(grammar);
        System.out.println(grammar.follow+"\n");


        System.out.println("Creating Parsing.Table:");
        Table.createParsingTable(grammar);
        System.out.println(grammar.parsingTable +"\n");


        System.out.println("Parsing the word "+word+":");
        System.out.println("Stack \t\tInput \t\tAction");
        CheckWord.parse(word, grammar);


        FileWriter myWriter = new FileWriter("output.txt");
        myWriter.write(grammar +"\n\n"+grammar.first+"\n"+grammar.follow+"\n"+grammar.parsingTable +"\n");
        myWriter.close();
    }


    public static void readFile(String pathName) throws IOException {
        File file = new File(pathName);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            switch (st.charAt(0)){
                case 'G':
                    startingSymbol = Character.toString(st.charAt(st.indexOf(')')-1));
                    break;
                case 'V':
                    String[] splitSymbols = st.substring(st.indexOf('{')+1, st.length()-1).split("[\\s,]+");
                    if(st.charAt(1) == 'n' || st.charAt(1) == 'N'){
                        for (String s : splitSymbols) {
                            nonTerminalSet.add(s.trim());
                        }
                    }
                    else if(st.charAt(1) == 't' || st.charAt(1) == 'T'){
                        for (String s : splitSymbols) {
                            terminalSet.add(s.trim());
                        }
                    }
                    break;
                default:
                    if(st.contains("->")) {
                        String[] transitionSymbols;
                        if(Character.isDigit(st.charAt(0)))
                            transitionSymbols = st.substring(st.indexOf('.') + 1).split("[->]+");
                        else transitionSymbols = st.split("[->]+");

                        String first = transitionSymbols[0].trim();
                        String second = transitionSymbols[1].trim();

                        if (!productionRules.containsKey(first))
                        {
                            HashSet<String> tempList = new HashSet<>();
                            tempList.add(second);
                            productionRules.put(first, tempList);
                        }
                        else
                        {
                            productionRules.get(first).add(second);
                        }
                    }
                    break;
            }
        }
    }
}