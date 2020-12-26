package game.entity;

import model.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DalekTest {


    @Test
    public void standardMoveDalekTest() {
        //given
        Vector2D dalekPosition = new Vector2D(2,2);
        Vector2D doctorPosition = new Vector2D(4,4);
        Vector2D positionToFinishOn = new Vector2D(3,3);

        Dalek dalek = new Dalek(dalekPosition);

        //when
        dalek.moveTowards(doctorPosition);

        //then
        assertEquals(positionToFinishOn, dalek.getPosition());
    }

    @Test
    public void dalekOnDoctorPositionTest() {
        //given
        Vector2D dalekPosition = new Vector2D(2,2);
        Vector2D doctorPosition = new Vector2D(2,2);

        Dalek dalek = new Dalek(dalekPosition);

        //when
        dalek.moveTowards(doctorPosition);

        //then
        assertEquals(dalekPosition, dalek.getPosition());
    }

    @Test
    public void dalekCatchDoctorTest() {
        //given
        Vector2D dalekPosition = new Vector2D(2,2);
        Vector2D doctorPosition = new Vector2D(3,3);

        Dalek dalek = new Dalek(dalekPosition);

        //when
        dalek.moveTowards(doctorPosition);

        //then
        assertEquals(doctorPosition, dalek.getPosition());
    }
}