package Mechanics;

import GUIForms.bord;

import java.io.Serializable;
import java.util.*;

/**
 * The Mechanics.Player class handles all the classes that are related to a player board
 * and implements player's decisions.
 */
public class Player implements Serializable {
    /** Nickname of the player */
    private final String name;
    /** Points gathered by the player */
    private int points;
    /** Mechanics.Wall containing the progress of the player */
    public Wall wall;
    /** Pattern lines filled by the player */
    public PatternLines pattern;
    /** Indicator of possession of First Mechanics.Player Mechanics.Tile */
    private boolean first;
    /** List containing the current state of player's floor line */
    public ArrayList<Tile> floor;

    public bord playersBoard;

    /**
     * Default constructor
     */
    public Player(String name) {
        this.points = 0;
        this.name = name;
        floor = new ArrayList<>();
        pattern = new PatternLines();
        wall = new Wall();
        playersBoard = new bord();

    }

    public String getName() {
        return name;
    }

    /**
     * Method for getting the number of points of the player
     * @return Number of points of the player
     */
    public int getPoints() {
        return points;
    }
    public String getPointsString() {
        String pts = points + "";
        return pts;
    }

    /**
     * Method that adds the given amount of points
     * to player's points
     * @param points Given points
     */
    public void sumPoints(int points) {
        this.points += points;
    }

    /**
     * Method setting the indicator first
     */
    public void setFirst() {
        first = true;
    }

    /**
     * Method for checking if player has First tile
     */
    public boolean getFirst(){
        return first;
    }

    /**
     * Method for clearing the floor line
     */
    public void clearFloor() {
        floor.clear();
    }

    /**
     * Method for printing the floor
     */
    public void printFloor() {
        System.out.print("Floor:");
        for (int i = 0; i < floor.size(); i++){
            System.out.print(floor.get(i) + " ");
        }
        System.out.println();
    }

    public bord getPlayersBoard() {
        return playersBoard;
    }

    public void setPlayersBoard(bord playersBoard) {
        this.playersBoard = playersBoard;
    }
}