package Mechanics;

import GUIForms.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The game class handles all other classes
 * and operates on their objects
 */
public class GUIGAME implements Serializable {
    /**
     * Number of players playing
     */
    private static GUIGAME INSTANCE;
    public final int players;
    public static int first;
    static int numberOfPlayers;
    static boolean isEnd = false;
    static boolean isGameFinished = false;

    public static Client client;
    private static GUIGAME game;

    public static JPanel currentPanel;
    public static String rec1;

    public static ArrayList<Integer> que = new ArrayList<>();

    /**
     * Mechanics.Game mode
     * 1 - local
     * 0 - online
     */
    public final int mode;

    private static Player winnerPlayer;

    /**
     * Mechanics.Table - one instance per game
     */
    public final Table table;

    /**
     * Mechanics.Player class instances - each player is represented by one
     */
    public final Player[] playersTables;

    /**
     * Default constructor
     */
//    public static GUIGAME getINSTANCEOfGUIGAME(int numberOfPlayers, int mode) throws Exception {
//        if(INSTANCE == null){
//            INSTANCE = new GUIGAME(numberOfPlayers,mode);
//        }
//        return INSTANCE;
//    }

//    public static GUIGAME getINSTANCEGUIGAME(){
//        return INSTANCE;
//    }
    public GUIGAME(int numberOfPlayers, int mode) throws Exception {
        playersTables = new Player[numberOfPlayers];
        players = numberOfPlayers;
        table = new Table(players);
        this.mode = mode;
    }

    /**
     * Checking whether the conditions for ending the first stage of the game are met
     *
     * @return true if first stage of game is finished
     */
    public boolean isFirstStageFinished() {
        int i;
        for (i = 0; i < table.factories.length; i++) {
            if (!table.factories[i].isEmpty())
                return false;
        }
        return table.center.isEmpty();
    }


    /**
     * Checking whether the conditions for ending the game are met
     *
     * @return true if game is finished
     */
    public boolean isGameFinished(int indexOfPlayer) {
        for (int i = 0; i < 5; i++) {
            if (playersTables[indexOfPlayer].wall.IsRowFull(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method responsible for saving the current game
     */
    public void save(String path) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path + ".bin"))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method responsible for loading the game
     */
    public static GUIGAME load(String path) {
        GUIGAME game;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path + ".bin"))) {
            game = (GUIGAME) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    /**
     * Method for pointing out the winner
     *
     * @return The player who has won
     */
    public Player returnWinner() {
        Player temp = playersTables[0];
        for (Player player : playersTables) {
            if (temp.getPoints() < player.getPoints()) {
                temp = player;
            }
        }
        return temp;
    }

    /**
     * Method for getting the number of players
     *
     * @return Number of players
     */
    public int getNumberOfPlayers() {
        return players;
    }

    /**
     * Method to return the table of players
     *
     * @return Mechanics.Table of Mechanics.Player objects representing the players
     */
    public Player[] getPlayersTables() {
        return playersTables;
    }

    /**
     * Method for printing the factory
     */
    public void printFactory() {
        System.out.println();
        for (int i = 0; i < 2 * players + 1; i++) {
            Tile[] contents = table.factories[i].getContents();
            System.out.print((i + 1) + " Mechanics.Factory: ");
            for (int j = 0; j < 4; j++) {
                System.out.print(contents[j] + " ");
            }
            System.out.println();
        }
        // Printing what center contains
        System.out.print("10 Center: ");
        for (int i = 0; i < table.center.size(); i++)
            System.out.print(table.center.get(i) + " ");
        System.out.println();
    }

    /**
     * Method for checking if move is valid
     *
     * @param number       index of factory or center
     * @param row          index of row in pattern lines to which we want to add tile
     * @param playerNumber players number
     * @param tile         the colour of the tiles which we want to add
     * @return true if move is valid
     */
    public boolean isMoveValid(int number, int row, int playerNumber, Tile tile) {
        if (row == 5) {
            return true;
        }
        if (number == 9) {
            return (table.center.contains(tile) && ((playersTables[playerNumber].pattern.colours[row] == tile &&
                    playersTables[playerNumber].pattern.amounts[row] < (row + 1)) ||
                    playersTables[playerNumber].pattern.colours[row] == null));
        }
        return (playersTables[playerNumber].pattern.colours[row] == tile &&
                playersTables[playerNumber].pattern.amounts[row] < (row + 1)) ||
                playersTables[playerNumber].pattern.colours[row] == null;
    }

    /**
     * Method for finding the first player
     *
     * @return index of player who has the First tile or -1 if players do not have the First tile
     */
    public int findFirstPlayer() {
        for (int i = 0; i < players; i++) {
            if (playersTables[i].getFirst())
                return i;
        }
        return -1;
    }

    /**
     * Method for adding tiles to pattern line
     *
     * @param indexOfPlayer     index of current player
     * @param number            index of factory or center
     * @param tileToAdd         the colour of the tiles which we want to add
     * @param whereToPlaceTiles index of row in pattern lines to which we want to add tile
     */
    public void addTilesToPatternLines(int indexOfPlayer, Tile tileToAdd, int number, int whereToPlaceTiles) {
        if (whereToPlaceTiles == 5 && number != 9) {
            for (int i = 0; i < 4; i++) {
                if (table.factories[number].getContents()[i] == tileToAdd) {
                    if (playersTables[indexOfPlayer].floor.size() < 7)
                        playersTables[indexOfPlayer].floor.add(tileToAdd);
                    else
                        table.box.add(tileToAdd);
                } else
                    table.center.add(table.factories[number].getContents()[i]);
                table.factories[number].getContents()[i] = null;
            }
        } else if (whereToPlaceTiles == 5) {
            if (table.isPriorityTileInCenter()) {
                playersTables[indexOfPlayer].setFirst();
                playersTables[indexOfPlayer].floor.add(Tile.FIRSTTILE);
                table.center.remove(Tile.FIRSTTILE);
            }

            for (int i = 0; i < table.center.size(); i++) {
                if (table.center.get(i) == tileToAdd) {
                    if (playersTables[indexOfPlayer].floor.size() < 7)
                        playersTables[indexOfPlayer].floor.add(tileToAdd);
                    else
                        table.box.add(tileToAdd);
                }
            }
            while (table.center.contains(tileToAdd))
                table.center.remove(tileToAdd);
        } else if (whereToPlaceTiles < 5 && number != 9) {
            for (int i = 0; i < 4; i++) {
                if (table.factories[number].getContents()[i] == tileToAdd &&
                        !playersTables[indexOfPlayer].pattern.isRowFull(whereToPlaceTiles))
                    playersTables[indexOfPlayer].pattern.addToRow(whereToPlaceTiles, tileToAdd, 1);
                else if (table.factories[number].getContents()[i] == tileToAdd &&
                        playersTables[indexOfPlayer].pattern.isRowFull(whereToPlaceTiles)) {
                    if (playersTables[indexOfPlayer].floor.size() < 7)
                        playersTables[indexOfPlayer].floor.add(tileToAdd);
                    else
                        table.box.add(tileToAdd);
                } else
                    table.center.add(table.factories[number].getContents()[i]);
                table.factories[number].getContents()[i] = null;
            }
        } else if (whereToPlaceTiles < 5) {
            for (int i = 0; i < table.center.size(); i++) {
                if (table.isPriorityTileInCenter()) {
                    playersTables[indexOfPlayer].setFirst();
                    playersTables[indexOfPlayer].floor.add(Tile.FIRSTTILE);
                    table.center.remove(Tile.FIRSTTILE);
                }

                if (table.center.get(i) == tileToAdd &&
                        !playersTables[indexOfPlayer].pattern.isRowFull(whereToPlaceTiles)) {
                    playersTables[indexOfPlayer].pattern.addToRow(whereToPlaceTiles, tileToAdd, 1);
                } else if (table.center.get(i) == tileToAdd &&
                        playersTables[indexOfPlayer].pattern.isRowFull(whereToPlaceTiles)) {
                    if (playersTables[indexOfPlayer].floor.size() < 7)
                        playersTables[indexOfPlayer].floor.add(tileToAdd);
                    else
                        table.box.add(tileToAdd);
                }
            }
            while (table.center.contains(tileToAdd))
                table.center.remove(tileToAdd);
        }
    }

    /**
     * Method for adding tiles from pattern lines to wall
     *
     * @param indexOfPlayer index of current player
     */
    public void addToWall(int indexOfPlayer) {
        for (int i = 0; i < 5; i++) {
            if (playersTables[indexOfPlayer].pattern.isRowFull(i)) {
                playersTables[indexOfPlayer].sumPoints(playersTables[indexOfPlayer].wall.addTile(playersTables[indexOfPlayer].pattern.colours[i], i));
                for (int j = 1; j < playersTables[indexOfPlayer].pattern.amounts[i]; j++) {
                    table.box.add(playersTables[indexOfPlayer].pattern.colours[i]);
                }
                playersTables[indexOfPlayer].pattern.clearRow(i);
            }
        }
    }

    /**
     * Method for subtracting points for the tiles on the floor and clearing floor
     *
     * @param indexOfPlayer index of current player
     */
    public void subtractPointsFromFloor(int indexOfPlayer) {
        for (int i = 0; i < playersTables[indexOfPlayer].floor.size(); i++) {
            if (playersTables[indexOfPlayer].floor.get(i) != null) {
                if (i == 0 || i == 1)
                    playersTables[indexOfPlayer].sumPoints(-1);
                else if (i < 5) {
                    playersTables[indexOfPlayer].sumPoints(-2);
                } else {
                    playersTables[indexOfPlayer].sumPoints(-3);
                }
            } else
                break;
        }
        playersTables[indexOfPlayer].clearFloor();
    }



    private static final Color highlightColor = new Color(32, 255, 70);
    private static final Color basicColor = new Color(255, 255, 255);
    public static void highlightLabel(int player) throws Exception {
        bord Board = GUIGAME.getGame().getPlayersTables()[GUI.currentPlayerIndex].getPlayersBoard();
        for(int i = 0; i < NumberOfPlayers.getClickedNumberOfPlayers(); i++) {

            System.out.println();
            if(i  == player) {
                if(i == 0) {
                    Workshop.getWorkshopInstance().getPlayerName1().setForeground(highlightColor);
                    Board.getPlayerName1().setForeground(highlightColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName1().setForeground(highlightColor);
                }
                if(i == 1) {
                    Workshop.getWorkshopInstance().getPlayerName2().setForeground(highlightColor);
                    Board.getPlayerName2().setForeground(highlightColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName2().setForeground(highlightColor);
                }
                if(i == 2) {
                    Workshop.getWorkshopInstance().getPlayerName3().setForeground(highlightColor);
                    Board.getPlayerName3().setForeground(highlightColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName3().setForeground(highlightColor);
                }
                if(i == 3) {
                    Workshop.getWorkshopInstance().getPlayerName4().setForeground(highlightColor);
                    Board.getPlayerName4().setForeground(highlightColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName4().setForeground(highlightColor);
                }
            }
            else {
                if(i == 0) {
                    Workshop.getWorkshopInstance().getPlayerName1().setForeground(basicColor);
                    Board.getPlayerName1().setForeground(basicColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName1().setForeground(basicColor);
                }
                if(i == 1) {
                    Workshop.getWorkshopInstance().getPlayerName2().setForeground(basicColor);
                    Board.getPlayerName2().setForeground(basicColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName2().setForeground(basicColor);
                }
                if(i == 2) {
                    Workshop.getWorkshopInstance().getPlayerName3().setForeground(basicColor);
                    Board.getPlayerName3().setForeground(basicColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName3().setForeground(basicColor);
                }
                if(i == 3) {
                    Workshop.getWorkshopInstance().getPlayerName4().setForeground(basicColor);
                    Board.getPlayerName4().setForeground(basicColor);
                    FactoriesCenter.getFactoriesCenterINSTANCE().getPlayerName4().setForeground(basicColor);
                }
            }
        }

    }









    public static void localGameStart() throws Exception {
        numberOfPlayers = NumberOfPlayers.getClickedNumberOfPlayers();
        game = new GUIGAME(numberOfPlayers, 1);
        int index = 0;
        for (String x : GUI.nameList) {
            game.playersTables[index] = new Player(x);
            game.playersTables[index].getPlayersBoard().updateScore1(index+1);
            index++;
        }

        ArrayList<Integer> que = new ArrayList<>();
        // First starting player is chosen randomly
        Random rand = new Random();

        first = rand.nextInt(numberOfPlayers);
        Workshop workshop = Workshop.getWorkshopInstance();
        GUI.frame.add(workshop.getWorkshopPanel());

        localGameMainLoop();
    }

    public static void localGameNextTurn(int index) throws Exception {
        if(!isEnd){

            GUI.currentPlayerIndex = index;

            highlightLabel(index);

            Workshop workshop = Workshop.getWorkshopInstance();
            Workshop.getWorkshopInstance().setIfTileIsTaken();
            FactoriesCenter.getFactoriesCenterINSTANCE().setIfTileIsPicked();
            workshop.getWorkshopPanel().setVisible(true);
            for (Integer integer : que) {
                bord bord = GUIGAME.game.playersTables[integer].getPlayersBoard();
                bord.getFullbord().setVisible(false);
                bord.setTurnInfo();
            }

        }
        if(isEnd) {
            first = game.findFirstPlayer();
            for (Integer integer : que) {
                bord bord = GUIGAME.game.playersTables[integer].getPlayersBoard();
                bord.getFullbord().setVisible(false);
                bord.setTurnInfo();
            }
            que.clear();
            localGameEmptyFactory();
        }

    }


    public static void localGameMainLoop() throws Exception {
        if (!isGameFinished) {
            Workshop.getWorkshopInstance().refillWorkshop();
            for (int i = 0; i < numberOfPlayers; i++)
                que.add((first + i) % numberOfPlayers);
            isEnd = false;
            localGameNextTurn(0);
        }
        else {
            Winner winner = new Winner();
            GUI.frame.add(winner.getWinner());
            winner.getWinner().setVisible(true);
            winner.getWinnername().setText("Congratulations to player"+ "" +winnerPlayer.getName());
        }
    }

    public static void localGameEmptyFactory() throws Exception {
        for (int i = 0; i < numberOfPlayers; i++) {
            game.addToWall(i);
            game.subtractPointsFromFloor(i);
        }

        for(int i=0;i<game.playersTables.length;i++){
            Player current =  game.playersTables[i];
            bord BoardCurrent = current.getPlayersBoard();
            Wall currentWall = current.wall;
            boolean[][] filledArray = currentWall.filled;
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if(filledArray[j][k]){
                        BoardCurrent.wallmatrix[j][k].setIcon(HelpfulMethodsGuiJava
                                .getImageIconWithSize(currentWall.pattern[j][k].getImageName(),90,90));
                    }

                }
            }
        }



//        for (int i = 0; i<game.playersTables.length; i++){
//            bord Board = game.playersTables[i].getPlayersBoard();
//            Board.getButton1().
//        }
        int[] tab = {0,1,3,6,10};
        int[] tab2 = {1,2,3,4,5};
        for (int a = 0; a<game.playersTables.length; a++){
            bord Board = game.playersTables[a].getPlayersBoard();
            for (int i = 0; i < 5; i++){
                for(int j = game.playersTables[a].pattern.amounts[i]; j<tab2[i] ;j++){
                    game.playersTables[a].playersBoard.buttonsArray.get(tab[i]+j)
                            .setIcon(HelpfulMethodsGuiJava.getImageIconWithSize("img/notile.png",90,90));
                }
                for (JButton x : game.playersTables[a].playersBoard.floorArray) {
                    x.setIcon(HelpfulMethodsGuiJava.getImageIconWithSize("img/notile.png",90,90));
                }
            }
            switch (game.getNumberOfPlayers()){
                case 1:
                    break;
                case 2:
                    Board.updateScoreFor2Players();
                    break;
                case 3:
                    Board.updateScoreFor3Players();
                    break;
                case 4:
                    Board.updateScoreFor4Players();
                    break;
            }
        }
        switch (game.getNumberOfPlayers()){
            case 1:
                break;
            case 2:
                Workshop.getWorkshopInstance().updateScoreFor2Players();
                FactoriesCenter.getFactoriesCenterINSTANCE().updateScoreFor2Players();
                break;
            case 3:
                Workshop.getWorkshopInstance().updateScoreFor3Players();
                FactoriesCenter.getFactoriesCenterINSTANCE().updateScoreFor3Players();
                break;
            case 4:
                Workshop.getWorkshopInstance().updateScoreFor4Players();
                FactoriesCenter.getFactoriesCenterINSTANCE().updateScoreFor4Players();
                break;
        }

        game.table.refillFactories();

        for (int i = 0; i < numberOfPlayers; i++)
            if (game.isGameFinished(i)){
                int temp = 0;
                for (int j = 0; j < numberOfPlayers; j++) {
                    if(temp < game.playersTables[j].getPoints()){
                        winnerPlayer =  game.playersTables[j];
                        temp = game.playersTables[j].getPoints();
                    }
                }
                isGameFinished = true;
            }



        localGameMainLoop();
    }

    private void rowFill(int row){

    }

    public static void localGamePhase1(int order) throws Exception {
                //System.out.println(game.table.bag.size());
                System.out.println("Mechanics.Player : " + (order+1));
                int number;
                String tiles;
                int whereToPlaceTiles;
                Tile tileToAdd;

                // READING FROM KEYBOARD
                do{
                    game.printFactory();
                    System.out.println();
                    System.out.println();

                    //TODO Tura gracza wewnątrz GUI

                    System.out.println("Choose factory or center from which you want to take tiles");
                    //WorkshopID
                    //number = reader.nextInt() - 1;
                    Workshop workshop = Workshop.getWorkshopInstance();
                    number = workshop.getWorkshopid();


                    System.out.println("Choose tile which you want to take from the factory");
                    //tile button -> który przycisk
                    //tiles = reader.next();

                    System.out.println("Choose where you want to add the tiles, 1-5 for pattern lines, 6 for floor");
                    //Sprytna metoda na rząd linii
                    //whereToPlaceTiles = reader.nextInt() - 1;
                    whereToPlaceTiles = bord.getRow() - 1;

                    tileToAdd = workshop.takenTile;


                    if(!game.table.isColourInFactory(tileToAdd, number) || !game.isMoveValid(number, whereToPlaceTiles, order, tileToAdd))
                        System.out.println("Chosen tile doesn't exist in this factory, please choose again");

                } while (!game.table.isColourInFactory(tileToAdd, number) || !game.isMoveValid(number, whereToPlaceTiles, order, tileToAdd));
                // END

                game.addTilesToPatternLines(order, tileToAdd, number, whereToPlaceTiles);

                int[] tab = {0,1,3,6,10};


                for (int i = 0; i < 5; i++){
                    for(int j = 0; j < game.playersTables[que.get(GUI.currentPlayerIndex)].pattern.amounts[i]; j++){
                        game.playersTables[que.get(GUI.currentPlayerIndex)].playersBoard.buttonsArray.get(tab[i]+j)
                                .setIcon(HelpfulMethodsGuiJava.getImageIconWithSize(game.playersTables[que.get(GUI.currentPlayerIndex)].pattern.colours[i].getImageName(),90,90));
                    }
                }

                game.playersTables[order].pattern.printPatternLine();
                System.out.println(numberOfPlayers);
                int index = 0;
                for(Tile x: game.playersTables[que.get(GUI.currentPlayerIndex)].floor){
                    game.playersTables[que.get(GUI.currentPlayerIndex)].playersBoard.floorArray.get(index)
                            .setIcon(HelpfulMethodsGuiJava.getImageIconWithSize(x.getImageName(),90,90));
                    index++;
                }

                game.playersTables[que.get(GUI.currentPlayerIndex)].printFloor();



                isEnd = game.isFirstStageFinished();
    }

    private static void localGame() throws Exception {

        NumberOfPlayers numberOfPlayersForm = new NumberOfPlayers();
        GUI.frame.add(numberOfPlayersForm.getPLAYERS());
        numberOfPlayersForm.getPLAYERS().setVisible(true);
        currentPanel.setVisible(false);

        /*
        while(!isGameFinished){
            for(int i = 0; i<numberOfPlayers; i++)
                que.add((first+i)%numberOfPlayers);

            isEnd = false;
            while(!isEnd) {
                for(int order: que) {
                    //System.out.println(game.table.bag.size());
                    System.out.println("Mechanics.Player : " + (order+1));
                    int number;
                    String tiles;
                    int whereToPlaceTiles;
                    Tile tileToAdd;




                    isEnd = game.isFirstStageFinished();
                    if(isEnd) {
                        first = game.findFirstPlayer();
                        que.clear();
                        break;
                    }
                }
            }


            System.out.println(game.table.bag);
            game.playersTables[0].wall.printWall();
            game.table.refillFactories();

            for(int i=0; i<numberOfPlayers; i++)
                if(game.isGameFinished(i))
                    isGameFinished = true;
        }
         */
    }

    public static void choosePlayerNumber(int number){
        numberOfPlayers = number;
        client.sendData(String.valueOf(numberOfPlayers));
    }



    private static void onlineGame() throws Exception {

        //SOCKET
        client = new Client("localhost", 12345);
        client.start();

        rec1 = client.receiveData();
        //System.out.println(rec1);
        if(rec1.charAt(0) == '0') {
            NumberOfPlayers numberOfPlayers = new NumberOfPlayers();
            GUI.frame.add(numberOfPlayers.getPLAYERS());
            numberOfPlayers.getPLAYERS().setVisible(true);
            currentPanel.setVisible(false);
        } else {
            numberOfPlayers = Integer.parseInt(rec1.substring(1,2));
            //TODO przekieruj do czekania na gre (BOARD z napisem Waiting)
            client.sendData("waiting");
        }/*
        int id = Integer.parseInt(rec1.substring(0,1));
        System.out.println("Players: " + (id + 1) + "/" + numberOfPlayers);
        System.out.println("CONNECTED TO SERVER, WAITING FOR PLAYERS");
        for(int i = id+1; i<numberOfPlayers-1; i++) {
            while(!Objects.equals(client.receiveData(), String.valueOf(i))){Thread.onSpinWait();}
            System.out.println("Players: " + (i + 1) + "/" + numberOfPlayers);
        }
        while(!Objects.equals(client.receiveData(), "START")){Thread.onSpinWait();}
        System.out.println("Players: " + numberOfPlayers + "/" + numberOfPlayers);
        System.out.println("STARTED");
        // SOCKET

        // setup Mechanics.Table and factories
        GUIGAME game = new GUIGAME(numberOfPlayers, 2);
        ArrayList<Integer> que = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++){
            String playerName = "playerName";
            game.playersTables[i] = new Player(playerName);
        }
        int first = 0;

        while(!isGameFinished){
            for(int i = 0; i<numberOfPlayers; i++)
                que.add((first+i)%numberOfPlayers);

            while(!isEnd) {
                for(int order: que) {
                    //System.out.println(game.table.bag.size());
                    System.out.println("Mechanics.Player : " + (order+1));
                    int number;
                    String tiles;
                    int whereToPlaceTiles;
                    Tile tileToAdd;

                    if(order == id) {
                        game.playersTables[order].pattern.printPatternLine();
                        // READING FROM KEYBOARD
                        do {
                            game.printFactory();
                            System.out.println();
                            System.out.println();

                            System.out.println("Choose factory or center from which you want to take tiles");
                            number = reader.nextInt() - 1;

                            System.out.println("Choose tile which you want to take from the factory");
                            tiles = reader.next();

                            System.out.println("Choose where you want to add the tiles, 1-5 for pattern lines, 6 for floor");
                            whereToPlaceTiles = reader.nextInt() - 1;

                            tileToAdd = switch (tiles.toUpperCase()) {
                                case "BLACK" -> Tile.BLACK;
                                case "WHITE" -> Tile.WHITE;
                                case "BLUE" -> Tile.BLUE;
                                case "YELLOW" -> Tile.YELLOW;
                                case "RED" -> Tile.RED;
                                default -> null;
                            };
                            if (!game.table.isColourInFactory(tileToAdd, number) || !game.isMoveValid(number, whereToPlaceTiles, order, tileToAdd))
                                System.out.println("Chosen tile doesn't exist in this factory, please choose again");
                        } while (!game.table.isColourInFactory(tileToAdd, number) || !game.isMoveValid(number, whereToPlaceTiles, order, tileToAdd));

                        client.sendData(String.valueOf(order) + number + whereToPlaceTiles + tiles);
                        // END
                    } else {
                        String temp = client.receiveData();
                        while(temp == null) temp = client.receiveData();
                        number = Integer.parseInt(temp.substring(1,2));
                        whereToPlaceTiles = Integer.parseInt(temp.substring(2,3));
                        tiles = temp.substring(3);
                        tileToAdd = switch (tiles.toUpperCase()) {
                            case "BLACK" -> Tile.BLACK;
                            case "WHITE" -> Tile.WHITE;
                            case "BLUE" -> Tile.BLUE;
                            case "YELLOW" -> Tile.YELLOW;
                            case "RED" -> Tile.RED;
                            default -> null;
                        };
                    }
                    game.addTilesToPatternLines(order, tileToAdd, number, whereToPlaceTiles);
                    game.playersTables[order].pattern.printPatternLine();
                    game.playersTables[order].printFloor();

                    isEnd = game.isFirstStageFinished();
                    if(isEnd) {
                        first = game.findFirstPlayer();
                        que.clear();
                        break;
                    }
                }
            }
            for(int i=0; i<numberOfPlayers; i++){
                game.addToWall(i);
                game.subtractPointsFromFloor(i);
            }

            System.out.println(game.table.bag);
            game.playersTables[0].wall.printWall();
            game.table.refillFactories();

            for(int i=0; i<numberOfPlayers; i++)
                if(game.isGameFinished(i))
                    isGameFinished = true;
        }
    */
        client.end();
    }

    public static void mainGame(int mode) throws Exception {
        if(mode == 1) { // SINGLE PLAYER
            localGame();
        } else { // MULTIPLAYER
            onlineGame();
        }

        // SAVING AND LOADING GAME
        // game.save("test");
        // Mechanics.Game g = Mechanics.Game.load("test");
    }

    public void createPlayers(){
        int i = 0;
        for(String x: GUI.nameList){

        }
    }

    public static GUIGAME getGame() {
        return game;
    }
}