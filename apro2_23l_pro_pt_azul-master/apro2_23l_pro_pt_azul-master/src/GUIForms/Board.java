package GUIForms;

import javax.swing.*;

public class Board {

    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JButton button15;
    private JButton button16;
    private JButton button17;
    private JButton button18;
    private JButton button19;
    private JButton button20;
    private JButton button5;
    private JButton button21;
    private JLabel l1;
    private JLabel l21;
    private JLabel l31;
    private JLabel l41;
    private JLabel l51;
    private JLabel l2;
    private JLabel l22;
    private JLabel l23;
    private JLabel l24;
    private JLabel l25;
    private JLabel l3;
    private JLabel l32;
    private JLabel l33;
    private JLabel l34;
    private JLabel l35;
    private JLabel l4;
    private JLabel l42;
    private JLabel l43;
    private JLabel l44;
    private JLabel l45;
    private JLabel l5;
    private JLabel l52;
    private JLabel l53;
    private JLabel l54;
    private JLabel l55;
    private JLabel[] tiles = new JLabel[25];
    private JPanel Board;
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

    Board(){
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button2);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button3);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button5);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button6);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button7);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button8);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button9);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button10);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button11);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button12);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button13);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button14);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button15);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button16);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button17);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button18);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button19);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button20);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button21);
        HelpfulMethodsGuiJava.createButton("img/notile.png",80,80,button4);

        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 80,80, l1);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 80,80, l21);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 80,80, l31);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 80,80, l41);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 80,80, l51);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 80,80, l2);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 80,80, l22);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 80,80, l32);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 80,80, l42);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 80,80, l52);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 80,80, l3);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 80,80, l23);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 80,80, l33);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 80,80, l43);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 80,80, l53);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 80,80, l4);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 80,80, l24);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 80,80, l34);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 80,80, l44);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 80,80, l54);
        HelpfulMethodsGuiJava.createLabel("img/yellow_notile.png", 80,80, l5);
        HelpfulMethodsGuiJava.createLabel("img/red_notile.png", 80,80, l25);
        HelpfulMethodsGuiJava.createLabel("img/black_notile.png", 80,80, l35);
        HelpfulMethodsGuiJava.createLabel("img/white_notile.png", 80,80, l43);
        HelpfulMethodsGuiJava.createLabel("img/blue_notile.png", 80,80, l55);


    }

    public JPanel getBoard() {
        return Board;
    }
}
