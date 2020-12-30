package command;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.World;
import game.entity.Dalek;
import game.entity.Doctor;
import game.entity.MapObject;
import guice.AppModule;
import model.Vector2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BombCommandTest {
    private static World world;
    private static Doctor doctor;

    private Dalek createPlaceDalek(Vector2D position) {
        Dalek dal = new Dalek(position);
        world.getDalekList().add(dal);
        world.getWorldMap().addEntity(dal);
        return dal;
    }

    private void moveDoctor(Vector2D position){
        doctor.move(position);
        world.getWorldMap().removeAlivePosition(doctor.getPrevPosition());
        world.getWorldMap().positionChange(doctor);
    }

    @BeforeAll
    static void init() {
        final Injector injector = Guice.createInjector(new AppModule());
        world = injector.getInstance(World.class);
    }

    @BeforeEach
    void setup() {
        world.initializeWorld(0);
        doctor = world.getDoctor();
    }

    @Test
    public void executeBombCommandLessBombsTest(){
        //given
        BombCommand command = new BombCommand(world);
        int bombNumber = doctor.getBombs().get();

        //when
        command.execute();

        //then
        assertEquals(bombNumber-1, doctor.getBombs().get());
    }

    @Test
    public void executeBombCommandKillNeighboursTest(){
        //given
        Vector2D doctorPosition = new Vector2D(5,5);
        moveDoctor(doctorPosition);
        Vector2D positionOne = new Vector2D(5,4);
        Vector2D positionTwo = new Vector2D(6,6);
        Dalek dalekOne = createPlaceDalek(positionOne);
        Dalek dalekTwo = createPlaceDalek(positionTwo);
        BombCommand command = new BombCommand(world);

        //when
        command.execute();

        //then
        assertTrue(doctor.isAlive());
        assertFalse(dalekOne.isAlive());
        assertFalse(dalekTwo.isAlive());
        assertTrue(world.getWorldMap().isOccupied(positionOne));
        assertTrue(world.getWorldMap().isOccupied(positionTwo));
    }

    @Test
    public void testUndoBombCommandBombsNumber(){
        //given
        int bombNumber = doctor.getBombs().get();
        BombCommand command = new BombCommand(world);
        command.execute();

        //when
        command.undo();

        //then
        assertEquals(bombNumber, doctor.getBombs().get());
    }

    @Test
    public void testUndoBombCommandNeighboursAlive(){
        //given
        Vector2D doctorPosition = new Vector2D(5,5);
        moveDoctor(doctorPosition);
        Vector2D positionOne = new Vector2D(5,4);
        Vector2D positionTwo = new Vector2D(6,6);
        createPlaceDalek(positionOne);
        createPlaceDalek(positionTwo);
        BombCommand command = new BombCommand(world);
        command.execute();

        //when
        command.undo();

        //then
        MapObject objectOne = world.getWorldMap().objectAt(positionOne)
                .orElseThrow(() -> new RuntimeException("Object not found at occupied field"));
        MapObject objectTwo = world.getWorldMap().objectAt(positionTwo)
                .orElseThrow(() -> new RuntimeException("Object not found at occupied field"));
        assertTrue(doctor.isAlive());
        assertTrue(objectOne.isAlive());
        assertTrue(objectTwo.isAlive());
        assertTrue(world.getWorldMap().isOccupied(positionOne));
        assertTrue(world.getWorldMap().isOccupied(positionTwo));
    }
}
