package game.integration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.World;
import game.WorldMap;
import game.entity.Dalek;
import game.entity.Doctor;
import guice.AppModule;
import mainApp.MainApp;
import model.Vector2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    private static World world;
    private static WorldMap worldMap;
    private int dalekNumber = MainApp.DALEK_NUMBER;
    Doctor doctor;

    void prepare(int number) {
        world.initializeWorld(number);
        doctor = world.getDoctor();
    }

    @BeforeAll
    static void init() {
        final Injector injector = Guice.createInjector(new AppModule());
        world = injector.getInstance(World.class);
        worldMap = world.getWorldMap();
    }

    @BeforeEach
    void setup(){
        world.setScore(0);
    }


    @Test
    public void testInitializeNewWorld(){
        //given

        //when
        world.initializeWorld(dalekNumber);

        //then
        assertEquals(0,world.getScore());
        assertFalse(world.isGameOver());
        assertTrue(world.getDoctor().isAlive());
        assertTrue(worldMap.isInMapBounds(world.getDoctor().getPosition()));
        assertEquals(dalekNumber, world.getDalekList().size());
        world.getDalekList().forEach((dalek) -> {
            assertTrue(worldMap.isInMapBounds(dalek.getPosition()));
        });

    }

    @Test
    public void testResetScoreAfterGameOver(){
        //given
        prepare(0);
        world.setScore(10);
        world.getDoctor().setAlive(false);

        //when
        world.resetWorld();

        //then
        assertEquals(0,world.getScore());
        assertEquals(dalekNumber, world.getDalekList().size());
    }

    @Test
    public void testScoreAfterWon(){
        //given
        prepare(dalekNumber);
        world.setScore(10);
        world.getDalekList().forEach((dal) -> dal.setAlive(false));
        world.onWorldAction();
        int score = world.getScore();

        //when
        world.resetWorld();

        //then
        assertEquals(score+MainApp.SCORE_ON_WON_GAME,world.getScore());
        assertEquals(dalekNumber+1, world.getDalekList().size());
    }

    @Test
    public void testHasWon(){
        //given
        prepare(dalekNumber);

        //when
        world.getDalekList().forEach((dal) -> dal.setAlive(false));
        world.onWorldAction();

        //then
        assertTrue(world.hasWon());
    }

    @Test
    public void testGameOver(){
        //given
        prepare(0);
        world.setScore(10);

        //when
        world.getDoctor().setAlive(false);

        //then
        assertTrue(world.isGameOver());
    }

    @Test
    public void testIncreaseScoreAfterMove(){
        //given
        prepare(0);

        //when
        world.onWorldAction();

        //then
        assertEquals(1,world.getScore());
    }

    @Test
    public void testIncreaseScoreAfterDeath(){
        //given
        prepare(0);

        //when
        doctor.setAlive(false);
        world.onWorldAction();

        //then
        assertEquals(0,world.getScore());
    }



}
