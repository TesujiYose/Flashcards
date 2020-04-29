package flashcards;

import java.util.*;


abstract class Game {

    public abstract void addCard();

    public abstract void removeCard();

    public abstract void importCard();

    public abstract void exportCard();

    public abstract void askCard();

    public abstract void exit();

    public abstract void playGame();

}

class Flashcard extends Game {

    String action = null;
    final Scanner sc = new Scanner(System.in);

    Map<String, String> termDefinition = new LinkedHashMap<>();
    String term;
    String definition;
    //String [] answer;
    ArrayList<String> answer = new ArrayList<>();

    public void playGame() {

        do {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            action = sc.nextLine().toLowerCase();
            switch (action) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importCard();
                    break;
                case "export":
                    exportCard();
                    break;
                case "ask":
                    askCard();
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    System.out.println("Wrong action!");
            }
        } while (!(action.equals("exit")));

    }



    @Override
    public void addCard() {
        System.out.print("The card:\n");
        do {
            term = sc.nextLine();
            if (termDefinition.containsKey(term)) {
                System.out.println(String.format("The card \"%s\" already exists. Try again:", term));
            }
        } while ((termDefinition.containsKey(term)));

        System.out.print("The definition of the card:\n");
        do {
            definition = sc.nextLine();
            if (termDefinition.containsValue(definition)) {
                System.out.println(String.format("The definition \"%s\" already exists. Try again:", definition));
            }
        } while ((termDefinition.containsValue(definition)));

        termDefinition.put(term, definition);
    }

    @Override
    public void removeCard() {
        do {
            System.out.print("The card:\n");
            term = sc.nextLine();
            if (termDefinition.containsKey(term)) {
                System.out.println("The card has been removed.");
                termDefinition.remove(term);
                break;
            } else {
                System.out.printf("Can't remove \"%s\": there is no such card.", term);
            }
        } while (true);
    }

    @Override
    public void importCard() {

    }

    @Override
    public void exportCard() {

    }

    @Override
    public void askCard() {
        System.out.println("How many times to ask?");
        int num = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < num; i++) {
            Random random = new Random();
            term = termDefinition.keySet().toArray()[random.nextInt(termDefinition.keySet().size())].toString();
            System.out.printf("Print the definition of \"%s\":\n", term);
            definition = sc.nextLine();
            answer.add(definition);
            System.out.println(checkAnswer(definition, term));
        }
    }

    @Override
    public void exit() {
        System.out.println("Bye bye!");
    }

    private String getTermVal(String s) {

        for (var entry : termDefinition.entrySet()) {
            if (entry.getValue().equals(s)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String checkAnswer(String userDefinition, String term) {
        String res = "";
        if (userDefinition.equals(termDefinition.get(term))) {
            res = "Correct answer.";
        } else {
            if (termDefinition.containsValue(userDefinition)) {
                res = (String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".",
                        termDefinition.get(term),
                        getTermVal(userDefinition)));
            } else {
                res = String.format("Wrong answer. The correct one is \"%s\".", termDefinition.get(term));
            }

        }
        return res;
    }
}

public class Main {
    public static void main(String[] args) {

        Flashcard game = new Flashcard();
        game.playGame();

    }
}
