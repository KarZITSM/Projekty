package Mechanics;

import java.io.Serializable;

/**
 * Class representing the factory, with its
 * storage and methods
 */
public class Factory implements Serializable {

    /**
     * The tiles that the factory contains
     */
    private final Tile[] contents;

    /**
     * Default constructor
     */
    public Factory() {
        contents = new Tile[4];
    }

    /**
     * Standard isEmpty() method
     * @return Information whether it is empty
     */
    public boolean isEmpty() {
        for (Tile tile : contents){
            if (tile != null)
                return false;
        }
        return true;
    }

    /**
     * Method that removes the tiles of chosen colour
     * from the factory
     * @param tile chosen colour
     */
    public void remove(Tile tile) {
        for (int i = 0; i < 4; i++){
            if (contents[i] == tile)
                contents[i] = null;
        }
    }

    /**
     * Returns the contents of the factory
     * @return Array of tiles contained in the factory
     */
    public Tile[] getContents() {
        return contents;
    }

    /**
     * Adding tiles from the bag to the factory at the beginning of the next round of the game
     * @param tiles added tiles
     */
    public void addTiles(Tile[] tiles) {
        System.arraycopy(tiles, 0, contents, 0, tiles.length);
    }

}