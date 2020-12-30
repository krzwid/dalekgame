package command;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.World;
import game.entity.Dalek;
import game.entity.Doctor;
import game.utils.Direction;
import guice.AppModule;
import model.Vector2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveCommandTest {

    private static World world;
    private static Doctor doctor;

    private void createPlaceDalek(Vector2D position) {
        Dalek dal = new Dalek(position);
        world.getDalekList().add(dal);
        world.getWorldMap().addEntity(dal);
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
    public void executeDoctorMovementTest(){
        //given
        Vector2D doctorPosition = new Vector2D(5,5);
        moveDoctor(doctorPosition);
        Direction dir = Direction.NORTH;
        MoveCommand command = new MoveCommand(world, dir);

        //when
        command.execute();

        //then
        assertEquals(doctorPosition, doctor.getPrevPosition());
        assertEquals(new Vector2D(5,4), doctor.getPosition());
        assertTrue(doctor.isAlive());
    }

    @Test
    public void testUndoDoctorMovement(){
        //given
        Vector2D doctorPosition = new Vector2D(5,5);
        moveDoctor(doctorPosition);

        Direction dir = Direction.NORTHEAST;
        MoveCommand command = new MoveCommand(world, dir);

        command.execute();

        //when
        command.undo();

        //then
        assertEquals(doctorPosition, doctor.getPosition());
        assertTrue(doctor.isAlive());
    }

    @Test
    public void testUndoDaleksMovement(){
        //given
        Vector2D doctorPosition = new Vector2D(5,5);
        moveDoctor(doctorPosition);

        Vector2D positionOne = new Vector2D(1,1);
        Vector2D positionTwo = new Vector2D(5,8);
        createPlaceDalek(positionOne);
        createPlaceDalek(positionTwo);

        Direction dir = Direction.NORTH;
        MoveCommand command = new MoveCommand(world, dir);
        command.execute();

        //when
        command.undo();

        //then
        assertEquals(2, world.getDalekList().size());
        assertTrue(world.getWorldMap().isOccupied(positionOne));
        assertTrue(world.getWorldMap().isOccupied(positionTwo));
        assertEquals(positionOne, world.getDalekList().get(0).getPosition());
        assertEquals(positionTwo, world.getDalekList().get(1).getPosition());
    }
}
