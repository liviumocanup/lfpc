import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class FiniteAutomaton{
    public String[] states;
    public String[] transitionVariables;
    public String[][] transitionTable;
    public String finalState = "";

    public void readInput(String inputPath) throws FileNotFoundException {
        File file = new File(inputPath);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()){
            String inputString = scanner.nextLine();
            String[] inputStringDelimited = inputString.split(":");
            switch (inputStringDelimited[0]){
                case "Q":
                    states = inputStringDelimited[1].split(",");
                    for(int i=0; i<states.length; i++)
                        states[i] = states[i].trim();
                    break;
                case "E":
                    transitionVariables = inputStringDelimited[1].split(",");
                    for(int i=0; i<transitionVariables.length; i++)
                        transitionVariables[i] = transitionVariables[i].trim();
                    break;
                case "F":
                    String[] symbolsF = inputStringDelimited[1].split(",");
                    finalState = symbolsF[0].trim();
                    break;
                case "D":
                    transitionTable = new String[states.length][transitionVariables.length];
                    Arrays.stream(transitionTable).forEach(x->Arrays.fill(x, ""));
                    break;
                default:
                    String[] symbolsD = inputStringDelimited[0].split(",");
                    if(symbolsD[0].trim().equals("}"))
                        break;
                    String state1 = symbolsD[0].trim();
                    String transitionState = symbolsD[1].trim();
                    String state2 = symbolsD[2].trim();

                    int state = Arrays.asList(states).indexOf(state1);
                    int trans = Arrays.asList(transitionVariables).indexOf(transitionState);
                    if(transitionTable[state][trans].equals("")){
                        transitionTable[state][trans] = transitionTable[state][trans].concat(state2);
                    }else{
                        transitionTable[state][trans] = transitionTable[state][trans].concat(" " + state2);
                    }
                    break;
            }
        }
    }

    public void checkString(String check){
        boolean canBeDone = true;
        int currentState = 0, charColumn;
        StringBuilder newString = new StringBuilder();

        //Starting from q0
        newString.append(states[currentState]);

        for(int i=0; i<check.length(); i++){
            charColumn = Arrays.asList(transitionVariables).indexOf(Character.toString(check.charAt(i)));

            if(charColumn>=0 && Arrays.asList(states).contains(transitionTable[currentState][charColumn])){
                currentState = Arrays.asList(states).indexOf(transitionTable[currentState][charColumn]);
                newString.append("->").append(states[currentState]);
            }
            else {
                canBeDone = false;
                break;
            }
        }
        if(canBeDone && states[currentState].equals(finalState))
            System.out.println("String can be done by path: "+newString);
        else if(canBeDone && !states[currentState].equals(finalState))
            System.out.println("String does not end in final state. "+newString);
        else System.out.println("String cannot be done. "+newString);
    }

    @Override
    public String toString() {
        return "states=" + Arrays.toString(states) +
                "\ntransitionVariables=" + Arrays.toString(transitionVariables) +
                "\ntransitionTable=" + Arrays.deepToString(transitionTable) +
                "\nfinalState=" + finalState;
    }
}