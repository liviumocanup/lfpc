package lab4.steps;

import lab4.Grammar;

import java.util.*;

public class Step5 {
    public static void ChomskyNormalForm(Grammar grammar) {
        HashMap<String, String> terminalX = new HashMap<>();
        HashMap<String, String> nonTerminalY = new HashMap<>();

        //add terminals to terminalX
        for (String s : grammar.terminalSet) {
            addX(s, terminalX);
        }

        //replace terminals with nonterminals if it doesn't derive directly in terminal
        replaceTerminals(grammar, terminalX);

        //replace combinations of 2 nonterminals with one nonterminal until the rule is normalized
        normalizeRule(grammar, nonTerminalY);

        //add the symbols to the nonTerminalSet
        grammar.nonTerminalSet.addAll(terminalX.keySet());
        grammar.nonTerminalSet.addAll(nonTerminalY.keySet());

        //add the new rules to the productionRules
        addToProductionRules(grammar, terminalX);
        addToProductionRules(grammar, nonTerminalY);
    }

    public static void addToProductionRules(Grammar grammar, HashMap<String, String> hashMap){
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            List<String> temp = new ArrayList<>();
            temp.add(entry.getValue());
            grammar.productionRules.put(entry.getKey(), temp);
        }
    }

    public static void replaceTerminals(Grammar grammar, HashMap<String, String> terminalX){
        for (Map.Entry<String, List<String>> entry : grammar.productionRules.entrySet()) {
            //iterate over the derivation list of a symbol
            for (int i = 0; i < entry.getValue().size(); i++) {
                String currString = entry.getValue().get(i);
                //if it doesn't derive only in terminal
                if (currString.length() > 1) {
                    //verify each character in String
                    for (int j = 0; j < currString.length(); j++) {
                        String currChar = charToStringAtIndex(currString, j);
                        //change terminal to nonterminal
                        if (terminalX.containsValue(currChar)) {
                            String newDerivation = getKeyByValue(terminalX, currChar);
                            if (newDerivation != null) {
                                String temp = entry.getValue().get(i).replace(currChar, newDerivation);
                                entry.getValue().set(i, temp);
                                currString = entry.getValue().get(i);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void normalizeRule(Grammar grammar, HashMap<String, String> nonTerminalY){
        for (Map.Entry<String, List<String>> entry : grammar.productionRules.entrySet()) {

            //iterate over the derivation list of a symbol
            for (int i = 0; i < entry.getValue().size(); i++) {
                String currString = entry.getValue().get(i);

                //while a rule is not normalized
                while(isNotNormalized(currString)) {

                    //change 2 nonterminals to the nonterminal found in the HashMap (if there is such)
                    checkExistenceForNonTerminal(currString, nonTerminalY, entry, i);

                    currString = entry.getValue().get(i);
                    for (int j = currString.length()-1; j > 0; j--) {

                        //if it's still not normalized after replacing it with the symbols that we already have
                        if(isNotNormalized(currString)) {

                            //we select a combination of 2 nonterminals
                            String[] tempChars = selectChars(currString, j);
                            String currChar1 = tempChars[0];
                            String currChar2 = tempChars[1];

                            //and check whether we already have a symbol that derives into these two
                            String currCombination = currChar1 + currChar2;
                            String newDerivation = getKeyByValue(nonTerminalY, currCombination);
                            String replacement;

                            if (newDerivation != null) {
                                replacement = entry.getValue().get(i).replace(currCombination, newDerivation);
                            } else {
                                //if we don't, we add it
                                addY(currChar1 + currChar2, nonTerminalY);
                                replacement = entry.getValue().get(i).replace(currCombination, "Y" + nonTerminalY.size());
                            }
                            //replace the value in the rule
                            entry.getValue().set(i, replacement);

                            currString = entry.getValue().get(i);
                        }
                    }

                    currString = entry.getValue().get(i);
                }
            }
        }
    }

    public static void addX(String currCharacter, HashMap<String, String> terminalX){
        if(!terminalX.containsValue(currCharacter)) {
            terminalX.put("X" + (terminalX.size() + 1), currCharacter);
        }
    }

    public static void addY(String currCharacter, HashMap<String, String> nonTerminalY){
        if(!nonTerminalY.containsValue(currCharacter)) {
            nonTerminalY.put("Y" + (nonTerminalY.size() + 1), currCharacter);
        }
    }

    public static boolean isNotNormalized(String s){
        if(s.length()==1 || (s.length()==2 && nrOfDigits(s)==0) || (s.length()==3 && nrOfDigits(s)==1))
            return false;
        return s.length() != 4 || nrOfDigits(s) != 2;
    }

    public static int nrOfDigits(String s){
        int count = 0;
        for(int i=0; i<s.length(); i++){
            if(Character.isDigit(s.charAt(i))){
                count++;
            }
        }
        return count;
    }

    public static String getKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String charToStringAtIndex(String currString, int index){
        return Character.toString(currString.charAt(index));
    }

    public static void checkExistenceForNonTerminal(String currString, Map<String, String> nonTerminalY, Map.Entry<String, List<String>> entry, int index){
        for (int j = currString.length()-1; j > 0; j--) {
            if(isNotNormalized(currString)) {
                String[] tempChars = selectChars(currString, j);
                String currChar1 = tempChars[0];
                String currChar2 = tempChars[1];

                String newDerivation = getKeyByValue(nonTerminalY, currChar1 + currChar2);
                if (newDerivation != null) {
                    String temp = entry.getValue().get(index).replace(currChar1 + currChar2, newDerivation);
                    entry.getValue().set(index, temp);
                    currString = entry.getValue().get(index);
                }
            }else break;
        }
    }

    public static String[] selectChars(String currString, int j){
        //String manipulations to get the right characters
        //ex for AX1 : A and X1 (X1 is a string of 2 characters)
        //ex for X1A : X1 and A
        //ex for AB : A and B
        //ex for X1X2 : X1 and X2 (X1X2 is a string of 4 characters but 2 symbols)

        String currChar1, currChar2;
        boolean nextCharIsDigit = false, previousCharIsDigit = false;
        boolean currentCharIsDigit = Character.isDigit(currString.charAt(j));
        if (j + 1 < currString.length()) {
            nextCharIsDigit = Character.isDigit(currString.charAt(j + 1));
        }
        if (j - 1 >= 0) {
            previousCharIsDigit = Character.isDigit(currString.charAt(j-1));
        }

        if(previousCharIsDigit)
        {
            currChar1 = currString.substring(j-2,j);
            currChar2 = setNextChar(currString, nextCharIsDigit, j);
        }
        else if(currentCharIsDigit)
        {
            if(j-1==0) {
                currChar2 = currString.substring(0, j+1);
                if(j+2<currString.length()) {
                    if (Character.isDigit(currString.charAt(j + 2)))
                        currChar1 = currString.substring(j, j + 2);
                    else currChar1 = charToStringAtIndex(currString, j + 1);
                }else currChar1 = charToStringAtIndex(currString, j + 1);
            }
            else{
                currChar2 = currString.substring(j-1, j+1);
                if (Character.isDigit(currString.charAt(j - 2)))
                    currChar1 = currString.substring(j - 3, j - 1);
                else currChar1 = charToStringAtIndex(currString, j - 2);
            }
        }
        else
        {
            currChar1 = charToStringAtIndex(currString, j - 1);
            currChar2 = setNextChar(currString, nextCharIsDigit, j);
        }

        String[] result = new String[2];
        result[0] = currChar1;
        result[1] = currChar2;
        return result;
    }

    public static String setNextChar(String currString, boolean condition, int j){
        if(j+1<currString.length()) {
            if (condition) {
                return currString.substring(j, j + 2);
            }
        }
        return charToStringAtIndex(currString, j);
    }
}