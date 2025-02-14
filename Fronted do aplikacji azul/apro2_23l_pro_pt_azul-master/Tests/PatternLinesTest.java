import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class PatternLinesTest {
    @Test
    void addToRowTest(){
        Tile[][] test = new Tile[5][5];
        for (int i = 0; i < 3; i++)
            test[3][i] = Tile.BLACK;
        PatternLines patternLines = new PatternLines();
        patternLines.addToRow(3,Tile.BLACK,3);
   //     assertArrayEquals(patternLines.rows, test);
    }
    @Test
    void addToRowTest2(){
        Tile[][] test = new Tile[5][5];
        for (int i = 0; i < 5; i++)
            test[4][i] = Tile.WHITE;
        PatternLines patternLines = new PatternLines();
        patternLines.addToRow(4,Tile.WHITE,5);
       // assertArrayEquals(patternLines.rows, test);
    }
    @Test
    void isRowFullTest(){
        PatternLines patternLines = new PatternLines();
        assertFalse(patternLines.isRowFull(4));
    }
    @Test
    void isRowFullTest2(){
        PatternLines patternLines = new PatternLines();
        patternLines.addToRow(0, Tile.WHITE,1);
        assertTrue(patternLines.isRowFull(0));
    }
}