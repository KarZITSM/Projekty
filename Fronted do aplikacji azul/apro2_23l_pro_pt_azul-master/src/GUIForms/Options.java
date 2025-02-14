package GUIForms;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Options {
    private JPanel panel1;
    private JButton smallButton;
    private JButton mediumButton;
    private JButton bigButton;
    private JButton returnButton;

    public  ActionListener setWidthHeight(int width, int height){
        return e -> {
            GUI.frame.setSize(width,height);
            switch (width) {
                case 1280 -> GUI.windowSize = WindowSize.SMALL;
                case 1600 -> GUI.windowSize = WindowSize.MEDIUM;
                case 1904 -> GUI.windowSize = WindowSize.BIG;
            }
            HelpfulMethodsGuiJava.createButton("img/small.png", 291,150, smallButton);
            HelpfulMethodsGuiJava.createButton("img/medium.png", 291,150, mediumButton);
            HelpfulMethodsGuiJava.createButton("img/big.png", 291,150, bigButton);
            HelpfulMethodsGuiJava.createButton("img/return.png", 291,150, returnButton);
        };
    }
    public ActionListener getBackToMenu(){
        return e -> {
            //Options options = (Options) e.getSource();
            panel1.setVisible(false);
            MainFrame azul = new MainFrame();
            azul.getAZUL().setVisible(true);
        };
    }

    public JPanel getPanel1() {
        return panel1;
    }

    Options(){
        HelpfulMethodsGuiJava.createButton("img/small.png", 291,150, smallButton);
        HelpfulMethodsGuiJava.createButton("img/medium.png", 291,150, mediumButton);
        HelpfulMethodsGuiJava.createButton("img/big.png", 291,150, bigButton);
        HelpfulMethodsGuiJava.createButton("img/return.png", 291,150, returnButton);

        smallButton.addActionListener(setWidthHeight(1280, 720));
        mediumButton.addActionListener(setWidthHeight(1600, 900));
        bigButton.addActionListener(setWidthHeight(1904, 1072));
        returnButton.addActionListener(getBackToMenu());

        smallButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        smallButton,
                        "img/small.png",
                        "img/smallHover.png",
                        291,
                        150));

        mediumButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        mediumButton,
                        "img/medium.png",
                        "img/mediumHover.png",
                        291,
                        150));

        bigButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        bigButton,
                        "img/big.png",
                        "img/bigHover.png",
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
