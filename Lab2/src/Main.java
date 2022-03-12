import java.io.IOException;
import java.util.*;

public class Main {
    static List<String> newElements = new ArrayList<>();

    static FiniteAutomaton nfa = new FiniteAutomaton();
    static FiniteAutomaton dfa = new FiniteAutomaton();
    static int step = 1;

    public static void main(String[] args) throws IOException{
        String inputPath = "src/input.txt";
        nfa.readInput(inputPath);

        dfa.states = new String[1];
        dfa.states[0] = nfa.states[0];
        dfa.transitionVariables = nfa.transitionVariables;
        dfa.transitionTable = new String[5][dfa.transitionVariables.length];

        nfaToDfa();
        System.out.print("NFA transition table:");
        createTable(nfa);
        System.out.print("DFA transition table:");
        createTable(dfa);


        String stringToCheck = "abacaacab";
        System.out.println("\nChecking string: "+stringToCheck);
        dfa.checkString(stringToCheck);
    }



    public static void nfaToDfa(){
        System.out.println(step++ + ". DFA states:" + Arrays.toString(dfa.states) + "\n");

        addInputCombination();

        copyNewStates(transitionsForNew());

        System.out.println(step++ +". DFA states:" + Arrays.toString(dfa.states) + "\nDFA transitions:" +
                Arrays.deepToString(dfa.transitionTable) + "\nDFA finalState:" + dfa.finalState+"\n");

        setFinal();

        System.out.println(step++ +". DFA states:" + Arrays.toString(dfa.states) + "\nDFA transitions:" +
                Arrays.deepToString(dfa.transitionTable) + "\nDFA finalState:" + dfa.finalState+"\n");
    }

    public static void addInputCombination(){
        //Add the new states combined from the input
        for(int i=0; i<nfa.states.length; i++){
            for (int j=0; j<dfa.transitionVariables.length; j++){
                if (!Arrays.asList(dfa.states).contains(nfa.transitionTable[i][j]) && !nfa.transitionTable[i][j].equals("")){
                    newElements.add(nfa.transitionTable[i][j]);
                    String[] temp = dfa.states;
                    dfa.states = new String[temp.length+1];
                    System.arraycopy(temp, 0, dfa.states, 0, temp.length);
                    dfa.states[temp.length] = nfa.transitionTable[i][j].replaceAll("\\s+","");
                }
                if(Arrays.asList(dfa.states).contains(Arrays.asList(nfa.states).get(i))){
                    dfa.transitionTable[i][j] = nfa.transitionTable[i][j].replaceAll("\\s+","");
                }
            }
            if(step!=4)
                System.out.println(step++ +". DFA states:" + Arrays.toString(dfa.states) +
                        " DFA transitions: " + Arrays.deepToString(dfa.transitionTable));
        }
    }

    public static String[][] transitionsForNew(){
        int size = newElements.size();
        String[][] tempTransitions = new String[10][dfa.transitionVariables.length];
        //Determining the transitions for each new state and adding a new state in newElements if found a new one
        for(int i=0;i<size;i++){
            String[] newTransitions = findStates(newElements.get(i), nfa.states, nfa.transitionVariables);
            for(String nt:newTransitions){
                if (isNew(nt)){
                    newElements.add(nt);
                    String[] temp = dfa.states;
                    dfa.states = new String[temp.length+1];
                    System.arraycopy(temp, 0, dfa.states, 0, temp.length);
                    dfa.states[temp.length] = nt.replaceAll("\\s+","");
                    size++;
                }
            }
            //The transitions from newTransitions[] for new state are added to temporary transition table
            System.arraycopy(newTransitions, 0, tempTransitions[i], 0, nfa.transitionVariables.length);
        }
        return tempTransitions;
    }

    public static void copyNewStates(String[][] tempTransitions){
        //Copying the new states' transitions to dfa transition table
        int skip = 0, k = 0;
        for(int i=0; i<dfa.states.length; i++){
            for(int j=0; j<dfa.transitionVariables.length; j++) {
                if (dfa.transitionTable[i][j] != null){
                    skip = 1;
                    break;
                }
            }
            if(skip==0)
                k++;
            else skip=0;
        }
        skip=0;
        for(int i=0; i<dfa.states.length; i++){
            for(int j=0; j<dfa.transitionVariables.length; j++) {
                if (tempTransitions[i][j] == null){
                    skip = 3;
                    break;
                }
                if(tempTransitions[i][j].equals("")){
                    skip++;
                }
            }
            if(skip!=3){
                for(int j=0; j<dfa.transitionVariables.length; j++) {
                    dfa.transitionTable[i+k][j] = tempTransitions[i][j].replaceAll("\\s+","");
                }
                System.out.println(step++ +". DFA states:" + Arrays.toString(dfa.states) +
                        " DFA transitions: " + Arrays.deepToString(dfa.transitionTable));
            }
            else {skip = 0; k--;}
        }
        String[][] temp = dfa.transitionTable;
        dfa.transitionTable = new String[dfa.states.length][dfa.transitionVariables.length];
        System.arraycopy(temp, 0, dfa.transitionTable, 0, dfa.transitionTable.length);
    }

    public static void setFinal(){
        //Setting the final states for DFA
        for (String el:newElements){
            if(el.contains(nfa.finalState)){
                newElements.set(newElements.indexOf(el), "*".concat(el));
                dfa.finalState += el.replaceAll("\\s+","");
            }
        }
    }

    //Method for computing the transitions for a state
    public static String[] findStates(String newState, String[] states, String[] trans) {
        String[] statesWithoutSpace = newState.split("\\s+");
        String[] eachTransition = new String[trans.length];
        Arrays.fill(eachTransition, "");

        //If newState is not a combination of states
        if (statesWithoutSpace.length == 1) {
            String[] res = new String[nfa.transitionVariables.length];
            Arrays.fill(res, "");
            return res; //return empty array
        }

        //Transitions for the states from newState combined
        for (String singleState : statesWithoutSpace) {
            int x = Arrays.asList(states).indexOf(singleState);
            for (int i = 0; i < eachTransition.length; i++) {
                eachTransition[i] += nfa.transitionTable[x][i] + " ";
            }
        }

        for (int i = 0; i < eachTransition.length; i++) {
            //If single state
            if (eachTransition[i].trim().length() == 2) {
                eachTransition[i] = eachTransition[i].trim();
            }
            //If combined state
            String[] divided = eachTransition[i].split("\\s+");
            divided = Arrays.stream(divided).distinct().toArray(String[]::new);
            eachTransition[i] = String.join(" ", divided);
        }
        return eachTransition;
    }

    public static boolean isNew(String element){
        return !Arrays.asList(nfa.states).contains(element) && !newElements.contains(element) && !element.equals("");
    }

    public static void createTable(FiniteAutomaton fa){
        System.out.println();
        for(int i=0; i<fa.transitionVariables.length; i++){
            System.out.print("\t\t"+fa.transitionVariables[i]);
        }
        System.out.println();
        OUTER: for(int i=0; i<fa.states.length; i++){
            if(i==0)
                System.out.print("->");
            System.out.print(Arrays.asList(fa.states).get(i));
            if(Arrays.asList(fa.states).get(i).equals(fa.finalState))
                System.out.print("*");
            if(fa.equals(nfa) && i==fa.states.length-1)
                System.out.print("\t");
            System.out.print("\t");
            if(i == 1)
                System.out.print("\t");
            for(int j=0; j<fa.transitionVariables.length; j++){
                if(fa.transitionTable[i][j]==null) break OUTER;
                if((((i==0 || i==1)&& j==1) || j==2) || (nfa==fa && j==1))
                    System.out.print("\t");
                if(fa.transitionTable[i][j].equals(""))
                    System.out.print("-\t");
                else
                    System.out.print(fa.transitionTable[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}