package project;

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
        if (producerYields != null) {
            toReturn += mapStatus("Producer yields", true) + "\n\t- ";
        } else {
            toReturn += mapStatus("Producer yields", false) + "\n\t- ";
        }
        if (statsCanYields != null) {
            toReturn += mapStatus("Statistics Canada yields", true) + "\n";
        } else {
            toReturn += mapStatus("Statistics Canada yields", false) + "\n";
        }

        return toReturn;
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
     * Chooses an action from a prompt string.
     * @param promptString The string to prompt.
     * @param validInputs Valid input chouces.
     * @return the chosen valid choice.
     */
    public int chooseAction(String promptString, ArrayList validInputs) {
        String input = "";
        int inputInt = -1;
        System.out.println(promptString);
        while (!validInputs.contains(inputInt)) {
            input = inputScanner.next();
            inputInt = convertInputAction(input);
            if (!validInputs.contains(inputInt)) {
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
}

