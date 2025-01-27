/*
 * Project: Concentration/ Card Memory Game
 * Programmer: Muhammad Ahmad Bajwa
 * Date: January 11, 2023
 * Program Name: Bajwa_ICS4U_Culminating -- executable is Main.java
 * Description: A terminal-based Concentration card game - my capstone.
 */
package com.ICS4U;

import java.io.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    /*
    This method is the main driver for the Concentration game. It checks for a saved game, asks the user if they'd
    like to play the saved game, and then calls upon logic methods to start a new game or play a saved game.
     */
    public static void main(String[] args) throws IOException {
        //Declare variables and objects
        String fileName = "Concentration.game";
        // Declare ConcentrationWithSave game object. Initialise the object with default parameters
        ConcentrationWithSave game = new ConcentrationWithSave(0, 0);
        boolean savedGameAvailable = false;
        String choice; // Stores user input
        Scanner s = new Scanner(System.in); // Scanner to collect user input

        System.out.println("""
                 _____                            _             _   _               _____                     \s
                /  __ \\                          | |           | | (_)             |  __ \\                    \s
                | /  \\/ ___  _ __   ___ ___ _ __ | |_ _ __ __ _| |_ _  ___  _ __   | |  \\/ __ _ _ __ ___   ___\s
                | |    / _ \\| '_ \\ / __/ _ \\ '_ \\| __| '__/ _` | __| |/ _ \\| '_ \\  | | __ / _` | '_ ` _ \\ / _ \\
                | \\__/\\ (_) | | | | (_|  __/ | | | |_| | | (_| | |_| | (_) | | | | | |_\\ \\ (_| | | | | | |  __/
                 \\____/\\___/|_| |_|\\___\\___|_| |_|\\__|_|  \\__,_|\\__|_|\\___/|_| |_|  \\____/\\__,_|_| |_| |_|\\___|
                                                                                                              \s
                                                                                                              \s""");
        // Print welcome message
        System.out.println("Welcome to the Concentration Game!");

        showBestScores();

        /*
         Try to read a previous game from file. If a game object isn't found in the file, the code block will throw an
          error and savedGameAvailable will not be set to true. A new game will therefore begin.
        */
        try {
            game = readGameFromFile(fileName);
            savedGameAvailable = true;
        } catch (FileNotFoundException | ClassNotFoundException | InvalidClassException ignored) {
        }

        if (savedGameAvailable) { // If a saved game is found, prompt the user to continue the game or restart
            System.out.println("Saved game found. Do you want to play saved game? (y/any other key)");
            choice = s.nextLine();
            if (choice.equalsIgnoreCase("y")) { // If the user says yes, play using the saved game object
                play(game, s, game.getRows(), game.getCols(), fileName); // Play the game
            } else { // Delete existing game file and start a new game
                deleteGameFile(fileName);
                startNewGame(s, fileName);
            }
        } else { // If no saved game is available, start a new game
            startNewGame(s, fileName);
        }
        System.out.println("See you soon!"); // Print concluding message if the user quits mid-game
    }

    /*
    Reads best scores file and displays scores if the file has not been tempered with. Otherwise, displays
    a file tempered message and delete best scores file.
     */
    private static void showBestScores() {
        // File containing best scores
        File scoreFile = new File("ConcentrationBestScores.txt");
        if(scoreFile.exists()) {
            // Best score file exists! Show the scores
            // Score are saved in the format, "Difficulty: #" for the three difficulty levels
            System.out.println("Best scores!");
            try {
                // Read scores from file. If the scores are 0, no games have been completed at that difficulty
                // Scores are only saved for complete games
                Scanner flipsScanner = new Scanner(scoreFile);
                String easy = flipsScanner.nextLine();
                String medium = flipsScanner.nextLine();
                String hard = flipsScanner.nextLine();
                int easyBest = Integer.parseInt(easy.split(" ")[1]);
                int mediumBest = Integer.parseInt(medium.split(" ")[1]);
                int hardBest = Integer.parseInt(hard.split(" ")[1]);
                flipsScanner.close();
                if(easyBest == 0) {
                    System.out.println("No completed games for easy difficulty.");
                } else {
                    System.out.println("Best score for Easy difficulty: " + easyBest);
                }
                if(mediumBest == 0) {
                    System.out.println("No completed games for Medium difficulty.");
                } else {
                    System.out.println("Best score for Medium difficulty: " + mediumBest);
                }
                if(hardBest == 0) {
                    System.out.println("No completed games for Hard difficulty.");
                } else {
                    System.out.println("Best score for Hard difficulty: " + hardBest);
                }
                System.out.println();
            } catch (NoSuchElementException | IllegalStateException | NumberFormatException | IndexOutOfBoundsException
                    e) {
                // The best scores file has been tampered with - show a message and delete the file
                System.out.println("Incorrect format of score file. Deleting file");
                try{
                    scoreFile.delete();
                } catch (Exception ex) {
                    System.out.println("Failed to delete best scores file");
                }
                System.out.println();
            } catch (FileNotFoundException ignored) {}
        }
    }

    /* startNewGame
     * This method starts a new Concentration game of a difficulty level chosen by the user.
     * @args s -  scanner to take user input
     * @args fileName - name of the file where the object is written/read from - "Concentration.txt"
     */
    private static void startNewGame(Scanner s, String fileName) throws IOException {
        // Declare variables and Concentration game object
        String choice;
        ConcentrationWithSave game;
        // Print a formatted table of the various difficulty levels, and ask the user to choose one.
        System.out.println("Let's choose the game difficulty. Use the following table to make your choice. E for easy, "
                + "M for medium, H for Hard. Press any other key to quit.");
        System.out.println(
                """
                        ╔════════════╦═══════════╗
                        ║ Difficulty ║ Grid Size ║
                        ╠════════════╬═══════════╣
                        ║ Easy       ║ 4x4       ║
                        ╠════════════╬═══════════╣
                        ║ Medium     ║ 6x6       ║
                        ╠════════════╬═══════════╣
                        ║ Hard       ║ 8x8       ║
                        ╚════════════╩═══════════╝""");

        choice = s.nextLine(); // Prompt the user for their choice of difficulty
            /*
             The following code evaluates the user's choice and initialises game with the number of rows and
             columns corresponding to the user's chosen difficulty. Then, the play method is executed using
             the scanner object, game object, rows, columns, and file name.
            */
        if (choice.equalsIgnoreCase("Easy") || choice.equalsIgnoreCase("e")) {
            game = new ConcentrationWithSave(4, 4);
            play(game, s, 4, 4, fileName);
        } else if (choice.equalsIgnoreCase("Medium") || choice.equalsIgnoreCase("M")) {
            game = new ConcentrationWithSave(6, 6);
            play(game, s, 6, 6, fileName);
        } else if (choice.equalsIgnoreCase("Hard") || choice.equalsIgnoreCase("H")) {
            game = new ConcentrationWithSave(8, 8);
            play(game, s, 8, 8, fileName);
        } else { // Un-recognised key is interpreted to be an exit. Therefore, program is terminated.
            System.exit(0);
        }
    }

    /* readGameFromFile
     * This method reads the stored state of a Concentration card game from Concentration.txt using an ObjectInputStream
     * and returns it as a ConcentrationWithSave object.
     * @args fileName - name of the file from which the object is read from - "Concentration.txt"
     * @return *game object* - game object read from the file, cast to ConcentrationWithSave.
     */
    public static ConcentrationWithSave readGameFromFile(String fileName) throws IOException, ClassNotFoundException {
        // Create file reference to Concentration.txt
        File concentrationFile = new File(fileName);
        // Open FileInputStream to the file, and pass it as an argument to an ObjectInputStream object to read data
        FileInputStream input = new FileInputStream(concentrationFile);
        ObjectInputStream objectInput = new ObjectInputStream(input);
        // Return stored data to the main method as a ConcentrationWithSave object
        return (ConcentrationWithSave) objectInput.readObject();
    }

    /* deleteGameFile
     * This method attempts to delete a Concentration.txt file.
     * @args fileName - name of the file from which the object is read/written from - "Concentration.txt"
     */
    public static void deleteGameFile(String fileName) {
        try { // Catch the general exception thrown during the deletion process
            // Create a File reference to Concentration.txt, and promptly delete it
            File concentrationFile = new File(fileName);
            concentrationFile.delete();
        } catch (Exception e) { //compiler appeasement; print error message
            System.out.println("File error. Could not delete saved game.");
        }
    }

    /* getUserInput
     * This method gets user input and verifies it to ensure it is in the correct format. If the input is invalid, it
     * will return an array of zeros.
     * @args s - Scanner for user input
     * @args row - the position of the card with respect to its row
     * @args col - the position of the card with respect to its column
     */
    private static int[] getUserInput(Scanner s, int rows, int cols) {
        // Returning an array of zeros for invalid input
        int[] errorResult = new int[]{0, 0};
        // Prompt the user for a response, and lay out the reply format
        System.out.println("Please enter row and col to select a particular card, e.g. " +
                "for card in row 2 and col 3 enter 23. To quit enter X");
        // Store the user's response
        String input = s.nextLine();
        if (input.equalsIgnoreCase("X")) { //terminate the program
            System.exit(0);
        }
        /*
        The remainder of the method validates the input. It checks to see if a two-digit integer with valid
        coordinates for row and column was inputted. If a validation step fails, an int array of zeros "errorResult" is
        returned. If the input passes the criteria, a converted int array "coordinates" is returned instead.
        */
        if (input.length() != 2) {
            return errorResult;
        }
        try { // If the input cannot be parsed as an integer
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return errorResult;
        }
        /*
        Note: This one-liner creates an array of Integers called "coordinates" which consists of the individual elements
        parsed from the input string. "input" is split by the empty character "" into individual elements which are
        parsed into integers, and those integers are added to the "coordinates" array.
        */
        int[] coordinates = Arrays.stream(input.split("")).mapToInt(Integer::parseInt).toArray();
        // Checks if coordinates are within bounds
        if (coordinates[0] < 1 || coordinates[0] > rows || coordinates[1] < 1 || coordinates[1] > cols) {
            return errorResult;
        }
        return coordinates; // The input is successfully parsed into coordinates, and is returned
    }

    /* play
     * This method consolidates different methods by playing the game. It prints the game, validates user input, and
     * ends the game appropriately.
     * @args game - the ConcentrationWithSave game object which the game operates on
     * @args fileName - name of the file from which the object is read from - "Concentration.txt"
     * @args row - the position of the card with respect to its row
     * @args col - the position of the card with respect to its column
     * @args s - input scanner
     */
    private static void play(ConcentrationWithSave game, Scanner s, int rows, int cols, String fileName)
            throws IOException {
        int[] coordinates; // Declare coordinates array to store user-typed coordinates
        while (true) { // Game loop
            game.printGame();  // Print updated game at the start of each loop
            if (game.checkGameOver()) { // All cards have been matched, so print goodbye message and terminate
                deleteGameFile(fileName); /* Delete the game file so the user isn't prompted to re-open it next
                execution */
                // In case of a finished game update best scores file
                if(rows == 4) {
                    game.updateBestScoresToFile(game.noOfFlips, "easy");
                } else if(rows == 6) {
                    game.updateBestScoresToFile(game.noOfFlips, "medium");
                } else {
                    game.updateBestScoresToFile(game.noOfFlips, "hard");
                }
                System.out.println("Thank you for playing! Do come back when you just can't concentrate!");
                System.exit(0);
            }
            do { // The game isn't over, and user has typed incorrect coordinates
                coordinates = getUserInput(s, rows, cols); // Receive and store user-selected coordinates
                /* If the first index is 0, then an invalid coordinate was received.
                     Re-prompt */

                if (coordinates[0] == 0) { System.out.println("Please enter valid input as [row][col] e.g. 23");
                }
            } while (coordinates[0] == 0);
            // The following code executes when the user inputs valid coordinates
            game.chooseCard(coordinates[0], coordinates[1]); // Flip, and if possible, match the card selected by user
            clearConsole(); // Simulate a framerate and keep console tidy by clearing console
            game.writeGameToFile(fileName); // Save game after each chosen card
        }
    }

    /* clearConsole
     * Clears the user's console. Checks operating system and clears accordingly.
     */
    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name"); // Gets the Operating system
            if (os.contains("Windows")) {
                //stackoverflow.com/questions/2979383/how-to-clear-the-console
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else { // Run the following commands on Unix-based Operating systems to clear console
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Handle any exceptions
            System.out.println("Could not clear console");
        }
    }
}