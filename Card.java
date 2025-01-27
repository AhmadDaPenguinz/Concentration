package com.ICS4U;

import java.io.Serializable;

/*
 * Class: Card
 * Programmer: Ahmad Bajwa
 * Description: Holds data and fields pertaining to card objects, including their symbol, face-up/face-down and matched
 * statuses.
 * Implements the Serializable interface to allow conversion and reversion between file-savable byte streams and
 * Java objects.
 * */
public class Card implements Serializable {
    // Field storing symbol assigned to a card object
    String symbol;
    // Booleans dictating the statuses of a card object
    public boolean isFaceUp = false;
    public boolean isMatched = false;

    // Constructor assigns a symbol to the card object
    public Card(String symbol) {
        this.symbol = symbol;
    }
    // Return the symbol of the Card object
    public String getSymbol() {
        return symbol;
    }
}
