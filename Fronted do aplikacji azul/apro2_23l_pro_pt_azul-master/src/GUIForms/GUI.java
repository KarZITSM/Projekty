package GUIForms;

import javax.swing.*;
import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * The GUIForms.GUI class is responsible for displaying the current state
 * of the gameboard
 */
public class GUI {

    /**
     * Default constructor
     */
    public GUI() {
        mainframe();
    }

    public static WindowSize windowSize;

    public static int currentPlayerIndex;

    public static ArrayList<String> nameList = new ArrayList<>();

    public static final JFrame frame = new JFrame("azul");


    public static void mainSetUp() {
        frame.setSize(1600, 900);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void mainframe() {
        MainFrame mainFrame = new MainFrame();
        mainSetUp();
        frame.add(mainFrame.getAZUL());
    }


    public static void main(String[] args) {
        new GUI();
    }

    /**
     * Atributes of the GUIForms.GUI
     * (not filled so as not to impose concept)
     */
    private Attributes atributes;

    /**
     * Method that updates the current state of the gameboard
     */
    public void update() {
        // TODO implement here
    }


}