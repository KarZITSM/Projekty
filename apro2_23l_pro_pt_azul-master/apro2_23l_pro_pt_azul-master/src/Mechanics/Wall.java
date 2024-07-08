package Mechanics;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class representing the wall with the pattern present
 * on each player's board
 */
public class Wall implements Serializable {
    /**
     * Matrix indicating which of the places on player's wall are filled
     */
    public final boolean[][] filled;

    /**
     * Matrix of tiles on the wall creating the pattern
     */
    public final Tile[][] pattern;

    /**
     * Default constructor
     */
    public Wall(){
        filled = new boolean[][]{{false, false, false, false, false},
                                 {false, false, false, false, false},
                                 {false, false, false, false, false},
                                 {false, false, false, false, false},
                                 {false, false, false, false, false}};
        pattern = new Tile[][]{{Tile.BLUE, Tile.WHITE, Tile.BLACK, Tile.RED, Tile.YELLOW},
                               {Tile.YELLOW, Tile.BLUE, Tile.WHITE, Tile.BLACK, Tile.RED},
                               {Tile.RED, Tile.YELLOW, Tile.BLUE, Tile.WHITE, Tile.BLACK},
                               {Tile.BLACK, Tile.RED, Tile.YELLOW, Tile.BLUE, Tile.WHITE},
                               {Tile.WHITE, Tile.BLACK, Tile.RED, Tile.YELLOW, Tile.BLUE}};
    }

    /**
     * Method for placing a tile on the wall
     * @param tile colour of the tile
     * @param row row in which it will be put
     * @return Number of points scored for the tile's placement
     */
    public int addTile(Tile tile, int row) {
        int column = 0;
        for (int i = 0; i < 5; i++){
            if (pattern[row][i] == tile){
                filled[row][i] = true;
                column = i;
                break;
            }
        }
        return countPointsForTile(column, row);
    }

    /**
     * Placing a tile on the wall
     * @param column index of column in which we placed tile
     * @param row index of row in which we placed tile
     * @return Number of points scored for the tile's placement
     */
    public int countPointsForTile(int column, int row) {
        int horizontalPoints = 0;
        int verticalPoints = 0;

        // checking horizontally
        for (int i = column - 1; i >= 0; i--){
            if (!filled[row][i])
                break;
            horizontalPoints++;
        }

        for (int i = column + 1; i < 5; i++){
            if (!filled[row][i])
                break;
            horizontalPoints++;
        }

        // checking vertically
        for (int i = row - 1; i >= 0; i--){
            if (!filled[i][column])
                break;
            verticalPoints++;
        }

        for (int i = row + 1; i < 5; i++){
            if (!filled[i][column])
                break;
            verticalPoints++;
        }

        if(verticalPoints == 4)
            verticalPoints += 7;
        if(horizontalPoints == 4)
            horizontalPoints += 2;
        if(horizontalPoints > 0)
            horizontalPoints++;
        if(verticalPoints > 0)
            verticalPoints++;
        if(verticalPoints + horizontalPoints == 0)
            verticalPoints++;


        // Checking if the color blue is completed
        if( filled[0][0] && filled[1][1] && filled[2][2] && filled[3][3] && filled[4][4]){
            horizontalPoints+=10;
        }
        // Checking if the color white is completed
        else if (filled[0][4] && filled[1][0] && filled[2][1] && filled[3][2] && filled[4][3]) {
            horizontalPoints+=10;
        }
        // Checking if the color black is completed
        else if (filled[0][3] && filled[1][4] && filled[2][0] && filled[3][1] && filled[4][2]) {
            horizontalPoints+=10;
        }
        // Checking if the color red is completed
        else if (filled[0][2] && filled[1][3] && filled[2][4] && filled[3][0] && filled[4][1]){
            horizontalPoints+=10;
        }
        // Checking if the color yellow is completed
        else if (filled[0][1] && filled[1][2] && filled[2][3] && filled[3][4] && filled[4][0]) {
            horizontalPoints+=10;
        }

        return verticalPoints + horizontalPoints;
    }

    /**
     * Method for evaluating whether the given colour is already present in the indicated row
     * @param tile indicated colour
     * @param row index of indicated row
     * @return true if indicated row already contains tile
     */
    public boolean colourCheck(Tile tile, int row) {
        for (int i = 0; i < 5; i++) {
            if (pattern[row][i] == tile && filled[row][i])
                return true;
        }
        return false;
    }

    /**
     * Method for evaluating whether the row is full
     * @param row index of row
     * @return true if row is full
     */
    public boolean IsRowFull(int row) {
        if (row > 5 || row < 0)
            throw new IllegalArgumentException("The specified row does not exist");
        for (int i = 0; i < 5; i++){
            if (!filled[row][i])
                return false;
        }
        return true;
    }

    /**
     * Method for evaluating whether the row contains given tile
     * @param row index of row
     * @param tile indicated colour
     * @return true if row is full
     */
    public boolean containsTile(Tile tile, int row){
        for (int i = 0; i < 5; i++){
            if (filled[row][i] && pattern[row][i] == tile)
                return true;
        }
        return false;
    }

    /**
     * Method for printing contents of the wall
     */

    public void printWall(){
        System.out.println();
        System.out.println("Mechanics.Wall:");
        for (int i = 0; i < 5; i++){
            System.out.print("Line " + (i + 1) + ": ");
            System.out.print(Arrays.toString(pattern[i]) + " ");
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < 5; i++){
            System.out.print("Line:" + (i + 1) + ": ");
            System.out.print(Arrays.toString(filled[i]) + " ");
            System.out.println();
        }
        System.out.println();
    }

    public boolean[][] getFilled() {
        return filled;
    }
}