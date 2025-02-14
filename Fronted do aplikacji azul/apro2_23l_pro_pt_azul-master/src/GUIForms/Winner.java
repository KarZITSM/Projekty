package GUIForms;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Winner {
    private JPanel winner;
    private JLabel winnername;
    private JButton menu;

    public JPanel getWinner() {
        return winner;
    }

    public JLabel getWinnername() {
        return winnername;
    }
    public Winner(){
        HelpfulMethodsGuiJava.createButton("img/return.png",200,100,menu);
        menu.addActionListener(getToMenu());
    }
    public ActionListener getToMenu(){
        return e-> {
            MainFrame mainFrame = new MainFrame();
            System.exit(0);
            GUI.mainSetUp();
            GUI.frame.add(mainFrame.getAZUL());
            winner.setVisible(false);
            mainFrame.getAZUL().setVisible(true);
        };
    }
}
