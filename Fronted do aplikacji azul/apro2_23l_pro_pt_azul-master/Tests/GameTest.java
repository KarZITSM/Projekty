import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void isFirstStageFinishedTest1() throws Exception {
        Game game1 = new Game(2, 0);
        for (int i = 0; i < game1.table.factories.length; i++) {
            for (int j = 0; j < 4; j++) {
                game1.table.factories[i].getContents()[j] = null;
            }
        }
        game1.table.center.clear();
        assertEquals(true, game1.isFirstStageFinished());
    }

    @Test
    public void isFirstStageFinishedTest2() throws Exception {
        Game game2 = new Game(2, 0);
        for (int i = 0; i < game2.table.factories.length; i++) {
            for (int j = 0; j < 4; j++) {
                game2.table.factories[i].getContents()[j] = null;
            }
        }
        game2.table.center.add(Tile.BLUE);
        assertEquals(false, game2.isFirstStageFinished());
    }

    @Test
    public void isFirstStageFinishedTest3() throws Exception {
        Game game3 = new Game(2, 0);
        for (int i = 0; i < game3.table.factories.length; i++) {
            for (int j = 0; j < 4; j++) {
                game3.table.factories[i].getContents()[j] = Tile.BLACK;
            }
        }
        game3.table.center.add(Tile.BLUE);
        assertEquals(false, game3.isFirstStageFinished());
    }

    @Test
    public void isFirstStageFinishedTest4() throws Exception {
        Game game4 = new Game(2, 0);
        for (int i = 0; i < game4.table.factories.length; i++) {
            for (int j = 0; j < 4; j++) {
                game4.table.factories[i].getContents()[j] = Tile.BLACK;
            }
        }
        game4.table.center.clear();
        assertEquals(false, game4.isFirstStageFinished());
    }

    @Test
    public void isGameFinishedTest1() throws Exception {
        Game game5 = new Game(2, 0);
        game5.playersTables[0] = new Player("xyz");
        game5.playersTables[0].wall.addTile(Tile.YELLOW, 3);
        game5.playersTables[0].wall.addTile(Tile.RED, 3);
        game5.playersTables[0].wall.addTile(Tile.BLACK, 3);
        game5.playersTables[0].wall.addTile(Tile.WHITE, 3);
        game5.playersTables[0].wall.addTile(Tile.BLUE, 3);
        assertEquals(true, game5.isGameFinished(0));
    }

    @Test
    public void isGameFinishedTest2() throws Exception {
        Game game5 = new Game(2, 0);
        game5.playersTables[0] = new Player("xyz");
        game5.playersTables[0].wall.addTile(Tile.YELLOW, 3);
        game5.playersTables[0].wall.addTile(Tile.RED, 3);
        game5.playersTables[0].wall.addTile(Tile.BLACK, 3);
        game5.playersTables[0].wall.addTile(Tile.WHITE, 3);
        assertEquals(false, game5.isGameFinished(0));
    }

    @Test
    public void returnWinnerTest() throws Exception {
        Game game6 = new Game(3, 0);
        game6.playersTables[0] = new Player("xyz");
        game6.playersTables[1] = new Player("abc");
        game6.playersTables[2] = new Player("klm");
        game6.playersTables[0].sumPoints(10);
        game6.playersTables[1].sumPoints(20);
        game6.playersTables[2].sumPoints(15);
        assertEquals(game6.playersTables[1].getName(), game6.returnWinner().getName());

    }

    @Test
    public void isMoveValidTest1() throws Exception {
        Game game7 = new Game(2, 1);
        assertTrue(game7.isMoveValid(9,5,0,Tile.BLACK));
    }

    @Test
    public void isMoveValidTest2() throws Exception {
        Game game8 = new Game(2, 1);
        assertTrue(game8.isMoveValid(9,4,0,Tile.FIRSTTILE));
    }

    @Test
    public void addToWall1() throws Exception {
        Game game9 = new Game(1, 1);
        game9.playersTables[0] = new Player("xyz");
        game9.playersTables[0].pattern.addToRow(1, Tile.BLUE, 2);
        game9.addToWall(0);
        assertEquals(true, game9.playersTables[0].wall.colourCheck(Tile.BLUE, 1));
    }

    @Test
    public void addToWall2() throws Exception {
        Game game10 = new Game(1, 1);
        game10.playersTables[0] = new Player("xyz");
        game10.playersTables[0].pattern.addToRow(2, Tile.YELLOW, 2);
        game10.addToWall(0);
        assertEquals(false, game10.playersTables[0].wall.colourCheck(Tile.BLUE, 2));
    }

    @Test
    public void subtractPointsFromFloorTest1() throws Exception {
        Game game11 = new Game(1, 1);
        game11.playersTables[0] = new Player("xyz");
        game11.playersTables[0].floor.add(0, Tile.BLUE);
        game11.subtractPointsFromFloor(0);
        assertEquals(-1, game11.playersTables[0].getPoints());
    }

    @Test
    public void subtractPointsFromFloorTest2() throws Exception {
        Game game12 = new Game(1, 1);
        game12.playersTables[0] = new Player("xyz");
        game12.playersTables[0].floor.add(0, Tile.BLUE);
        game12.playersTables[0].floor.add(1, Tile.YELLOW);
        game12.playersTables[0].floor.add(2, Tile.WHITE);
        game12.subtractPointsFromFloor(0);
        assertEquals(-4, game12.playersTables[0].getPoints());
    }

}