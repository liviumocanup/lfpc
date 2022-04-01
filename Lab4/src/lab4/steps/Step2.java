package lab4.steps;

import lab4.Grammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step2 {
    public static void EliminateRenamings(Grammar grammar){
        HashMap<String, List<String>> productionRules = grammar.productionRules;

        boolean loop = false;
        while(true){
            //iterate over ProductionRules
            for(Map.Entry<String, List<String>> entry : productionRules.entrySet()){
                //iterate over the List the symbol can derive into
                for(int i=0; i < entry.getValue().size(); i++){
                    //if derives into nonterminal single symbol
                    String derivesInto = entry.getValue().get(i);
                    if(derivesInto.length()==1 && grammar.isNonTerminal(derivesInto)){
                        loop = true;
                        //add everything the renaming derives into to the symbol derivation list
                        addAllNoDuplicates(entry.getValue(), productionRules.get(derivesInto));
                        //delete the renaming
                        productionRules.get(entry.getKey()).remove(i);
                    }
                }
            }

            if(!loop)
                break;
            loop = false;
        }
    }

    public static void addAllNoDuplicates(List<String> addTo, List<String> addFrom){
        for(String s : addFrom){
            if(!addTo.contains(s)){
                addTo.add(s);
            }
        }
    }

}
