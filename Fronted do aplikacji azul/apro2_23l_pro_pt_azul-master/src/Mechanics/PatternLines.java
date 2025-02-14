package Mechanics;

import java.io.Serializable;

/**
 * The Mechanics.PatternLines class represents lines that
 * are being filled by the player
 */
public class PatternLines implements Serializable {
    /**
     * Rows of pattern lines
     */
    public final Tile[] colours;
    public final int[] amounts;
    /**
     * Default constructor
     */
    public PatternLines() {
        colours = new Tile[5];
        amounts = new int[]{0, 0, 0, 0, 0};
    }

    /**
     * Method for adding given amount of tiles of certain colour to given row of pattern lines
     * @param row given row
     * @param colour colour of tiles
     * @param amount amount of given tiles
     */
    public void addToRow(int row, Tile colour, int amount) {
        if (row > 5 || row < 0)
            throw new IllegalArgumentException("The specified row does not exist");
        if (amount < 0 || (amount + amounts[row]) > (row + 1) )
            throw new IllegalArgumentException("The amount of tiles can't be fewer than 0 or higher than the index of row we want to add tiles to");
        if (colours[row] != colour && colours[row] != null)
            throw new IllegalArgumentException("Wrong colour");
        amounts[row] += amount;
        colours[row] = colour;
    }

    /**
     * Method for evaluating whether the given row is full
     * @param row index of given row
     * @return true if given row is full
     */
    public boolean isRowFull(int row) {
        if (row > 5 || row < 0)
            throw new IllegalArgumentException("The specified row does not exist");
        return amounts[row] == (row + 1);
    }

    /**
     * Method for evaluating whether the given row is empty
     * @param row index of given row
     * @return true if given row is empty
     */
    public boolean isRowEmpty(int row) {
        if (row > 5 || row < 0)
            throw new IllegalArgumentException("The specified row does not exist");
        return amounts[row] == 0;
    }

    /**
     * Method for clearing the given row
     * @param row index of given row
     */
    public void clearRow(int row) {
        if (row > 5 || row < 0)
            throw new IllegalArgumentException("The specified row does not exist");
        amounts[row] = 0;
        colours[row] = null;
    }

    /**
     * Method for printing contents of the pattern lines
     */
    public void printPatternLine(){
        System.out.println();
        System.out.println("Patternline:");
        for (int i = 0; i < 5; i++){
            System.out.print("Line " + (i + 1) + ": ");
            for(int j = 0; j < amounts[i]; j++)
                System.out.print(" " + colours[i] + " ");
            System.out.println();
        }
        System.out.println();
    }
}