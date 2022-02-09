import java.util.*;

public class Main {
    private static final Map<Character, ArrayList<String>> rules;
    private static String inputString;
    static{
        rules = new HashMap<>();
        rules.put('S', new ArrayList<>(Arrays.asList("aS", "bB")));
        rules.put('B', new ArrayList<>(Arrays.asList("cB", "d", "aD")));
        rules.put('D', new ArrayList<>(Arrays.asList("aB", "b")));
    }

    public static void main(String[] args) {
        String answer1 = "S";

        //Get the string which should be verified
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter string:");
        inputString = scanner.nextLine();
        if(!check(answer1))
            System.out.println("String can't be made. It is not valid.");
    }
    public static boolean check(String answer1){
        //Stop entirely if found
        if(answer1.equals(inputString)){
            System.out.println("String found. The answer is valid.");
            return true;
        }
        //Stop path if too big
        if(answer1.length()>inputString.length()) {
            return false;
        }
        for(int i=0; i<answer1.length(); i++){
            //If we have nonterminal symbols
            if(Character.isUpperCase(answer1.charAt(i)))
            {
                for(Map.Entry<Character, ArrayList<String>> rule : rules.entrySet()){
                    //Find the corresponding nonterminal symbol
                    if (answer1.charAt(i) == rule.getKey()) {
                        //Choose one derivation path
                        for (String derivation : rule.getValue()) {
                            StringBuilder temp = new StringBuilder(answer1);
                            temp.replace(i, i + 1, derivation);
                            if (check(temp.toString()))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
