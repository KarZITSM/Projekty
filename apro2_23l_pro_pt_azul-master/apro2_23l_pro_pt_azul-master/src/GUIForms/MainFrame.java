package GUIForms;

import Mechanics.GUIGAME;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private JButton SETTINGSButton;
    private JButton ONLINEButton;
    private JButton LOCALButton;
    public JPanel AZUL;
    private JLabel logoLabel;
    private static int isGameOnline=0;

    public static int getIsGameOnline() {
        return isGameOnline;
    }

    private static int init =0;

    public JPanel getAZUL() {
        return AZUL;
    }

    public static void createButton(String path, int width, int height, JButton current){
        current.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize(path, width, height));
        current.setBorder(BorderFactory.createEmptyBorder());
        current.setOpaque(false);
        current.setContentAreaFilled(false);
        current.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public MainFrame() {
        logoLabel.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize("img/logo.png", 600, 320));

        MainFrame.createButton("img/localButton.png", 291,150, LOCALButton);
        MainFrame.createButton("img/onlineButton.png", 291,150, ONLINEButton);
        MainFrame.createButton("img/settingsButton.png", 291,150, SETTINGSButton);


        GUI.frame.add(AZUL);
        SETTINGSButton.addActionListener(e -> {
            Options options = new Options();
            GUI.frame.add(options.getPanel1());
            AZUL.setVisible(false);
        });
        ONLINEButton.addActionListener(e->{
            if(init ==0){
                try {
                    GUIGAME.currentPanel = AZUL;
                    isGameOnline=1;
                    GUIGAME.mainGame(2);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        LOCALButton.addActionListener(e->{
                try {
                    GUIGAME.currentPanel = AZUL;
                    GUIGAME.mainGame(1);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
        });

        SETTINGSButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        SETTINGSButton,
                        "img/settingsButton.png",
                        "img/settingsButtonHover.png",
                        291,
                        150));

        LOCALButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        LOCALButton,
                        "img/localButton.png",
                        "img/localButtonHover.png",
                        291,
                        150));

        ONLINEButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        ONLINEButton,
                        "img/onlineButton.png",
                        "img/onlineButtonHover.png",
                        291,
                        150));
    }
}
