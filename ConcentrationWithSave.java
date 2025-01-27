/*
 * Class: ConcentrationWithSave (extends Concentration)
 * Programmer: Ahmad Bajwa
 * Description: This class extends the Concentration class to write the game object to a file.
 * Its constructor takes rows and cols as parameters that are passed onto the super constructor of
 * the abstract class Concentration.
 */
package com.ICS4U;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

//declare a new class ConcentrationWithSave which extends the Concentration class.
public class ConcentrationWithSave extends Concentration{
    public ConcentrationWithSave(int rows, int cols) {
        //call constructor of Concentration to create an instance of the class.
        super(rows, cols);
    }

    /*
     * writeGameToFile
     * This method is used to create a Concentration.txt file, and uses fileOutputStream and ObjectOutputStream to
     * Write the game object to the file
     */
    public void writeGameToFile(String fileName) throws IOException {
        //create File reference
        File cardsArrayFile = new File(fileName);
        //Open FileOutputStream to the file, and pass it as an argument to an ObjectOutputStream object to write data
        FileOutputStream output = new FileOutputStream(cardsArrayFile);
        ObjectOutputStream objectOutput = new ObjectOutputStream(output);
        //write the game object to the file, then flush the buffer and close the stream.
        objectOutput.writeObject(this);
        objectOutput.flush();
        objectOutput.close();
    }
    /*
     * updateBestScoresToFile
     * This method is used to create a ConcentrationBestScores.txt file, and uses Scanner and FileWriter to
     * read best scores from file if it exists and updates if the current scores are better than the best
     * (poetic, isn't it?)
     */
    public void updateBestScoresToFile(int noOfFlips, String mode) throws IOException {
        // Create file reference for best scores
        File bestScoresFile = new File ("ConcentrationBestScores.txt");
        // Best scores for the 3 game modes. 0 indicates the initial state
        int easyBest = 0;
        int mediumBest = 0;
        int hardBest = 0;
        if(bestScoresFile.exists()) {
            // Read best scores for the 3 modes and update respective variables. Scores in file are saved per line for
            // the three difficulty modes in the format "Difficulty: #"
            try {
                Scanner flipsScanner = new Scanner(bestScoresFile);
                String easy = flipsScanner.nextLine();
                String medium = flipsScanner.nextLine();
                String hard = flipsScanner.nextLine();
                easyBest = Integer.parseInt(easy.split(" ")[1]);
                mediumBest = Integer.parseInt(medium.split(" ")[1]);
                hardBest = Integer.parseInt(hard.split(" ")[1]);
                flipsScanner.close();
            } catch (NoSuchElementException | IllegalStateException | NumberFormatException | IndexOutOfBoundsException
                    e) {
                // The best scores file has been tempered with show a message, delete the file and reset scores
                System.out.println("Incorrect format of score file. Resetting best scores");
                try{
                    bestScoresFile.delete();
                } catch (Exception ex) {
                    System.out.println("Failed to delete best scores file");
                }
                easyBest = 0;
                mediumBest = 0;
                hardBest = 0;
            }
        }
        // Update the variable for appropriate mode
        if(Objects.equals(mode, "easy")) {
            if(easyBest == 0) {
                easyBest = noOfFlips;
            } else {
                easyBest = Math.min(easyBest, noOfFlips);
            }
        } else if(Objects.equals(mode, "medium")) {
            if(mediumBest == 0) {
                mediumBest = noOfFlips;
            } else {
                mediumBest = Math.min(mediumBest, noOfFlips);
            }
        } else {
            if(hardBest == 0) {
                hardBest = noOfFlips;
            } else {
                hardBest = Math.min(hardBest, noOfFlips);
            }
        }
        // Create FileWriter and PrintWriter objects to save best scores
        FileWriter flipsWriter = new FileWriter (bestScoresFile, false);
        PrintWriter pwFlips = new PrintWriter (flipsWriter);
        pwFlips.println ("Easy: " + easyBest);
        pwFlips.println ("Medium: " + mediumBest);
        pwFlips.println ("Hard: " + hardBest);
        // After printing close writers
        pwFlips.close();
        flipsWriter.close();
    }
}
