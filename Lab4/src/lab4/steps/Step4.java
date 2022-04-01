package lab4.steps;

import lab4.Grammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Step4 {
    public static void EliminateInaccessible(Grammar grammar){
        List<String> accessible = new ArrayList<>();
        accessible.add("S");

        //iterate over ProductionRules
        checkAccessibility(grammar, new ArrayList<>(), accessible, "S");

        Iterator<String> iterator = grammar.nonTerminalSet.iterator();
        while (iterator.hasNext()) {
            String symbol = iterator.next();
            if (!accessible.contains(symbol)) {
                grammar.RemoveSymbol(symbol);
                iterator.remove();
            }
        }
    }

    public static void checkAccessibility(Grammar grammar, List<String> checked, List<String> accessible, String symbol){
        checked.add(symbol);
        for (String s : grammar.productionRules.get(symbol)) {
            for (int i = 0; i < s.length(); i++) {
                String currChar = Character.toString(s.charAt(i));
                if(!accessible.contains(currChar))
                    accessible.add(currChar);
                if (grammar.isNonTerminal(currChar) && !checked.contains(currChar)) {
                    checkAccessibility(grammar, checked, accessible, currChar);
                }
            }
        }
    }
}
