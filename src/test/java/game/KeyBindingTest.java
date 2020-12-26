package game;

import controller.KeyBindings;
import game.utils.Direction;
import org.junit.jupiter.api.Test;

import javax.swing.text.JTextComponent;

import static org.junit.jupiter.api.Assertions.*;


public class KeyBindingTest {

    @Test
    public void testIsResetKey(){
        //given

        //when

        //then
        assertTrue(KeyBindings.isResetKey("r"));
        assertFalse(KeyBindings.isResetKey("R"));
        assertFalse(KeyBindings.isResetKey("0"));
    }

    @Test
    public void testIsMovementKey(){
        //given

        //when

        //then
        assertTrue(KeyBindings.isMovementKey("2"));
        assertFalse(KeyBindings.isMovementKey("5"));
        assertFalse(KeyBindings.isMovementKey("r"));
        assertFalse(KeyBindings.isMovementKey("t"));
    }



    @Test
    public void testCorrectKeyToDirection(){
        //given
        String key1 = "2";

        //when
        Direction dir1 = KeyBindings.keyToDirection(key1);

        //then
        assertEquals(Direction.SOUTH,dir1);
    }

    @Test
    public void testWrongKeyToDirection(){
        //given
        String key2 = "r";

        //when

        //then
        assertThrows(IllegalArgumentException.class, () -> KeyBindings.keyToDirection(key2));
    }
}
