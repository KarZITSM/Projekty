package Mechanics;

import java.io.Serializable;
import java.util.*;

/**
 * Class representing the table, on which the factories,
 * bag and box are located
 */
public class Table  implements Serializable {
    /**
     * ArrayList representing the tiles present in the box
     */
    public ArrayList<Tile> box;
    /**
     * ArrayList representing the tiles present in the bag
     */
    public ArrayList<Tile> bag;
    /**
     * ArrayList representing the tiles present in the center
     */
    public ArrayList<Tile> center;

    /**
     * List of factories
     * 2 players - 5 factories
     * 3 players - 7 factories
     * 4 players - 9 factories
     */
    public Factory[] factories;

    /**
     * Number of factories
     */
    private final int factoryNo;

    /**
     * Default constructor
     */
    public Table(int numberOfPlayers) throws Exception {
        factoryNo = numberOfPlayers*2 + 1;
        factories = new Factory[factoryNo];
        for (int i = 0; i < factoryNo; i++)
            factories[i] = new Factory();
        center = new ArrayList<>();
        center.add(Tile.FIRSTTILE);
        bag = new ArrayList<>(100);
        for (int i = 0; i < 20; i++) {
            bag.add(Tile.WHITE);
            bag.add(Tile.BLUE);
            bag.add(Tile.BLACK);
            bag.add(Tile.RED);
            bag.add(Tile.YELLOW);
        }

        box = new ArrayList<>();
        refillFactories();
    }

    /**
     * Method for evaluating whether the bag is empty
     * @return true if bag is empty
     */
    public boolean isBagEmpty() {
        return bag.isEmpty();
    }


    /**
     * Method for evaluating whether tiles of the given colour are present in the factory
     * @param tile chosen colour
     * @param factory index of the chosen factory
     * @return true if chosen factory contains chosen colour
     */
    public boolean isColourInFactory(Tile tile, int factory) {
        if (factory == 9){
            for (int i = 0; i < center.size(); i++){
                if (center.contains(tile))
                    return true;
            }
        }
        else {
            for (int i = 0; i < 4; i++) {
                if(factories[factory].getContents()[i] == tile)
                    return true;
            }
        }
        return false;
    }

    /**
     * Method for refilling bag with tiles from the box
     */
    public void refillBag() throws Exception {
        if(isBagEmpty()) {
            bag = box;
            box = new ArrayList<>();
        } else
            throw new Exception("Can't refill bag until it is not empty");
    }

    /**
     * Method for refilling factories with tiles from the bag
     */
    public void refillFactories() throws Exception {
        if(bag.size() < factoryNo*4){
            refillBag();
            System.out.println(bag.size());
            System.out.println(bag);
        }

        Random rand = new Random();
        for (int i = 0; i < factoryNo; i++){
            int temp;
            Tile tile1, tile2, tile3, tile4;
            temp = rand.nextInt(bag.size());
            tile1 = bag.get(temp);
            bag.remove(temp);
            temp = rand.nextInt(bag.size());
            tile2 = bag.get(temp);
            bag.remove(temp);
            temp = rand.nextInt(bag.size());
            tile3 = bag.get(temp);
            bag.remove(temp);
            temp = rand.nextInt(bag.size());
            tile4 = bag.get(temp);
            bag.remove(temp);
            Tile[] factoryTiles = new Tile[]{tile1, tile2, tile3, tile4};
            factories[i].addTiles(factoryTiles);
        }
    }

    /**
     * Method for evaluating whether the priority tile is in the center
     * @return true if priority tile is in the center
     */
    public boolean isPriorityTileInCenter() {
        return center.contains(Tile.FIRSTTILE);
    }

}