package GUIForms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HelpfulMethodsGuiJava {
    public static ImageIcon getImageIconWithSize(String path, int width, int height){
        double multiplier = 1;
        if(GUI.windowSize == WindowSize.SMALL){
            multiplier = 0.8;
        }
        else if(GUI.windowSize == WindowSize.BIG){
            multiplier = 1.2;
        }
        ImageIcon imageIcon = new ImageIcon(path);
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) (width * multiplier), (int) (height * multiplier),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newimg);
    }

    public static MouseAdapter hoverEvents(JButton button, String pathHovered, String pathDefault, int width, int height) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                button.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize(pathDefault, width, height));
            }
            @Override
            public void mouseExited(MouseEvent me){
                button.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize(pathHovered, width, height));
            }
        };
    }
    public static JButton createButton(String path, int width, int height, JButton current){
        current.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize(path, width, height));
        current.setBorder(BorderFactory.createEmptyBorder());
        current.setOpaque(false);
        current.setContentAreaFilled(false);
        current.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return current;
    }

    public static JLabel createLabel(String path, int width, int height, JLabel current){
        current.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize(path, width, height));
        current.setBorder(BorderFactory.createEmptyBorder());
        current.setOpaque(false);
        //current.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return current;
    }
}
