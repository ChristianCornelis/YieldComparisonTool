package project.helpers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Temporary class to handle input. This will be removed in the future, so it's kinda gross for the time being: )
 */
public class InputHandler extends PromptHelper {

    private Scanner inputScanner;

    /**
     * Constructor.
     */
    public InputHandler() {
        this.inputScanner = new Scanner(System.in);
    }


    /**
     * Builds a string representing the status of the maps containing yields.
     * @param producerYields producer yields map
     * @param statsCanYields  statsCan yields map
     * @return the pre-formatted string.
     */
    public String getYieldsStatus(Map producerYields, Map statsCanYields) {
        String toReturn = "\nLoaded yields status:\n\t- ";
        if (producerYields != null && producerYields.size() != 0) {
            toReturn += outputMapStatus("Producer yields", true) + "\n\t- ";
        } else {
            toReturn += outputMapStatus("Producer yields", false) + "\n\t- ";
        }
        if (statsCanYields != null && statsCanYields.size() != 0) {
            toReturn += outputMapStatus("Statistics Canada yields", true) + "\n";
        } else {
            toReturn += outputMapStatus("Statistics Canada yields", false) + "\n";
        }

        return toReturn;
    }

    /**
     * Chooses an action from a prompt string.
     * @param promptString The string to prompt.
     * @param validInputs Valid input chouces.
     * @param errorPrompt the additional prompt to provide when an invalid input is given.
     * @return the chosen valid choice.
     */
    public int chooseAction(String promptString, ArrayList validInputs, String errorPrompt) {
        String input = "";
        int inputInt = -1;
        System.out.println(promptString);
        while (!validInputs.contains(inputInt)) {
            input = inputScanner.next();
            inputInt = convertInputAction(input);
            if (!validInputs.contains(inputInt)) {
                System.out.println(errorPrompt);
                System.out.println(promptString);
            }
        }
        return inputInt;
    }

    /**
     * Chooses an action from a prompt string.
     * @param promptString The string to prompt.
     * @param validInputs Valid input chouces.
     * @return the chosen valid choice.
     */
    public int chooseAction(String promptString, int[] validInputs) {
        String input = "";
        int inputInt = -1;
        System.out.println(promptString);
        while (!actionIsValid(validInputs, inputInt)) {
            input = inputScanner.next();
            inputInt = convertInputAction(input);

            if (!actionIsValid(validInputs, inputInt)) {
                System.out.println(promptString);
            }
        }
        return inputInt;
    }

    /**
     * Wrapper on scanner.next() for getting super basic input.
     * @return the input.
     */
    public String getBasicInput() {
        return inputScanner.next();
    }

    /**
     * Checks if an action is valid.
     * @param validActions array of valid actions
     * @param action action to check
     * @return true or false
     */
    private Boolean actionIsValid(int[] validActions, int action) {
        for (int v : validActions) {
            if (v == action) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts an action to an integer. Handles format exceptions.
     * @param action the action to convert
     * @return the action as an integer.
     */
    private int convertInputAction(String action) {
        int inputAction = -1;
        try {
            inputAction = Integer.parseInt(action);
        } catch (NumberFormatException e) {
            inputAction = -1;
        }
        return inputAction;
    }

    /**
     * Getter for scanner.
     * @return the scanner.
     */
    public Scanner getInputScanner() {
        return inputScanner;
    }

    /**
     * Setter for scanner.
     * @param inputScanner the new input scanner.
     */
    public void setInputScanner(Scanner inputScanner) {
        this.inputScanner = inputScanner;
    }
}

