package GUIForms;

import Mechanics.GUIGAME;
import Mechanics.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChoosePlayerNames {
    private JPanel panel1;
    private JTextField textField1;
    private JButton confirmButton;
    private JLabel playerName1;
    private JLabel playerName2;
    private JLabel playerName3;
    private JLabel playerName4;
    private JPanel ScoreBoard;
    private JLabel score1;
    private JLabel score2;
    private JLabel score3;
    private JLabel score4;
    private JLabel scoreboard;
    private static int numberOfNames = 0;
    private Player[] names = new Player[NumberOfPlayers.getClickedNumberOfPlayers()];


    public JPanel getPanel1() {
        return panel1;
    }

    public void setTextField1(JTextField textField1) {
        this.textField1 = textField1;
        textField1.setFont((new Font("Georgia", Font.PLAIN, 18)));
    }

    public ChoosePlayerNames() throws Exception {
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
        scoreboard.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize("img/scoreboard.png",339,90));
        HelpfulMethodsGuiJava.createButton("img/confirm.png",291,150,confirmButton);
        panel1.setVisible(true);
        ScoreBoard.setVisible(true);
            confirmButton.addActionListener(e -> {
                textField1.getText();
                System.out.println(textField1.getText());
                String name = (textField1.getText());

                textField1.setText(null);
                if(numberOfNames==0){
                    playerName1.setText(name);

                } else if (numberOfNames==1) {
                    playerName2.setText(name);
                } else if (numberOfNames==2) {
                    playerName3.setText(name);
                } else {
                    playerName4.setText(name);
                }
                GUI.nameList.add(name);
                numberOfNames++;

              //  System.out.println(numberOfNames);
               // System.out.println(NumberOfPlayers.getClickedNumberOfPlayers());

                if (numberOfNames == NumberOfPlayers.getClickedNumberOfPlayers()) {
                    System.out.println("wchodzi");
                    Workshop workshop;
                    try {
                        GUIGAME.localGameStart();
                        //workshop = Workshop.getWorkshopInstance();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    //GUI.frame.add(workshop.getWorkshopPanel());
                    //GUI.setWorkshop(workshop);
                    panel1.setVisible(false);


                }
            });
        if(MainFrame.getIsGameOnline()==1) {
            if (numberOfNames==1){
                //TODO należy utworzyć sesje gry, tzn jakiś panel
            }
        }
        else if (numberOfNames == NumberOfPlayers.getClickedNumberOfPlayers()) {
            Workshop workshop = Workshop.getWorkshopInstance();
            GUI.frame.add(workshop.getWorkshopPanel());
            panel1.setVisible(false);
        }


        confirmButton.addMouseListener(
                HelpfulMethodsGuiJava.hoverEvents(
                        confirmButton,
                        "img/confirm.png",
                        "img/confirmHover.png",
                        291,
                        150));

        // Add any necessary code for setting up the GUI, such as creating the panel and text field
        // and adding them to the frame.
    }

    public static int getNumberOfNames() {
        return numberOfNames;
    }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if ("Confirm".equals(e.getActionCommand())) {  // Check action command of the event
//            System.out.println("Welcome " + textField1.getText());
//            // button.setEnabled(false);
//            // textField.setEditable(false);
//        }
//    }
}
