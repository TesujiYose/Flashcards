package flashcards;

import java.util.*;

abstract class Game {

    public void gameProcess() {
        inputCards();
        playGame();
    }

    public abstract void inputCards();
    public abstract void playGame();

}

class Flashcard extends Game {

    final Scanner sc = new Scanner(System.in);
    int numCards;
    //could be error, init with null
    Map<String, String> termDefinition = new LinkedHashMap<>();
    String  term;
    String  definition;
    String [] answer;

    public Flashcard() {
        System.out.println("Input the number of cards:");
        this.numCards = Integer.parseInt(sc.nextLine());
        answer = new String[numCards];
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
    public void playGame() {
        int i = 0;
        for (String term : termDefinition.keySet()) {
            //iterate over all keys (terms) and check answer
            System.out.printf("Print the definition of \"%s\":\n", term);
            answer[i] = sc.nextLine();
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
        if (answer[i].equals(termDefinition.get(term))) {
            res = "Correct answer.";
        } else {
            if (termDefinition.containsValue(answer[i])) {
                res = (String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".",
                        termDefinition.get(term),
                        getTermVal(answer[i])));
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
