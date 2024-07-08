package GUIForms;

import Mechanics.Tile;

import javax.swing.*;
import java.util.ArrayList;

public class Wall {
    private JPanel wall;
    private JLabel tile6;
    private JLabel tile11;
    private JLabel tile1;
    private JLabel tile12;
    private JLabel tile2;
    private JLabel tile3;
    private JLabel tile4;
    private JLabel tile5;
    private JLabel tile7;
    private JLabel tile8;
    private JLabel tile9;
    private JLabel tile10;
    private JLabel tile13;
    private JLabel tile14;
    private JLabel tile15;
    private JLabel tile16;
    private JLabel tile17;
    private JLabel tile18;
    private JLabel tile19;
    private JLabel tile20;
    private JLabel tile21;
    private JLabel tile22;
    private JLabel tile23;
    private JLabel tile24;
    private JLabel tile25;
    private JLabel[] tiles = new JLabel[25];
    public Wall(){
        tiles[0] = tile1;
        tiles[1] = tile2;
        tiles[2] = tile3;
        tiles[3] = tile4;
        tiles[4] = tile5;
        tiles[5] = tile6;
        tiles[6] = tile7;
        tiles[7] = tile8;
        tiles[8] = tile9;
        tiles[9] = tile10;
        tiles[10] = tile11;
        tiles[11] = tile12;
        tiles[12] = tile13;
        tiles[13] = tile14;
        tiles[14] = tile15;
        tiles[15] = tile16;
        tiles[16] = tile17;
        tiles[17] = tile18;
        tiles[18] = tile19;
        tiles[19] = tile20;
        tiles[20] = tile21;
        tiles[21] = tile22;
        tiles[22] = tile23;
        tiles[23] = tile24;
        tiles[24] = tile25;

        for(JLabel tile: tiles){
            HelpfulMethodsGuiJava.createLabel("img/notile.png", 50,50, tile);
        }
    }
    public void setTileIcon(Tile tile, int tilePosition){
        switch (tile){
            case RED -> {
                HelpfulMethodsGuiJava.createLabel("img/red.png", 50,50, tiles[tilePosition+1]);
            }
            case WHITE -> {
                HelpfulMethodsGuiJava.createLabel("img/white.png", 50,50, tiles[tilePosition+1]);
            }
            case BLUE -> {
                HelpfulMethodsGuiJava.createLabel("img/blue.png", 50,50, tiles[tilePosition+1]);
            }
            case BLACK -> {
                HelpfulMethodsGuiJava.createLabel("img/black.png", 50,50, tiles[tilePosition+1]);
            }
            case YELLOW -> {
                HelpfulMethodsGuiJava.createLabel("img/yellow.png", 50,50, tiles[tilePosition+1]);
            }
            // temporary icon is "notile" - not created FIRSTTILE icon yet
            case FIRSTTILE -> {
                HelpfulMethodsGuiJava.createLabel("img/notile.png", 50,50, tiles[tilePosition+1]);
            }
        }
    }

    public JPanel getWall() {
        return wall;
    }
}
