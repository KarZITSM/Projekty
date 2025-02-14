package GUIForms;

import Mechanics.GUIGAME;
import Mechanics.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static Mechanics.GUIGAME.*;

public class bord {
    private JPanel fullbord;
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
    private JPanel stairs;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JButton button10;
    private JButton button15;
    private JButton button16;
    private JButton button17;
    private JButton button18;
    private JButton button19;
    private JButton button20;
    private JPanel wall;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JLabel l4;
    private JLabel l5;
    private JLabel l6;
    private JLabel l7;
    private JLabel l8;
    private JLabel l9;
    private JLabel l10;
    private JLabel l11;
    private JLabel l12;
    private JLabel l13;
    private JLabel l14;
    private JLabel l15;
    private JLabel l16;
    private JLabel l17;
    private JLabel l18;
    private JLabel l19;
    private JLabel l20;
    private JLabel l21;
    private JLabel l22;
    private JLabel l23;
    private JLabel l24;
    private JLabel l25;
    private JButton button21;
    private JButton button22;
    private JButton RedirectButton;
    private JLabel turnInfo;
    private JPanel TurnInfo;
    private JButton button23;
    private JPanel floor;

    private JLabel[] tiles = new JLabel[25];
    private static int row;
    public ArrayList<JButton> buttonsArray = new ArrayList<>();

    public ArrayList<JButton> floorArray = new ArrayList<>();

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
    public JLabel[][] wallmatrix = new JLabel[5][5];

    public bord() {


        floorArray.add(button17);
        floorArray.add(button18);
        floorArray.add(button19);
        floorArray.add(button20);
        floorArray.add(button22);
        floorArray.add(button21);
        floorArray.add(button16);

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
        turnInfo.setForeground(new Color(245,255,255));
        scoreboard.setForeground(new Color(245,255,255));

        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button2);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button3);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button5);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button6);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button7);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button8);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button9);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button10);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button11);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button12);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button13);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button14);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button15);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button16);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button17);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button18);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button19);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button20);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button21);
        HelpfulMethodsGuiJava.createButton("img/notile.png",90,90,button4);
        HelpfulMethodsGuiJava.createButton("img/notile.png", 90, 90, button1);
        HelpfulMethodsGuiJava.createButton("img/notile.png", 90, 90, button22);

        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 90,90, l1);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 90,90, l2);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 90,90, l3);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 90,90, l4);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 90,90, l5);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 90,90, l6);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 90,90, l7);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 90,90, l8);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 90,90, l9);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 90,90, l10);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 90,90, l11);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 90,90, l12);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 90,90, l13);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 90,90, l14);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 90,90, l15);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 90,90, l16);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 90,90, l17);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 90,90, l18);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 90,90, l19);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 90,90, l20);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 90,90, l21);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 90,90, l22);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 90,90, l23);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 90,90, l24);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 90,90, l25);

        HelpfulMethodsGuiJava.createButton("img/workshop.png", 250,100, button23);
        button23.addActionListener(getToWorkhop());

        buttonsArray.add(button1);
        buttonsArray.add(button5);
        buttonsArray.add(button6);
        buttonsArray.add(button4);
        buttonsArray.add(button7);
        buttonsArray.add(button11);
        buttonsArray.add(button3);
        buttonsArray.add(button8);
        buttonsArray.add(button12);
        buttonsArray.add(button14);
        buttonsArray.add(button2);
        buttonsArray.add(button9);
        buttonsArray.add(button13);
        buttonsArray.add(button10);
        buttonsArray.add(button15);

        int index = 0;

        for (Component component : this.stairs.getComponents()) {
            if (component instanceof JButton && index < 16) {
                ((JButton) component).addActionListener(makeTurn());
                index++;
            }
        }
        for (Component component  : this.floor.getComponents()){
            ((JButton) component).addActionListener(makeTurn());
        }

//        for (Component component : this.stairs.getComponents()) {
//            if (component instanceof JButton && index < 16) {
//                ((JButton) component).addActionListener(makeTurnFloor());
//                index++;
//            }
//        }

       wallmatrix[0][0] = l1;
       wallmatrix[0][1] = l6;
       wallmatrix[0][2] = l11;
       wallmatrix[0][3] = l16;
       wallmatrix[0][4] = l21;
        wallmatrix[1][0] = l2;
        wallmatrix[1][1] = l7;
        wallmatrix[1][2] = l12;
        wallmatrix[1][3] = l17;
        wallmatrix[1][4] = l22;
        wallmatrix[2][0] = l3;
        wallmatrix[2][1] = l8;
        wallmatrix[2][2] = l13;
        wallmatrix[2][3] = l18;
        wallmatrix[2][4] = l23;
        wallmatrix[3][0] = l4;
        wallmatrix[3][1] = l9;
        wallmatrix[3][2] = l14;
        wallmatrix[3][3] = l19;
        wallmatrix[3][4] = l24;
        wallmatrix[4][0] = l5;
        wallmatrix[4][1] = l10;
        wallmatrix[4][2] = l15;
        wallmatrix[4][3] = l20;
        wallmatrix[4][4] = l25;

        switch (NumberOfPlayers.getClickedNumberOfPlayers()){
            case 1:
                playerName1.setText(GUI.nameList.get(0));

                break;
            case 2:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
//                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
//                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
                break;
            case 3:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                playerName3.setText(GUI.nameList.get(2));
//                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
//                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
//                score3.setText(GUIGAME.getGame().playersTables[2].getPointsString());

                break;
            case 4:
                playerName1.setText(GUI.nameList.get(0));
                playerName2.setText(GUI.nameList.get(1));
                playerName3.setText(GUI.nameList.get(2));
                playerName4.setText(GUI.nameList.get(3));
//                score1.setText(GUIGAME.getGame().playersTables[0].getPointsString());
//                score2.setText(GUIGAME.getGame().playersTables[1].getPointsString());
//                score3.setText(GUIGAME.getGame().playersTables[2].getPointsString());
//                score4.setText(GUIGAME.getGame().playersTables[0].getPointsString());
            default:
                playerName1.setText(GUI.nameList.get(0));

        }

    }

    private ActionListener makeTurnFloor() {
        return e -> {

        };
    }

    public JPanel getFullbord() {
        return fullbord;
    }
    public JLabel setTurnInfo(){
        turnInfo.setText(getGame().playersTables[que.get(GUI.currentPlayerIndex)].getName()+"'s turn!");
        return turnInfo;
    }
    public ActionListener whereToPutTheTile(Tile tile){
        return e->{
        };
    }
    public ActionListener getToWorkhop(){
        return e->{
            try {
                Workshop.getWorkshopInstance().getWorkshopPanel().setVisible(true);
                getFullbord().setVisible(false);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public ActionListener makeTurn(){
        return e -> {
            boolean workshopInfo;
            try {
                workshopInfo = Workshop.getWorkshopInstance().isTileTaken();
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
            if(workshopInfo || FactoriesCenter.getFactoriesCenterINSTANCE().isTilePicked()) {
                int index = 100;
                for (int i = 0; i < buttonsArray.size(); i++) {
                    if (e.getSource() == buttonsArray.get(i)) {
                        index = i;
                    }
                }

                row = 0;
                int howManyTilesInARow;
                if (e.getSource().equals(button1)) {
                    row = 1;
                    howManyTilesInARow = 1;
                } else if (e.getSource().equals(button5) || e.getSource().equals(button6)) {
                    row = 2;
                    howManyTilesInARow = 2;
                } else if (e.getSource().equals(button4) || e.getSource().equals(button7) || e.getSource().equals(button11)) {
                    row = 3;
                    howManyTilesInARow = 3;
                } else if (e.getSource().equals(button14) || e.getSource().equals(button12) || e.getSource().equals(button8) || e.getSource().equals(button3)) {
                    row = 4;
                    howManyTilesInARow = 4;
                } else if (e.getSource().equals(button15) || e.getSource().equals(button10) || e.getSource().equals(button13) || e.getSource().equals(button9) || e.getSource().equals(button2)) {
                    row = 5;
                    howManyTilesInARow = 5;
                } else {
                    row = 6;
                }
                int k = 0;
                int floorindex = 0;
                int addtiles = 0;
                try {
                    if (!GUIGAME.getGame().isMoveValid(Workshop.getWorkshopInstance().getWorkshopid(), row - 1, que.get(GUI.currentPlayerIndex), Workshop.getWorkshopInstance().takenTile))
                        return;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    //TODO error handling
                    localGamePhase1(que.get(GUI.currentPlayerIndex));
                    GUI.currentPlayerIndex++;
                    GUI.currentPlayerIndex = GUI.currentPlayerIndex % GUIGAME.getGame().getNumberOfPlayers();
                    localGameNextTurn(GUI.currentPlayerIndex);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            else
                System.out.println("What are u doing?");
        };
    }

    public static int getRow() {
        return row;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
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
    public void updateScore1(int x){
       switch (x){
           case 2:
               String scoreStr1 = (GUIGAME.getGame().playersTables[0].getPointsString());
               String scoreStr2 = (GUIGAME.getGame().playersTables[x-1].getPointsString());
               score1.setText(scoreStr1);
               score2.setText(scoreStr2);
               break;
           case 3:
               scoreStr1 = (GUIGAME.getGame().playersTables[0].getPointsString());
               scoreStr2 = (GUIGAME.getGame().playersTables[1].getPointsString());
               String scoreStr3 = (GUIGAME.getGame().playersTables[x-1].getPointsString());
               score1.setText(scoreStr1);
               score2.setText(scoreStr2);
               score3.setText(scoreStr3);

               GUIGAME.getGame().playersTables[0].getPlayersBoard().updateScoreFor3Players();
               GUIGAME.getGame().playersTables[1].getPlayersBoard().updateScoreFor3Players();
               break;
           case 4:
               scoreStr1 = (GUIGAME.getGame().playersTables[0].getPointsString());
               scoreStr2 = (GUIGAME.getGame().playersTables[1].getPointsString());
               scoreStr3 = (GUIGAME.getGame().playersTables[2].getPointsString());
               String scoreStr4 = (GUIGAME.getGame().playersTables[x-1].getPointsString());
               score1.setText(scoreStr1);
               score2.setText(scoreStr2);
               score3.setText(scoreStr3);
               score4.setText(scoreStr4);
               GUIGAME.getGame().playersTables[0].getPlayersBoard().updateScoreFor4Players();
               GUIGAME.getGame().playersTables[1].getPlayersBoard().updateScoreFor4Players();
               GUIGAME.getGame().playersTables[2].getPlayersBoard().updateScoreFor4Players();
       }
    }
    public JButton getButton1() {
        return button1;
    }

    public JButton getButton2() {
        return button2;
    }

    public JButton getButton3() {
        return button3;
    }

    public JButton getButton4() {
        return button4;
    }

    public JButton getButton5() {
        return button5;
    }

    public JButton getButton6() {
        return button6;
    }

    public JButton getButton7() {
        return button7;
    }

    public JButton getButton8() {
        return button8;
    }

    public JButton getButton9() {
        return button9;
    }

    public JButton getButton11() {
        return button11;
    }

    public JButton getButton12() {
        return button12;
    }

    public JButton getButton13() {
        return button13;
    }

    public JButton getButton14() {
        return button14;
    }

    public JButton getButton10() {
        return button10;
    }

    public JButton getButton15() {
        return button15;
    }

    public JButton getButton16() {
        return button16;
    }

    public JButton getButton17() {
        return button17;
    }

    public JButton getButton18() {
        return button18;
    }

    public JButton getButton19() {
        return button19;
    }

    public JButton getButton20() {
        return button20;
    }
}
