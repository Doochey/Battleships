package battleships;

import Battleships.BModel;
import java.io.File;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Connor
 */
public class BModelTest {
    
  
    /**
     * First loads a game with no ship at 1,1 and hits 1,1
     * then loads a game with ship at 1,1 and hits 1,1
     * if load is successful, hitting 1,1 after load
     * should return true
     */
    @Test
    public void test1() {
        System.out.println("Test 1");
        File file = new File("save_game_test_2.txt"); 
        BModel instance = new BModel();
        instance.loadGame(file);
        boolean result1 = instance.hitShip(1,1);
        boolean  expResult1 = false;
        assertEquals(result1, expResult1);
        file = new File("save_game_test.txt");
        instance.loadGame(file);
        boolean result2 = instance.hitShip(1, 1);
        boolean expResult2 = true;
        assertEquals(result2, expResult2);
        assertEquals(!result1, result2);
    }

    /**
     * Loads game with ship at 1,1
     * Gets the gameBoard value at 1,1 expected to be 'S'
     * hits ship at 1,1 this should change the value of 1,1 to 'V'
     */
    @Test
    public void test2() {
        System.out.println("Test 2");
        BModel instance = new BModel();
        File file = new File("save_game_test.txt"); 
        instance.loadGame(file);
        char expResult1 = 'S';
        char result1 = instance.getGameBoard()[1][1];
        assertEquals(result1, expResult1);
        instance.hitShip(1, 1);
        char expResult2 = 'V';
        char result2 = instance.getGameBoard()[1][1];
        assertEquals(result2, expResult2);
    }
    
    /**
     * Starts a default game
     * checks if game is over ( If ships array is empty ) should be false
     * hits every coordinate to sink all ships
     * checks if game is over again, should be true
     * (all ships in ships have been removed )
     */
    @Test
    public void test3() {
        System.out.println("Test 3");
        BModel instance = new BModel();
        instance.startDefaultGame();
        boolean expResult = false;
        boolean result = instance.gameOver();
        assertEquals(result, expResult);
        for (int r = 1; r < 11; r++) {
            for (int c = 1; c < 11; c++) {
                instance.hitShip(c, r);
            }
        }
        expResult = true;
        result = instance.gameOver();
        assertEquals(result, expResult);
        
    }
    
    

}
