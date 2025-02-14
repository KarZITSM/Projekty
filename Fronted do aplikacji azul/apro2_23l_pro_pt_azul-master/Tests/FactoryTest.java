import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {

    Factory factory = new Factory();
    Tile[] tiles = new Tile[4];

    @Test
    public void addTilesTest1(){
        tiles[0] = Tile.BLACK;
        tiles[1] = Tile.WHITE;
        tiles[2] = Tile.YELLOW;
        tiles[3] = Tile.BLUE;
        factory.addTiles(tiles);
        assertEquals("[BLACK, WHITE, YELLOW, BLUE]", Arrays.toString(factory.getContents()));
    }

    @Test
    public void addTilesTest2(){
        tiles[0] = Tile.BLUE;
        tiles[1] = Tile.RED;
        tiles[2] = Tile.YELLOW;
        factory.addTiles(tiles);
        tiles[3] = Tile.BLACK;
        factory.addTiles(tiles);
        assertEquals("[BLUE, RED, YELLOW, BLACK]", Arrays.toString(factory.getContents()));
    }

    @Test
    public void addTilesTest3(){
        factory.addTiles(tiles);
        assertEquals("[null, null, null, null]", Arrays.toString(factory.getContents()));
    }

    @Test
    public void isEmptyTest1(){
        tiles[0] = Tile.BLACK;
        tiles[1] = Tile.BLUE;
        tiles[2] = Tile.RED;
        tiles[3] = Tile.WHITE;
        factory.addTiles(tiles);
        assertEquals(false, factory.isEmpty() );
    }

    @Test
    public void isEmptyTest2(){
        assertEquals(true, factory.isEmpty() );
    }

    @Test
    public void isEmptyTest3(){
        tiles[0] = Tile.BLACK;
        tiles[1] = Tile.BLUE;
        tiles[2] = Tile.RED;
        tiles[3] = null;
        factory.addTiles(tiles);
        assertEquals(false, factory.isEmpty() );
    }

    @Test
    public void removeTest1(){
        tiles[0] = Tile.BLUE;
        tiles[1] = Tile.RED;
        tiles[2] = Tile.YELLOW;
        tiles[3] = Tile.BLACK;
        factory.addTiles(tiles);
        factory.remove(Tile.YELLOW);
        assertEquals("[BLUE, RED, null, BLACK]", Arrays.toString(factory.getContents()));
    }

    @Test
    public void removeTest2(){
        tiles[0] = Tile.WHITE;
        tiles[1] = Tile.RED;
        tiles[2] = Tile.YELLOW;
        tiles[3] = Tile.BLUE;
        factory.addTiles(tiles);
        factory.remove(Tile.WHITE);
        factory.remove(Tile.RED);
        assertEquals("[null, null, YELLOW, BLUE]", Arrays.toString(factory.getContents()));
    }

    @Test
    public void removeTest3(){
        tiles[0] = Tile.WHITE;
        tiles[1] = Tile.RED;
        tiles[2] = Tile.BLUE;
        tiles[3] = Tile.BLACK;
        factory.addTiles(tiles);
        factory.remove(Tile.YELLOW);
        assertEquals("[WHITE, RED, BLUE, BLACK]", Arrays.toString(factory.getContents()));
    }
}