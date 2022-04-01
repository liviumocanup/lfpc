package lab4;

import lab4.steps.*;

import java.io.*;
import java.util.*;

public class Main {
    static HashSet<String> nonTerminalSet = new HashSet<>();
    static HashSet<String> terminalSet = new HashSet<>();
    static HashMap<String, List<String>> productionRules = new HashMap<>();
    static String startingSymbol = null;

    public static void main(String[] args) throws IOException {
        String pathToFile = "src\\lab4\\input.txt";
        readFile(pathToFile);

        Grammar grammar = new Grammar(startingSymbol, nonTerminalSet, terminalSet, productionRules);
        System.out.println(grammar);

        Step1.EliminateEpsilon(grammar);
        System.out.println(grammar);

        Step2.EliminateRenamings(grammar);
        System.out.println(grammar);

        Step3.EliminateNonProductive(grammar);
        System.out.println(grammar);

        Step4.EliminateInaccessible(grammar);
        System.out.println(grammar);

        Step5.ChomskyNormalForm(grammar);
        System.out.println(grammar);

        FileWriter myWriter = new FileWriter("output.txt");
        myWriter.write(String.valueOf(grammar));
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
                            List<String> tempList = new ArrayList<>();
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
