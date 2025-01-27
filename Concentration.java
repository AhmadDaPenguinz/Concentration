package com.ICS4U;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
 * Class: Concentration
 * Programmer: Ahmad Bajwa
 * Description: Holds data and methods concerning data for the Concentration game, field manipulation of
 * Card objects, and a flip count manager.
 * */
public class Concentration implements Serializable {
    // Symbols taken from https://www.fileformat.info/info/unicode/font/consolas/grid.htm
    String[] availableSymbols = {"#", "$", "%", "&", "@", "£", "¥", "§", "©", "♫",
            "«", "»", "µ", "±", "Ø", "π", "Ω", "θ", "ε", "β", "δ", "η", "ψ", "Ϣ", "╬",
            "Ϫ", "Є", "₨", "₴", "⅓", "√", "∞", "∑", "∫", "∂", "↑", "↓", "↔", "↕", "♠", "♣", "♥", "♦"};

    // Declare and/or initialise variables
    // Store availableSymbols array as an arraylist to subtract symbols within the Concentration constructor
    ArrayList<String> symbolsArrayList = new ArrayList<>(Arrays.asList(availableSymbols));
    Card[] cards; //card array of card objects
    public int indexOfOneAndOnlyFaceUpCard = -1; // Initialised such that no card is faced up

    int noOfFlips = 0;
    int rows;
    int cols;
    String flipsText = "";

    // This constructor assigns random symbols from an ArrayList to cards, and then assigns matching copies of those
    // cards to other positions in an array.
    public Concentration(int rows, int cols) {
        // Assign instance variables to their parameter values.
        this.rows = rows;
        this.cols = cols;
        int noOfPairs = (rows * cols) / 2; // Computes the number of matching symbol pairs.
        // Creates an ArrayList of positions to store valid indices in the cards array.
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < noOfPairs * 2; i++) { // Fills the positions ArrayList with symbols from 0 to the
            // size of the board - 1
            positions.add(i);
        }
        // Initialize card array
        this.cards = new Card[noOfPairs * 2];
        for (int i = 0; i < noOfPairs; i++) {
            // Generate random indexes using a time as seed:
            // https://stackoverflow.com/questions/12458383/java-random-numbers-using-a-seed
            Random randomSymbol = new Random(System.currentTimeMillis());
            int randomSymbolIndex = randomSymbol.nextInt(symbolsArrayList.size()); // Fetch index of a random symbol
            // Create two pairs of card objects with the symbol at index of symbols arrayList
            Card card = new Card(symbolsArrayList.get(randomSymbolIndex));
            Card matchingCard = new Card(symbolsArrayList.get(randomSymbolIndex));
            symbolsArrayList.remove(randomSymbolIndex); //Remove symbol from list so that the same symbol is not chosen
            // multiple times
            Random randomPosition = new Random(System.currentTimeMillis());
            int randomCardIndex = randomPosition.nextInt(positions.size()); // Fetch index of a random board coordinate
            cards[positions.get(randomCardIndex)] = card; // Assign the card to the generated coordinate in cards[]
            positions.remove(randomCardIndex); // Remove position from list so that the same position is not chosen
            // twice || in other iterations
            randomPosition = new Random(System.currentTimeMillis()); // Re-generate random position
            randomCardIndex = randomPosition.nextInt(positions.size()); // Fetch another index of a random board coordinate
            cards[positions.get(randomCardIndex)] = matchingCard; // Assign matching card to re-generated coordinate in
            // cards[]
            positions.remove(randomCardIndex); // Remove position from list so that the same position is not chosen twice
            // || in other iterations
        }
    }

    // Used to return the number of rows
    public int getRows() {
        return rows;
    }

    // Used to return the number of columns
    public int getCols() {
        return cols;
    }

    // Used to return the noOfFlips variable
    /* printGame
     * This method prints a game of the specified size and updates the board each time a card is clicked.
     */
    public void printGame() {
        // The following code prints the x-coordinates of the top row according to the size specified by the user.
        if (this.rows == 4 && this.cols == 4) {
            System.out.println("    1   2   3   4");
            System.out.println();
        } else if (this.rows == 6 && this.cols == 6) {
            System.out.println("    1   2   3   4   5   6");
            System.out.println();
        } else if (this.rows == 8 && this.cols == 8) {
            System.out.println("    1   2   3   4   5   6   7   8");
            System.out.println();
        } else {
            System.out.println("Invalid Game");
            System.exit(0);
        }
        // The following code iterates through the elements of the cards array, and prints either an unflipped card (-),
        // or a blank space depending on the "isMatched" and "isFaceUp" boolean values of the card objects.
        for (int i = 0; i < this.rows; i++) {
            System.out.print(i + 1); // Print the y-coordinate at the start of the row
            System.out.print("   "); // Space for formatting
            for (int j = 0; j < this.cols; j++) {
                String toPrint;
                if (this.cards[i * this.cols + j].isMatched) { //If matched, assign a blank space where the card was
                    toPrint = " ";
                } else if (this.cards[i * this.cols + j].isFaceUp) { //If instead flipped, assign the symbol of the card.
                    toPrint = this.cards[i * this.cols + j].getSymbol();
                } else { // The card is unmatched and not flipped, so print a dash
                    toPrint = "-";
                }
                System.out.print(toPrint); //Print the assigned condition of the card
                System.out.print("   "); //Spacing for formatting
            }
            System.out.print("\n");
        }
        // The following code prints the flip counter. When first executed, 0 flips will have happened. flipsText
        // is dynamically updated in the chooseCard method.
        if (this.flipsText.equals("")) {
            System.out.println("Flips: 0");
        } else {
            System.out.println(this.flipsText);
        }
    }
    /* chooseCard
     * This method determines the status of the card the user has selected. It contains logic to flip and match cards.
     * @args row - the position of the card with respect to its row
     * @args col - the position of the card with respect to its column
     */
    public void chooseCard(int row, int col) {
        int index = (row - 1) * this.cols + (col - 1); // Calculates the position of the selected card
        if (!cards[index].isMatched) { // The card is not already matched, in which case continue processing.
            this.incrementFlipCount();
            if (indexOfOneAndOnlyFaceUpCard != -1) { // There is a face-up card already, in which case we can compare the
                // two cards.
                int matchIndex = indexOfOneAndOnlyFaceUpCard;
                if (matchIndex != index) { // Ensure two distinct cards are being compared to each other
                    // Check if the cards match
                    if (cards[matchIndex].getSymbol().equals(cards[index].getSymbol())) {// Symbols of both cards match
                        /*
                        Update the card's boolean fields. On the next execution, the game will print and these cards
                        will be replaced with blank space.
                        */
                        cards[index].isMatched = true;
                        cards[matchIndex].isMatched = true;
                    }
                    // Even if the cards don't match, turn the card face up
                    cards[index].isFaceUp = true;
                    /* Reset index to -1. This is done because when multiple cards are
                     compared, only two can be face up at any given point in the game. */
                    indexOfOneAndOnlyFaceUpCard = -1;
                }

            } else { // If the selected card is unmatched, or it is the only card face-up
                for (Card card : cards) {
                    card.isFaceUp = false; // Reset the board by setting all cards face-down
                }
                cards[index].isFaceUp = true; // Set only the selected card to face-up
                // Set the index of one and only face-up card equal to the selected card
                indexOfOneAndOnlyFaceUpCard = index;
            }
        }
    }

    /* incrementFlipCount
     * This method contains logic to increment the number of flips which are displayed to the user
     */
    public void incrementFlipCount() {
        this.noOfFlips += 1;
        this.flipsText = "Flips: " + this.noOfFlips; //update the text viewed by the user
    }

    /* checkGameOver
     * This method checks whether all cards are matched. If not, the game isn't over.
     * return false - if at least one card is not matched
     * return true - if all cards are matched
     */
    public boolean checkGameOver() {
        for (Card c : cards) {
            if (!c.isMatched) {
                return false;
            }
        }
        return true;
    }
}
