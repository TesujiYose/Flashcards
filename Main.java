package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


abstract class Game {

    public abstract void addCard();

    public abstract void removeCard();

    public abstract void importCard();

    public abstract void exportCard();

    public abstract void askCard();

    public abstract void exit();

    public abstract void log();

    public abstract void hardestCard();

    public abstract void resetStats();

    public abstract void playGame();

}


class Flashcard extends Game {

    public Flashcard(String[] args) {
        this.args = args;
        readParams();
    }

    public void printAndAddToLog(String line) {
        System.out.println(line);
        log.add(line);
    }

    public String readAndAddToLog() {
        final Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        log.add(line);
        return line;
    }

    private void readParams() {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                String importFilename = args[i + 1];
                processImportCard(importFilename);
            }
            if (args[i].equals("-export")) {
                exportFilename = args[i + 1];
                isExport = true;
            }
        }
    }

    String[] args;
    ArrayList<String> log = new ArrayList<>();
    String action = null;
    String exportFilename = "";
    boolean isExport;

    Map<String, Integer> errorCount = new LinkedHashMap<>();
    Map<String, String> termDefinition = new LinkedHashMap<>();
    String term;
    String definition;
    ArrayList<String> answer = new ArrayList<>();

    public void playGame() {
        do {
            printAndAddToLog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            action = readAndAddToLog().toLowerCase();
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
                case "log":
                    log();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                default:
                    printAndAddToLog("Wrong action!");
            }
        } while (!(action.equals("exit")));
    }

    @Override
    public void addCard() {
        printAndAddToLog("The card:");

        term = readAndAddToLog();
        if (termDefinition.containsKey(term)) {
            printAndAddToLog(String.format("The card \"%s\" already exists.", term));
        } else {
            printAndAddToLog("The definition of the card:");
            definition = readAndAddToLog();
            if (termDefinition.containsValue(definition)) {
                printAndAddToLog(String.format("The definition \"%s\" already exists.", definition));
            } else {
                errorCount.putIfAbsent(term, 0);
                termDefinition.put(term, definition);
                printAndAddToLog(String.format("The pair (\"%s\":\"%s\") has been added.", term, definition));
            }
        }
    }

    @Override
    public void removeCard() {
        printAndAddToLog("The card:");
        term = readAndAddToLog();
        if (termDefinition.containsKey(term)) {
            printAndAddToLog("The card has been removed.");
            termDefinition.remove(term);
            errorCount.remove(term);
        } else {
            printAndAddToLog(String.format("Can't remove \"%s\": there is no such card.", term));
        }
    }

    @Override
    public void importCard() {
        printAndAddToLog("File name:");
        String pathToFile = readAndAddToLog();
        processImportCard(pathToFile);
    }

    private void processImportCard(String pathToFile) {
        File file = new File(pathToFile);
        int num = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String[] line = scanner.nextLine().split(":");
                termDefinition.put(line[0], line[1]);
                if (errorCount.containsKey(line[0])) {
                    errorCount.put(line[0], Integer.parseInt(line[2]));
                } else {
                    errorCount.putIfAbsent(line[0], 0);
                }
                num++;
            }
        } catch (FileNotFoundException e) {
            printAndAddToLog("File not found.");
        }
        if (num > 0) printAndAddToLog(String.format("%d cards have been loaded.", num));
    }

    @Override
    public void exportCard() {
        printAndAddToLog("File name:");
        String pathToFile = readAndAddToLog();
        processExportCard(pathToFile);
    }

    private void processExportCard(String pathToFile) {
        File file = new File(pathToFile);
        int num = 0;
        try (FileWriter writer = new FileWriter(file)) {
            for (var entry : termDefinition.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + ":" + errorCount.get(entry.getKey()) + "\n");
                num++;
            }
        } catch (IOException e) {
            printAndAddToLog(String.format("An exception occurs %s", e.getMessage()));
        }
        printAndAddToLog(String.format("%d cards have been saved", num));
    }

    @Override
    public void askCard() {
        printAndAddToLog("How many times to ask?");
        int num = Integer.parseInt(readAndAddToLog());
        for (int i = 0; i < num; i++) {
            Random random = new Random();
            term = termDefinition.keySet().toArray()[random.nextInt(termDefinition.keySet().size())].toString();
            printAndAddToLog(String.format("Print the definition of \"%s\":", term));
            definition = readAndAddToLog();
            answer.add(definition);
            printAndAddToLog(checkAnswer(definition, term));
        }
    }

    @Override
    public void exit() {
        printAndAddToLog("Bye bye!");
        if (isExport) {
            processExportCard(exportFilename);
        }
    }

    @Override
    public void log() {
        printAndAddToLog("File name:");
        String pathToFile = readAndAddToLog();
        File file = new File(pathToFile);
        try (FileWriter writer = new FileWriter(file)) {
            for (var entry : log) {
                writer.write(entry+"\n");

            }
        } catch (IOException e) {
            printAndAddToLog(String.format("An exception occurs %s", e.getMessage()));
        }
        printAndAddToLog("The log has been saved.");
    }

    @Override
    public void hardestCard() {
        int maxError = 0;
        int count = 0;
        StringBuilder res = new StringBuilder();
        for (int entry : errorCount.values()) {
            if (entry > maxError) {
                maxError = entry;
            }
        }

        for (int entry : errorCount.values()) {
            if ((entry == maxError) && (maxError != 0)) count++;
        }

        if (count < 1) {
            printAndAddToLog("There are no cards with errors.");
        } else if (count == 1) {
            for (var entry : errorCount.entrySet()) {
                if (entry.getValue() == maxError)
                    term = entry.getKey();
            }
            printAndAddToLog(String.format("The hardest card is \"%s\". You have %d errors answering it.", term , maxError));
        }
        else {

            for (var entry : errorCount.entrySet()) {
                if (entry.getValue() == maxError) {
                    term = entry.getKey();
                    res.append(String.format("\"%s\", ", term));
                }
            }
            res.delete(res.length() - 2,res.length());
            printAndAddToLog(String.format("The hardest cards are %s. You have %d errors answering them.", res.toString(), maxError));
        }
    }

    @Override
    public void resetStats() {
        errorCount.replaceAll((e, v) -> 0);
        printAndAddToLog("Card statistics has been reset.");
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
        String res;
        if (userDefinition.equals(termDefinition.get(term))) {
            res = "Correct answer.";
        } else {
            errorCount.put(term, errorCount.get(term) + 1);
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
        Flashcard game = new Flashcard(args);
        game.playGame();
    }
}
