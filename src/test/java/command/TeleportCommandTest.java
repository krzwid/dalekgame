package command;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.World;
import game.entity.Doctor;
import guice.AppModule;
import model.Vector2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeleportCommandTest {
    private static World world;
    private static Doctor doctor;

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
    public void executeTeleportCommandLessTeleportsTest(){
        //given
        TeleportCommand command = new TeleportCommand(world);
        int teleportNumber = doctor.getTeleports().get();

        //when
        command.execute();

        //then
        assertEquals(teleportNumber-1, doctor.getTeleports().get());
    }

    @Test
    public void testExecuteTeleportCommand(){
        //given
        Vector2D doctorPosition = new Vector2D(5,5);
        moveDoctor(doctorPosition);
        TeleportCommand command = new TeleportCommand(world);

        //when
        command.execute();

        //then
        assertNotEquals(doctorPosition, doctor.getPosition());
        assertFalse(world.getWorldMap().isOccupied(doctorPosition));
    }


    @Test
    public void testUndoTeleportCommandTeleportsNumber(){
        //given
        int teleportNumber = doctor.getTeleports().get();
        TeleportCommand command = new TeleportCommand(world);
        command.execute();

        //when
        command.undo();

        //then
        assertEquals(teleportNumber, doctor.getTeleports().get());
    }

    @Test
    public void testUndoTeleportCommandPreviousPosition(){
        //given
        Vector2D doctorPosition = new Vector2D(5,5);
        moveDoctor(doctorPosition);
        TeleportCommand command = new TeleportCommand(world);
        command.execute();

        //when
        command.undo();

        //then
        assertEquals(doctorPosition, doctor.getPosition());
        assertTrue(world.getWorldMap().isOccupied(doctorPosition));
        assertFalse(world.getWorldMap().isOccupied(doctor.getPrevPosition()));
    }
}
