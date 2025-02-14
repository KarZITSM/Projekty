package GUIForms;

import Mechanics.GUIGAME;

import javax.swing.*;
import java.awt.*;

public class NumberOfPlayers {
    private JPanel PLAYERS;
    private JButton a4Button;
    private JButton a3Button;
    private JButton a2Button;
    private JButton a1Button;
    private JButton returnButton;
    private JLabel chosenumberofplayers;
    private static int ClickedNumberOfPlayers;

    public static int getClickedNumberOfPlayers() {
        return ClickedNumberOfPlayers;
    }

    public JPanel getPLAYERS() {
        return PLAYERS;
    }

    public NumberOfPlayers() {
        chosenumberofplayers.setForeground(Color.WHITE);
        chosenumberofplayers.setBackground(Color.decode("#45678B"));
        chosenumberofplayers.setFont(new Font("Georgia", Font.PLAIN, 40));
        //GUI.frame.add(PLAYERS);
        //PLAYERS.setVisible(true);

        /**
         * ważne - zakomentowane są GUIGAME.choosePlayerNumber(), ze względu na brak dokończonej metody
         * w klasie GUIGAME
         */
        // TODO disclaimer nad ^^^^
        a1Button.addActionListener(e -> {//GUIGAME.choosePlayerNumber(1);
            ClickedNumberOfPlayers=1;
            PLAYERS.setVisible(false);
            ChoosePlayerNames names = null;
            try {
                names = new ChoosePlayerNames();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            GUI.frame.add(names.getPanel1());
            names.getPanel1().setVisible(true);

        });
        a2Button.addActionListener(e -> {//GUIGAME.choosePlayerNumber(2);
            ClickedNumberOfPlayers=2;
            PLAYERS.setVisible(false);
            ChoosePlayerNames names = null;
            try {
                names = new ChoosePlayerNames();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            GUI.frame.add(names.getPanel1());
            names.getPanel1().setVisible(true);
        });
        a3Button.addActionListener(e -> {//GUIGAME.choosePlayerNumber(3);
            ClickedNumberOfPlayers=3;
            PLAYERS.setVisible(false);
            ChoosePlayerNames names = null;
            try {
                names = new ChoosePlayerNames();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            GUI.frame.add(names.getPanel1());
            names.getPanel1().setVisible(true);
        });
        a4Button.addActionListener(e -> {//GUIGAME.choosePlayerNumber(4);
            PLAYERS.setVisible(false);
            ClickedNumberOfPlayers=4;
            ChoosePlayerNames names = null;
            try {
                names = new ChoosePlayerNames();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            GUI.frame.add(names.getPanel1());
            names.getPanel1().setVisible(true);
        });
        returnButton.addActionListener(e -> {
            MainFrame azulMenu = new MainFrame();
            azulMenu.AZUL.setVisible(true);
            getPLAYERS().setVisible(false);
        });

        HelpfulMethodsGuiJava.createButton("img/oneHover.png", 291,150, a1Button);
        HelpfulMethodsGuiJava.createButton("img/two.png", 291,150, a2Button);
        HelpfulMethodsGuiJava.createButton("img/three.png", 291,150, a3Button);
        HelpfulMethodsGuiJava.createButton("img/four.png", 291,150, a4Button);
        HelpfulMethodsGuiJava.createButton("img/return.png", 291,150, returnButton);


        a1Button.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        a1Button,
                        "img/oneHover.png",
                        "img/one.png",
                        291,
                        150));

        a2Button.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        a2Button,
                        "img/two.png",
                        "img/twoHover.png",
                        291,
                        150));

        a3Button.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        a3Button,
                        "img/three.png",
                        "img/threeHover.png",
                        291,
                        150));

        a4Button.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        a4Button,
                        "img/four.png",
                        "img/fourHover.png",
                        291,
                        150));

        returnButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        returnButton,
                        "img/return.png",
                        "img/returnHover.png",
                        291,
                        150));
    }

}
