package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Vector2DTest {
    @Test
    public void getCloseToDiagonallyUpTest() {
        //given
        Vector2D positionToCatch = new Vector2D(5,5);
        Vector2D myPosition = new Vector2D(1,1);
        Vector2D positionToFinishOn = new Vector2D(2,2);

        //when
        Vector2D myNewPosition = myPosition.getCloseTo(positionToCatch);

        //then
        assertEquals(positionToFinishOn, myNewPosition);
    }

    @Test
    public void getCloseToNearlyDiagonallyUpTest() {
        //given
        Vector2D positionToCatch = new Vector2D(2,3);
        Vector2D myPosition = new Vector2D(1,1);
        Vector2D positionToFinishOn = new Vector2D(2,2);

        //when
        Vector2D myNewPosition = myPosition.getCloseTo(positionToCatch);

        //then
        assertEquals(positionToFinishOn, myNewPosition);
    }

    @Test
    public void getCloseToStraightUpTest() {
        //given
        Vector2D positionToCatch = new Vector2D(1,4);
        Vector2D myPosition = new Vector2D(1,1);
        Vector2D positionToFinishOn = new Vector2D(1,2);

        //when
        Vector2D myNewPosition = myPosition.getCloseTo(positionToCatch);

        //then
        assertEquals(positionToFinishOn, myNewPosition);
    }

    @Test
    public void getCloseToDiagonallyDownTest() {
        //given
        Vector2D positionToCatch = new Vector2D(1,1);
        Vector2D myPosition = new Vector2D(5,5);
        Vector2D positionToFinishOn = new Vector2D(4,4);

        //when
        Vector2D myNewPosition = myPosition.getCloseTo(positionToCatch);

        //then
        assertEquals(positionToFinishOn, myNewPosition);
    }

    @Test
    public void getCloseToNearlyDiagonallyDownTest() {
        //given
        Vector2D positionToCatch = new Vector2D(1,3);
        Vector2D myPosition = new Vector2D(4,5);
        Vector2D positionToFinishOn = new Vector2D(3,4);

        //when
        Vector2D myNewPosition = myPosition.getCloseTo(positionToCatch);

        //then
        assertEquals(positionToFinishOn, myNewPosition);
    }

    @Test
    public void getCloseToStraightDownTest() {
        //given
        Vector2D positionToCatch = new Vector2D(1,2);
        Vector2D myPosition = new Vector2D(1,5);
        Vector2D positionToFinishOn = new Vector2D(1,4);

        //when
        Vector2D myNewPosition = myPosition.getCloseTo(positionToCatch);

        //then
        assertEquals(positionToFinishOn, myNewPosition);
    }

    @Test
    public void toStringTest() {
        //given
        Vector2D position = new Vector2D(2,3);
        String expectedStringPosition = "x: 2, y: 3";

        //when
        String toStringPosition = position.toString();

        // then
        assertEquals(expectedStringPosition,toStringPosition);
    }

    @Test
    public void getPositionsAroundTest(){
        //given
        Vector2D centerPosition = new Vector2D(5,5);
        List<Vector2D> expectedPositions = Arrays.asList(
                new Vector2D(4,4), new Vector2D(5, 4), new Vector2D(6,4),
                new Vector2D(4,5), new Vector2D(6,5),
                new Vector2D(4,6), new Vector2D(5,6), new Vector2D(6,6));
        List<Vector2D> positions;

        //when
        positions = Vector2D.getPositionsAround(centerPosition);

        //then
        expectedPositions.forEach((pos) -> {
            System.out.println(pos.toString());
            assertTrue(positions.contains(pos));
        });
    }

}