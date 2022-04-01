package lab4.steps;

import lab4.Grammar;

import java.util.*;

public class Step1 {
    public static void EliminateEpsilon(Grammar grammar){
        HashMap<String, List<String>> productionRules = grammar.productionRules;
        List<String> emptySymbols;

        do {
            emptySymbols = new ArrayList<>();
            //iterate over ProductionRules
            for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
                //iterate over the List symbol can derive into
                for (int i = 0; i < entry.getValue().size(); i++) {
                    //if derives into empty string
                    if (entry.getValue().get(i).equals("ε") || entry.getValue().get(i).equals("")) {
                        emptySymbols.add(entry.getKey());
                        grammar.productionRules.get(entry.getKey()).remove(i);
                    }
                }
            }

            for (String emptySymbol : emptySymbols) {
                HashSet<String> list = new HashSet<>();
                for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
                    for (int j = 0; j < entry.getValue().size(); j++) {
                        getAllCombinations(entry.getValue().get(j), list, emptySymbol.charAt(0));
                    }
                    for (String s : list) {
                        if (!grammar.productionRules.get(entry.getKey()).contains(s))
                            grammar.productionRules.get(entry.getKey()).add(s);
                    }
                    list = new HashSet<>();
                }

            }
        }while(!emptySymbols.isEmpty());
    }

    private static void getAllCombinations(String current, Set<String> combinations, char c){
        combinations.add(current);
        for(int i = 0;i<current.length();i++){
            if(current.charAt(i) == c)
                getAllCombinations(current.substring(0,i)+current.substring(i+1),combinations,c);
        }
    }
}
