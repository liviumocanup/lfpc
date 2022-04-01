package lab4.steps;

import lab4.Grammar;

import java.util.*;

public class Step3 {
    public static void EliminateNonProductive(Grammar grammar){
        HashMap<String, List<String>> productionRules = grammar.productionRules;
        List<String> productive = new ArrayList<>();

        //iterate over ProductionRules
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            if(!productive.contains(entry.getKey())) {
                checkProductive(grammar, productive, new ArrayList<>(), entry.getKey());
            }
        }

        Iterator<String> iterator = grammar.nonTerminalSet.iterator();
        while (iterator.hasNext()) {
            String symbol = iterator.next();
            if (!productive.contains(symbol)) {
                grammar.RemoveSymbol(symbol);
                iterator.remove();
            }
        }
    }

    public static boolean checkProductive(Grammar grammar, List<String> productive, List<String> checked, String symbol){
        //iterate over the List symbol can derive into
        List<String> symbolDerivations = grammar.productionRules.get(symbol);
        boolean isProductive = true;
        checked.add(symbol);

        for (String symbolDerivation : symbolDerivations) {
            //check every character of derivation to be terminal
            for (int j = 0; j < symbolDerivation.length(); j++) {
                String currChar = Character.toString(symbolDerivation.charAt(j));
                if (grammar.isNonTerminal(currChar)) {
                    //check if nonTerminal characters are or can be productive
                    if (productive.contains(currChar) ||
                            (!checked.contains(currChar) && checkProductive(grammar, productive, checked, currChar)))
                        continue;
                    //otherwise not productive
                    isProductive = false;
                    break;
                }
            }
            if (isProductive) {
                productive.add(symbol);
                return true;
            }
            else isProductive = true;
        }
        return false;
    }
}
