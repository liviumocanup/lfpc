package lab4;

import java.util.*;

public class Grammar {
    public String startingSymbol;
    public HashSet<String> nonTerminalSet;
    public HashSet<String> terminalSet;
    public HashMap<String, List<String>> productionRules;

    public Grammar(String startingSymbol, HashSet<String> nonTerminalSet, HashSet<String> terminalSet, HashMap<String, List<String>> productionRules) {
        this.startingSymbol = startingSymbol;
        this.nonTerminalSet = nonTerminalSet;
        this.terminalSet = terminalSet;
        this.productionRules = productionRules;
    }

    public boolean isNonTerminal(String checkSymbol){
        return nonTerminalSet.contains(checkSymbol);
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "\nstartingSymbol = '" + startingSymbol + '\'' +
                ",\nnonTerminalSet = " + nonTerminalSet +
                ",\nterminalSet = " + terminalSet +
                ",\nproductionRules = " + productionRules +
                "\n}";
    }

    public void RemoveSymbol(String symbol){
        Iterator<Map.Entry<String, List<String>>> it = productionRules.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            if(entry.getKey().equals(symbol)) {
                it.remove();
                continue;
            }
            for (int i = 0; i < entry.getValue().size(); i++) {
                //if contains symbol
                if (entry.getValue().get(i).contains(symbol)) {
                    entry.getValue().remove(i);
                }
            }
        }
    }
}
