package command;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.World;
import game.entity.Doctor;
import game.utils.Direction;
import guice.AppModule;
import javafx.beans.binding.IntegerBinding;
import mainApp.MainApp;
import model.Vector2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandRegistryTest {
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
    public void addToStackExecutedCommandTest(){
        //given
        Command bombCommand = new BombCommand(world);
        CommandRegistry commandRegistry = new CommandRegistry();
        int stackSize = commandRegistry.getStackSizeProperty().get();

        //when
        commandRegistry.executeCommand(bombCommand);

        //then
        assertEquals(stackSize+1, commandRegistry.getStackSizeProperty().get());
    }

    @Test
    public void notAddToStackExecutedCommandTest(){
        //given
        Command bombCommand = new BombCommand(world);
        CommandRegistry commandRegistry = new CommandRegistry();
        int stackSize = commandRegistry.getStackSizeProperty().get();
        doctor.setBombs(0);

        //when
        commandRegistry.executeCommand(bombCommand);

        //then
        assertEquals(stackSize, commandRegistry.getStackSizeProperty().get());
    }

    @Test
    public void addCommandToFullStackTest(){
        //given
        moveDoctor(new Vector2D(0,0));
        Command moveCommand= new MoveCommand(world, Direction.SOUTH);
        CommandRegistry commandRegistry = new CommandRegistry();
        for(int i=0;i< MainApp.INITIAL_REWINDS;i++){
            commandRegistry.executeCommand(moveCommand);
        }
        int stackSize = commandRegistry.getStackSizeProperty().get();

        //when //then
        assertEquals(MainApp.INITIAL_REWINDS, stackSize);

        commandRegistry.executeCommand(moveCommand);

        assertEquals(MainApp.INITIAL_REWINDS, commandRegistry.getStackSizeProperty().get());
        assertEquals(stackSize, commandRegistry.getStackSizeProperty().get());
    }

    @Test
    public void undoEmptyStackTest(){
        //given
        CommandRegistry commandRegistry = new CommandRegistry();
        int stackSize = commandRegistry.getStackSizeProperty().get();
        Vector2D doctorPosition = doctor.getPosition();
        int bombs = doctor.getBombs().get();
        int teleports = doctor.getTeleports().get();

        //when
        commandRegistry.undo();

        //then
        assertEquals(0,stackSize);
        assertEquals(0,commandRegistry.getStackSizeProperty().get());
        assertEquals(doctorPosition, doctor.getPosition());
        assertEquals(bombs, doctor.getBombs().get());
        assertEquals(teleports, doctor.getTeleports().get());
    }

    @Test
    public void undoStackWithOneCommandTest(){
        //given
        BombCommand bombCommand = new BombCommand(world);
        CommandRegistry commandRegistry = new CommandRegistry();
        int stackSize = commandRegistry.getStackSizeProperty().get();
        int bombs = doctor.getBombs().get();
        commandRegistry.executeCommand(bombCommand);
        int stackSizeAfterExecute = commandRegistry.getStackSizeProperty().get();

        //when
        commandRegistry.undo();

        //then
        assertEquals(stackSize,commandRegistry.getStackSizeProperty().get());
        assertEquals(stackSize+1,stackSizeAfterExecute);
        assertEquals(bombs, doctor.getBombs().get());
    }

    @Test
    public void undoFullStackTest(){
        //given
        moveDoctor(new Vector2D(0,0));
        CommandRegistry commandRegistry = new CommandRegistry();
        Vector2D doctorPosition = doctor.getPosition();
        for(int i=0;i< MainApp.INITIAL_REWINDS;i++){
            doctorPosition = doctor.getPosition();
            Command moveCommand= new MoveCommand(world, Direction.SOUTH);
            commandRegistry.executeCommand(moveCommand);
        }
        int stackSizeAfterExecute = commandRegistry.getStackSizeProperty().get();

        //when
        commandRegistry.undo();

        //then
        assertEquals(stackSizeAfterExecute - 1,commandRegistry.getStackSizeProperty().get());
        assertEquals(doctorPosition, doctor.getPosition());
    }

    @Test
    public void clearFullStackTest(){
        //given
        moveDoctor(new Vector2D(0,0));
        CommandRegistry commandRegistry = new CommandRegistry();
        for(int i=0;i< MainApp.INITIAL_REWINDS;i++){
            Command moveCommand= new MoveCommand(world, Direction.SOUTH);
            commandRegistry.executeCommand(moveCommand);
        }

        //when
        commandRegistry.clearCommandStack();

        //then
        assertEquals(0,commandRegistry.getStackSizeProperty().get());
    }

    @Test
    public void getStackSizePropertyTest(){
        //given
        moveDoctor(new Vector2D(0,0));
        CommandRegistry commandRegistry = new CommandRegistry();
        int j = MainApp.INITIAL_REWINDS - 1;
        for(int i=0;i < j;i++){
            Command moveCommand= new MoveCommand(world, Direction.SOUTH);
            commandRegistry.executeCommand(moveCommand);
        }

        //when
        IntegerBinding stackSize = commandRegistry.getStackSizeProperty();

        //then
        assertEquals(j, stackSize.get());
    }

}
