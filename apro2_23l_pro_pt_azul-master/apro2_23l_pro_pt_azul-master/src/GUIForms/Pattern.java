package GUIForms;

import javax.swing.*;
import java.util.ArrayList;

public class Pattern {
    private JPanel wall;

    public JPanel getWall() {
        return wall;
    }

    private JButton button1;
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
    private JPanel pattern;
    private JLabel tile6;
    private JLabel tile11;
    private JLabel tile1;
    private JLabel tile9;
    private JLabel tile8;
    private JLabel tile5;
    private JLabel tile10;
    private JLabel tile7;
    private JLabel tile4;
    private JLabel tile3;
    private JLabel tile2;
    private JLabel tile14;
    private JLabel tile15;
    private JLabel tile23;
    private JLabel tile17;
    private JLabel tile12;
    private JLabel tile13;
    private JLabel tile18;
    private JLabel tile19;
    private JLabel tile20;
    private JLabel tile25;
    private JLabel tile24;
    private JLabel tile22;
    private JLabel tile16;
    private JLabel tile21;
    private JPanel wala;
    private JButton button22;
    private JButton button23;
    private JButton button24;
    private JButton button25;
    ArrayList<JButton> tilebuttons = new ArrayList<>();
    public Pattern(){
        tilebuttons.add(button1);
        tilebuttons.add(button2);
        tilebuttons.add(button3);
        tilebuttons.add(button4);
        tilebuttons.add(button5);
        tilebuttons.add(button6);
        tilebuttons.add(button7);
        tilebuttons.add(button8);
        tilebuttons.add(button9);
        tilebuttons.add(button10);
        tilebuttons.add(button11);
        tilebuttons.add(button12);
        tilebuttons.add(button13);
        tilebuttons.add(button14);
        tilebuttons.add(button15);
        tilebuttons.add(button16);
        tilebuttons.add(button17);
        tilebuttons.add(button18);
        tilebuttons.add(button19);
        tilebuttons.add(button20);
        tilebuttons.add(button21);
        tilebuttons.add(button22);
        tilebuttons.add(button23);
        tilebuttons.add(button24);
        tilebuttons.add(button25);
        for(JButton button: tilebuttons){
            HelpfulMethodsGuiJava.createButton("img/notile.png", 50,50,button);
        }
    }

    public JPanel getPattern() {
        return pattern;
    }
}
