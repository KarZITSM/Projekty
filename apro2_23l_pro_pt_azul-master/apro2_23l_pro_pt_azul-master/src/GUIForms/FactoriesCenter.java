package GUIForms;

import Mechanics.GUIGAME;
import Mechanics.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static Mechanics.GUIGAME.que;

public class FactoriesCenter {
    private static FactoriesCenter INSTANCE;
    private JPanel factoryCenter;
    private JPanel scoreBoard;
    private JLabel playerName1;
    private JLabel playerName4;
    private JLabel playerName3;
    private JLabel playerName2;
    private JLabel scoreboard;
    private JLabel score4;
    private JLabel score3;
    private JLabel score2;
    private JLabel score1;
    private JButton factoriesButton;
    private JButton whiteTile;
    private JButton blackTile;
    private JButton blueTile;
    private JButton redTile;
    private JButton yellowTile;
    private JPanel centerTiles;
    private JLabel whiteNumber;
    private JLabel blackNumber;
    private JLabel blueNumber;
    private JLabel redNumber;
    private JLabel yellowNumber;

    private static int whiteQuantity = 0;
    private static int blackQuantity = 0;
    private static int redQuantity = 0;
    private static int blueQuantity = 0;
    private static int yellowQuantity = 0;

    private boolean isTilePicked = false;

    public static int getWhiteQuantity() {
        return whiteQuantity;
    }

    public static int getBlackQuantity() {
        return blackQuantity;
    }

    public static int getRedQuantity() {
        return redQuantity;
    }

    public static int getBlueQuantity() {
        return blueQuantity;
    }

    public static int getYellowQuantity() {
        return yellowQuantity;
    }

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

    public JPanel getFactoryCenterPanel(){
        return factoryCenter;
    }
    public ActionListener getToWorkshop(){
        return e -> {
            Workshop workshop;
          factoryCenter.setVisible(false);
            try {
                workshop = Workshop.getWorkshopInstance();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            workshop.getWorkshopPanel().setVisible(true);
        };
    }

    public static FactoriesCenter getFactoriesCenterINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new FactoriesCenter();
        }
        return INSTANCE;
    }
    public ActionListener tilePicked(String name){
        return e -> {
            try {
                if(!isTilePicked && !Workshop.getWorkshopInstance().isTileTaken()) {
                    addTileToPlayersPocketFromCenter(name);
                    isTilePicked = true;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            factoryCenter.setVisible(false);
            bord board = GUIGAME.getGame().getPlayersTables()[que.get(GUI.currentPlayerIndex)].getPlayersBoard();
            GUI.frame.add(board.getFullbord());
            board.getFullbord().setVisible(true);
        };
    }

    private FactoriesCenter(){
        playerName4.setForeground(Color.WHITE);
        playerName3.setForeground(Color.WHITE);
        playerName2.setForeground(Color.WHITE);
        playerName1.setForeground(Color.WHITE);
        playerName1.setFont(new Font("Georgia", Font.PLAIN, 28));
        playerName2.setFont(new Font("Georgia", Font.PLAIN, 28));
        playerName3.setFont(new Font("Georgia", Font.PLAIN, 28));
        playerName4.setFont(new Font("Georgia", Font.PLAIN, 28));
        whiteNumber.setFont(new Font("Georgia", Font.PLAIN, 28));
        blackNumber.setFont(new Font("Georgia", Font.PLAIN, 28));
        blueNumber.setFont(new Font("Georgia", Font.PLAIN, 28));
        redNumber.setFont(new Font("Georgia", Font.PLAIN, 28));
        yellowNumber.setFont(new Font("Georgia", Font.PLAIN, 28));
        whiteNumber.setForeground(new Color(245,255,255));
        blackNumber.setForeground(new Color(245,255,255));
        blueNumber.setForeground(new Color(245,255,255));
        redNumber.setForeground(new Color(245,255,255));
        yellowNumber.setForeground(new Color(245,255,255));
        scoreboard.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize("img/scoreboard.png",339,90));
        score1.setForeground(Color.WHITE);
        score1.setFont(new Font("Georgia", Font.PLAIN, 28));
        score2.setForeground(Color.WHITE);
        score2.setFont(new Font("Georgia", Font.PLAIN, 28));
        score3.setForeground(Color.WHITE);
        score3.setFont(new Font("Georgia", Font.PLAIN, 28));
        score4.setForeground(Color.WHITE);
        score4.setFont(new Font("Georgia", Font.PLAIN, 28));
        switch (NumberOfPlayers.getClickedNumberOfPlayers()){
            case 1:
                playerName1.setText(GUI.nameList.get(0));

                break;
            case 2:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
                break;
            case 3:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                playerName3.setText(GUI.nameList.get(2));
                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
                score3.setText(GUIGAME.getGame().playersTables[2].getPointsString());

                break;
            case 4:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                playerName3.setText(GUI.nameList.get(2));
                playerName4.setText(GUI.nameList.get(3));
                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
                score3.setText(GUIGAME.getGame().playersTables[2].getPointsString());
                score4.setText(GUIGAME.getGame().playersTables[0].getPointsString());

            default:
                playerName1.setText(GUI.nameList.get(0));

        }
        HelpfulMethodsGuiJava.createButton("img/white.png",100,100,whiteTile);
        HelpfulMethodsGuiJava.createButton("img/black.png",100,100,blackTile);
        HelpfulMethodsGuiJava.createButton("img/blue.png",100,100,blueTile);
        HelpfulMethodsGuiJava.createButton("img/red.png",100,100,redTile);
        HelpfulMethodsGuiJava.createButton("img/yellow.png",100,100,yellowTile);
        updateTileQuantities();
        factoriesButton.addActionListener(getToWorkshop());
        HelpfulMethodsGuiJava.createButton("img/factories.png", 300,150,factoriesButton);
        whiteTile.addActionListener(tilePicked("WHITE"));
        blackTile.addActionListener(tilePicked("BLACK"));
        blueTile.addActionListener(tilePicked("BLUE"));
        redTile.addActionListener(tilePicked("RED"));
        yellowTile.addActionListener(tilePicked("YELLOW"));

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

    public void updateTileQuantities() {
        whiteNumber.setText(String.valueOf(whiteQuantity));
        blackNumber.setText(String.valueOf(blackQuantity));
        redNumber.setText(String.valueOf(redQuantity));
        blueNumber.setText(String.valueOf(blueQuantity));
        yellowNumber.setText(String.valueOf(yellowQuantity));
    }
    private void addTileToPlayersPocketFromCenter( String tileName) throws Exception {
        int quantity;
        Workshop.getWorkshopInstance().setWorkshopid(9);
            switch (tileName) {
                case ("WHITE"):
                    if(whiteQuantity!=0) {
                        quantity = whiteQuantity;
                        System.out.println(quantity);
                        setWhiteQuantity(0);
                        updateTileQuantities();
                        Workshop.getWorkshopInstance().setTakenTile(Tile.WHITE);
                    }
                    break;
                case ("BLACK"):
                    if (blackQuantity !=0) {
                        quantity = blackQuantity;
                        setBlackQuantity(0);
                        updateTileQuantities();
                        Workshop.getWorkshopInstance().setTakenTile(Tile.BLACK);
                    }
                    break;
                case ("RED"):
                    if(redQuantity != 0) {
                        quantity = redQuantity;
                        setRedQuantity(0);
                        updateTileQuantities();
                        Workshop.getWorkshopInstance().setTakenTile(Tile.RED);
                    }
                    break;
                case ("BLUE"):
                    if(blueQuantity !=0) {
                        quantity = blueQuantity;
                        setBlueQuantity(0);
                        updateTileQuantities();
                        Workshop.getWorkshopInstance().setTakenTile(Tile.BLUE);
                    }
                    break;
                case ("YELLOW"):
                    if(yellowQuantity != 0) {
                        quantity = yellowQuantity;
                        setYellowQuantity(0);
                        updateTileQuantities();
                        Workshop.getWorkshopInstance().setTakenTile(Tile.YELLOW);
                    }
                    break;

            }
    }

    public static void setWhiteQuantity(int whiteQuantity) {
        FactoriesCenter.whiteQuantity = whiteQuantity;
    }

    public static void setBlackQuantity(int blackQuantity) {
        FactoriesCenter.blackQuantity = blackQuantity;
    }

    public static void setRedQuantity(int redQuantity) {
        FactoriesCenter.redQuantity = redQuantity;
    }

    public static void setBlueQuantity(int blueQuantity) {
        FactoriesCenter.blueQuantity = blueQuantity;
    }

    public static void setYellowQuantity(int yellowQuantity) {
        FactoriesCenter.yellowQuantity = yellowQuantity;
    }

    public boolean isTilePicked() {
        return isTilePicked;
    }
    public void setIfTileIsPicked(){
        isTilePicked = false;
    }
}
