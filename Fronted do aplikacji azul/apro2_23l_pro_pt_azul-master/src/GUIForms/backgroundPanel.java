package GUIForms;

import javax.swing.*;
import java.awt.*;

public class backgroundPanel extends JPanel {
    public final ImageIcon backgroundImage;
    public backgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath);
        setLayout(new BorderLayout());
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int imageWidth = backgroundImage.getIconWidth();
            int imageHeight = backgroundImage.getIconHeight();
            double scaleX = (double) getWidth() / imageWidth;
            double scaleY = (double) getHeight() / imageHeight;
            double scale = Math.min(scaleX, scaleY);
            int scaledWidth = (int) (scale * imageWidth);
            int scaledHeight = (int) (scale * imageHeight);
            int x = (getWidth() - scaledWidth) / 2;
            int y = (getHeight() - scaledHeight) / 2;
            g.drawImage(backgroundImage.getImage(), x, y, scaledWidth, scaledHeight, null);
        }
    }
}