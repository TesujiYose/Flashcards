package flashcards;

import java.util.*;

abstract class Game {

    public void gameProcess() {
        inputAction();
        inputCards();
        playGame();
    }

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
    String  term;
    String  definition;
    //String [] answer;
    ArrayList<String> answer = new ArrayList<>();

    public void inputAction() {
        System.out.println("Input the action (add, remove, import, export, ask, exit):");
        String line = sc.nextLine();
        line = line.toLowerCase();
        action = line;

        switch (action) {
            case "add":
                break;
            case "remove":
                break;
            case "import":
                break;
            case "export":
                break;
            case "ask":
                break;
            case "exit":
                break;
            default:
                System.out.println("Wrong action!");
        }

    }

    public Flashcard() {


    }


    @Override
    public void inputCards() {
        for (int i = 0; i < numCards; i++) {

            System.out.printf("The card #%d:\n", i+1);
            do {
                term = sc.nextLine();
                if (termDefinition.containsKey(term)) {
                    System.out.println(String.format("The card \"%s\" already exists. Try again:", term));
                }
            } while ((termDefinition.containsKey(term)));

            System.out.printf("The definition of the card #%d:\n", i+1);
            do {
                definition = sc.nextLine();
                if (termDefinition.containsValue(definition)) {
                    System.out.println(String.format("The definition \"%s\" already exists. Try again:", definition));
                }
            } while ((termDefinition.containsValue(definition)));

            termDefinition.put(term,definition);
        }

    }

    @Override
    public void addCard() {

    }

    @Override
    public void removeCard() {

    }

    @Override
    public void importCard() {

    }

    @Override
    public void exportCard() {

    }

    @Override
    public void askCard() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void playGame() {
        int i = 0;
        for (String term : termDefinition.keySet()) {
            //iterate over all keys (terms) and check answer
            System.out.printf("Print the definition of \"%s\":\n", term);
            answer.add(sc.nextLine());
            System.out.println(checkAnswer(i,term));
            i++;
        }
    }

    private String getTermVal(String s) {

        for (var entry : termDefinition.entrySet()) {
            if (entry.getValue().equals(s)) {
                return entry.getKey();
            }

        }

        return null;
    }

    private String checkAnswer(int i, String term) {
        String res = "";
        if (answer.get(i).equals(termDefinition.get(term))) {
            res = "Correct answer.";
        } else {
            if (termDefinition.containsValue(answer.get(i))) {
                res = (String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".",
                        termDefinition.get(term),
                        getTermVal(answer.get(i))));
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
        game.gameProcess();

    }
}
