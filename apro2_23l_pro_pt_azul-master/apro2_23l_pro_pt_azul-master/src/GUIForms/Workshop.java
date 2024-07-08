package GUIForms;

import Mechanics.GUIGAME;
import Mechanics.Table;
import Mechanics.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionListener;

import static Mechanics.GUIGAME.que;

public class Workshop {

    private static Workshop INSTANCE;

    private JPanel workshop;
    private JButton confirmButton;
    private JPanel ScoreBoard;
    private JLabel playerName1;
    private JLabel playerName4;
    private JLabel playerName3;
    private JLabel playerName2;
    private JLabel score4;
    private JLabel score3;
    private JLabel score2;
    private JLabel score1;
    private JButton boardButton;
    private JButton centerButton;
    private JPanel factory1;
    private JPanel center;
    private JPanel factory2;
    private JPanel factory3;
    private JPanel factory4;
    private JPanel factory5;
    private JPanel factory6;
    private JPanel factory7;
    private JPanel factory8;
    private JPanel factory9;
    private JLabel scoreboard;
    private JButton factory1Tile1;
    private JButton factory1Tile2;
    private JButton factory1Tile3;
    private JButton factory1Tile4;
    private JButton factory2Tile1;
    private JButton factory2Tile2;
    private JButton factory2Tile3;
    private JButton factory2Tile4;
    private JButton factory3Tile1;
    private JButton factory3Tile2;
    private JButton factory3Tile3;
    private JButton factory3Tile4;
    private JButton factory4Tile1;
    private JButton factory4Tile2;
    private JButton factory4Tile3;
    private JButton factory4Tile4;
    private JButton factory5Tile1;
    private JButton factory5Tile2;
    private JButton factory5Tile3;
    private JButton factory5Tile4;
    private JButton factory6Tile1;
    private JButton factory6Tile2;
    private JButton factory6Tile3;
    private JButton factory6Tile4;
    private JButton factory7Tile1;
    private JButton factory7Tile2;
    private JButton factory7Tile3;
    private JButton factory7Tile4;
    private JButton factory8Tile1;
    private JButton factory8Tile2;
    private JButton factory8Tile3;
    private JButton factory8Tile4;
    private JButton factory9Tile1;
    private JButton factory9Tile2;
    private JButton factory9Tile3;
    private JButton factory9Tile4;
    private JTextField textField1;
    private ArrayList<JButton> buttons = new ArrayList<>();

    public JLabel getPlayerName1() {
        return playerName1;
    }

    public JLabel getPlayerName4() {
        return playerName4;
    }

    public JLabel getPlayerName3() {
        return playerName3;
    }

    public JLabel getPlayerName2() {
        return playerName2;
    }

    private Table table;
    private boolean isTileTaken = false;
    public int workshopid = 0;
    public Tile takenTile;
    public static int howmanytiles;

    public int getWorkshopid() {
        return workshopid;
    }

    public JPanel getWorkshopPanel() {
        return workshop;
    }

    public ActionListener getToCenter(){
        return e -> {
          FactoriesCenter center = FactoriesCenter.getFactoriesCenterINSTANCE();
          GUI.frame.add(center.getFactoryCenterPanel());
          workshop.setVisible(false);
          center.getFactoryCenterPanel().setVisible(true);
        };
    }
    public ActionListener getToBoard(){
        return e -> {
            System.out.println("essa");
            workshop.setVisible(false);
            bord bord = GUIGAME.getGame().getPlayersTables()[que.get(GUI.currentPlayerIndex)].getPlayersBoard();
            GUI.frame.add(bord.getFullbord());
            bord.getFullbord().setVisible(true);
//            Pattern pattern = new Pattern();
//            GUI.frame.add(pattern.getWall());
//            pattern.getWall().setVisible(true);
        };
    }

    public ActionListener  collectTiles(int numberOfFactory, int numberOfTile, Table table){
        final int[] howManydeleted = {0};
        return e -> {
            String name ="";
            if(!isTileTaken && !FactoriesCenter.getFactoriesCenterINSTANCE().isTilePicked()) {
                name = table.factories[numberOfFactory - 1].getContents()[numberOfTile - 1].getImageName();
                //table.factories[numberOfFactory - 1].remove(table.factories[numberOfFactory - 1].getContents()[numberOfTile - 1]);
                for (int i = 0; i < 4; i++) {
                    if (table.factories[numberOfFactory - 1].getContents()[i] == table.factories[numberOfFactory - 1].getContents()[numberOfTile - 1]) {
                        buttons.get((numberOfFactory - 1) * 4 + i).setVisible(false);
                        howManydeleted[0]++;
                        System.out.println(howManydeleted[0]);
                        continue;
                    }
                    buttons.get((numberOfFactory - 1) * 4 + i).setVisible(false);
                }

                for (Tile t : table.factories[numberOfFactory - 1].getContents()) {
                    if (t.getImageName() != name) {
                        addToCenter(1, t.getImageName());
                    }
                }

                System.out.println(name);
                isTileTaken = true;
                addToPlayersPocketFromWorkshop(howManydeleted[0], name);
                howmanytiles = howManydeleted[0];
            }
            JButton currentPressed = (JButton) e.getSource();
            workshopid = buttons.indexOf(currentPressed)/4;
            switch (name){
                case "img/red.png" -> takenTile = Tile.RED;
                case "img/black.png" -> takenTile = Tile.BLACK;
                case "img/white.png" -> takenTile = Tile.WHITE;
                case "img/yellow.png" -> takenTile = Tile.YELLOW;
                case "img/blue.png" -> takenTile = Tile.BLUE;
                default -> takenTile = null;
            }
            workshop.setVisible(false);
            bord board = GUIGAME.getGame().getPlayersTables()[que.get(GUI.currentPlayerIndex)].getPlayersBoard();
            GUI.frame.add(board.getFullbord());
            board.getFullbord().setVisible(true);
        };
    }
    public ActionListener chosenTile(){
        return e ->{
            JButton currentPressed = (JButton) e.getSource();
            workshopid = buttons.indexOf(currentPressed)/4;
            String currentIcon = currentPressed.getIcon().toString();
            switch (currentIcon){
                case "img/red.png" -> takenTile = Tile.RED;
                case "img/black.png" -> takenTile = Tile.BLACK;
                case "img/white.png" -> takenTile = Tile.WHITE;
                case "img/yellow.png" -> takenTile = Tile.YELLOW;
                case "img/blue.png" -> takenTile = Tile.BLUE;
            }

        };
    }



    public static Workshop getWorkshopInstance() throws Exception {
        if(INSTANCE == null){
            INSTANCE = new Workshop();
        }
        return INSTANCE;
    }

    public void refillWorkshop(){
        table = GUIGAME.getGame().table;
        int button = 0;
        for (int i = 0; i < table.factories.length; i++) {
            for (int j = 0; j < 4; j++) {
                HelpfulMethodsGuiJava.createButton(table.factories[i].getContents()[j].getImageName(),80,80,buttons.get(button));
                buttons.get(button).setVisible(true);
                button++;
            }
        }
    }

    private Workshop() throws Exception {
        playerName4.setForeground(Color.WHITE);
        playerName3.setForeground(Color.WHITE);
        playerName2.setForeground(Color.WHITE);
        playerName1.setForeground(Color.WHITE);
        playerName1.setFont(new Font("Georgia", Font.PLAIN, 28));
        playerName2.setFont(new Font("Georgia", Font.PLAIN, 28));
        playerName3.setFont(new Font("Georgia", Font.PLAIN, 28));
        playerName4.setFont(new Font("Georgia", Font.PLAIN, 28));
        score1.setForeground(Color.WHITE);
        score1.setFont(new Font("Georgia", Font.PLAIN, 28));
        score2.setForeground(Color.WHITE);
        score2.setFont(new Font("Georgia", Font.PLAIN, 28));
        score3.setForeground(Color.WHITE);
        score3.setFont(new Font("Georgia", Font.PLAIN, 28));
        score4.setForeground(Color.WHITE);
        score4.setFont(new Font("Georgia", Font.PLAIN, 28));
        isTileTaken = false;
        boardButton.addActionListener(getToBoard());
        scoreboard.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize("img/scoreboard.png",339,90));
        centerButton.addActionListener(getToCenter());
        HelpfulMethodsGuiJava.createButton("img/center.png", 300, 150, centerButton);
        HelpfulMethodsGuiJava.createButton("img/board.png",291,150,boardButton);
        //table = new Table(NumberOfPlayers.getClickedNumberOfPlayers());
        table = GUIGAME.getGame().table;
        //table.refillFactories();
        buttons.add(factory1Tile1);
        buttons.add(factory1Tile2);
        buttons.add(factory1Tile3);
        buttons.add(factory1Tile4);
        buttons.add(factory2Tile1);
        buttons.add(factory2Tile2);
        buttons.add(factory2Tile3);
        buttons.add(factory2Tile4);
        buttons.add(factory3Tile1);
        buttons.add(factory3Tile2);
        buttons.add(factory3Tile3);
        buttons.add(factory3Tile4);
        buttons.add(factory4Tile1);
        buttons.add(factory4Tile2);
        buttons.add(factory4Tile3);
        buttons.add(factory4Tile4);
        buttons.add(factory5Tile1);
        buttons.add(factory5Tile2);
        buttons.add(factory5Tile3);
        buttons.add(factory5Tile4);
        buttons.add(factory6Tile1);
        buttons.add(factory6Tile2);
        buttons.add(factory6Tile3);
        buttons.add(factory6Tile4);
        buttons.add(factory7Tile1);
        buttons.add(factory7Tile2);
        buttons.add(factory7Tile3);
        buttons.add(factory7Tile4);
        buttons.add(factory8Tile1);
        buttons.add(factory8Tile2);
        buttons.add(factory8Tile3);
        buttons.add(factory8Tile4);
        buttons.add(factory9Tile1);
        buttons.add(factory9Tile2);
        buttons.add(factory9Tile3);
        buttons.add(factory9Tile4);
        int button = 0;

        for (int i = 0; i < table.factories.length; i++) {
            for (int j = 0; j < 4; j++) {
                HelpfulMethodsGuiJava.createButton(table.factories[i].getContents()[j].getImageName(),80,80,buttons.get(button));
                button++;
            }
        }




        switch (NumberOfPlayers.getClickedNumberOfPlayers()){
            case 1:
                playerName1.setText(GUI.nameList.get(0));
                factoriesFor1Players();
                break;
            case 2:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                factoriesFor2Players();
                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
                break;
            case 3:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                playerName3.setText(GUI.nameList.get(2));
                factoriesFor3Players();
                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
                score3.setText(GUIGAME.getGame().playersTables[2].getPointsString());

                break;
            case 4:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                playerName3.setText(GUI.nameList.get(2));
                playerName4.setText(GUI.nameList.get(3));
                factoriesFor4Players();
                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
                score3.setText(GUIGAME.getGame().playersTables[2].getPointsString());
                score4.setText(GUIGAME.getGame().playersTables[3].getPointsString());
            default:
                playerName1.setText(GUI.nameList.get(0));
                factoriesVisibleBase();
        }
        factory1Tile1.addActionListener(collectTiles(1,1, table));
        factory1Tile2.addActionListener(collectTiles(1,2, table));
        factory1Tile3.addActionListener(collectTiles(1,3, table));
        factory1Tile4.addActionListener(collectTiles(1,4, table));
        factory2Tile1.addActionListener(collectTiles(2,1, table));
        factory2Tile2.addActionListener(collectTiles(2,2, table));
        factory2Tile3.addActionListener(collectTiles(2,3, table));
        factory2Tile4.addActionListener(collectTiles(2,4, table));
        factory3Tile1.addActionListener(collectTiles(3,1, table));
        factory3Tile2.addActionListener(collectTiles(3,2, table));
        factory3Tile3.addActionListener(collectTiles(3,3, table));
        factory3Tile4.addActionListener(collectTiles(3,4, table));
        factory4Tile1.addActionListener(collectTiles(4,1, table));
        factory4Tile2.addActionListener(collectTiles(4,2, table));
        factory4Tile3.addActionListener(collectTiles(4,3, table));
        factory4Tile4.addActionListener(collectTiles(4,4, table));
        factory5Tile1.addActionListener(collectTiles(5,1, table));
        factory5Tile2.addActionListener(collectTiles(5,2, table));
        factory5Tile3.addActionListener(collectTiles(5,3, table));
        factory5Tile4.addActionListener(collectTiles(5,4, table));
        factory6Tile1.addActionListener(collectTiles(6,1, table));
        factory6Tile2.addActionListener(collectTiles(6,2, table));
        factory6Tile3.addActionListener(collectTiles(6,3, table));
        factory6Tile4.addActionListener(collectTiles(6,4, table));
        factory7Tile1.addActionListener(collectTiles(7,1, table));
        factory7Tile2.addActionListener(collectTiles(7,2, table));
        factory7Tile3.addActionListener(collectTiles(7,3, table));
        factory7Tile4.addActionListener(collectTiles(7,4, table));
        factory8Tile1.addActionListener(collectTiles(8,1, table));
        factory8Tile2.addActionListener(collectTiles(8,2, table));
        factory8Tile3.addActionListener(collectTiles(8,3, table));
        factory8Tile4.addActionListener(collectTiles(8,4, table));
        factory9Tile1.addActionListener(collectTiles(9,1, table));
        factory9Tile2.addActionListener(collectTiles(9,2, table));
        factory9Tile3.addActionListener(collectTiles(9,3, table));
        factory9Tile4.addActionListener(collectTiles(9,4, table));

    }
    private void addToPlayersPocketFromWorkshop(int quantity, String tileName){

    }
    private void addToCenter(int quantity,String tileName){
                FactoriesCenter center = FactoriesCenter.getFactoriesCenterINSTANCE();
        switch (tileName){
            case ("img/white.png"):
                System.out.println(quantity+FactoriesCenter.getWhiteQuantity());
                FactoriesCenter.setWhiteQuantity(quantity+FactoriesCenter.getWhiteQuantity());
                center.updateTileQuantities();
                break;
            case ("img/black.png"):
                FactoriesCenter.setBlackQuantity(quantity+FactoriesCenter.getBlackQuantity());
                center.updateTileQuantities();
                break;
            case ("img/red.png"):

                FactoriesCenter.setRedQuantity(quantity+FactoriesCenter.getRedQuantity());
                center.updateTileQuantities();
                break;
            case ("img/blue.png"):
                FactoriesCenter.setBlueQuantity(quantity+FactoriesCenter.getBlueQuantity());
                center.updateTileQuantities();
                break;
            case ("img/yellow.png"):
                FactoriesCenter.setYellowQuantity(quantity+FactoriesCenter.getYellowQuantity());
                center.updateTileQuantities();
                break;
        }
    }

    private void factoriesVisibleBase() {
        factory1.setVisible(true);
        factory2.setVisible(true);
        factory3.setVisible(true);
        factory4.setVisible(true);
        factory5.setVisible(true);
        center.setVisible(true);
    }
    private void factoriesFor1Players(){
        factoriesVisibleBase();
        factory6.setVisible(false);
        factory7.setVisible(false);
        factory8.setVisible(false);
        factory9.setVisible(false);
    }
    private void factoriesFor2Players(){
        factoriesVisibleBase();
        factory6.setVisible(false);
        factory7.setVisible(false);
        factory8.setVisible(false);
        factory9.setVisible(false);
    }
    private void factoriesFor3Players(){
        factoriesVisibleBase();
        factory6.setVisible(true);
        factory7.setVisible(true);
        factory8.setVisible(false);
        factory9.setVisible(false);
    }
    private void factoriesFor4Players(){
        factoriesVisibleBase();
        factory6.setVisible(true);
        factory7.setVisible(true);
        factory8.setVisible(true);
        factory9.setVisible(true);
    }

    public  void updateScoreFor2Players(){
       score1.setText((GUIGAME.getGame().playersTables[0].getPointsString()));
       score2.setText((GUIGAME.getGame().playersTables[1].getPointsString()));
    }
    public void updateScoreFor3Players(){
        updateScoreFor2Players();
        score3.setText((GUIGAME.getGame().playersTables[2].getPointsString()));
    }
    public void updateScoreFor4Players(){
        updateScoreFor3Players();
        score4.setText((GUIGAME.getGame().playersTables[3].getPointsString()));
    }


    public boolean isTileTaken() {
        return isTileTaken;
    }

    public void setIfTileIsTaken(){
        isTileTaken = false;
    }

    public void setWorkshopid(int workshopid) {
        this.workshopid = workshopid;
    }

    public void setTakenTile(Tile takenTile) {
        this.takenTile = takenTile;
    }
}
