package game.integration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.World;
import game.WorldMap;
import game.entity.Doctor;
import guice.AppModule;
import mainApp.MainApp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    private static World world;
    private static WorldMap worldMap;
    private final int dalekNumber = MainApp.DALEK_NUMBER;
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
    public void initializeNewWorldTest(){
        //given

        //when
        world.initializeWorld(dalekNumber);

        //then
        assertEquals(0,world.getScore().get());
        assertFalse(world.isGameOver());
        assertTrue(world.getDoctor().isAlive());
        assertTrue(worldMap.isInMapBounds(world.getDoctor().getPosition()));
        assertEquals(dalekNumber, world.getDalekList().size());
        world.getDalekList().forEach((dalek) -> {
            assertTrue(worldMap.isInMapBounds(dalek.getPosition()));
        });

    }

    @Test
    public void resetScoreAfterGameOverTest(){
        //given
        prepare(0);
        world.setScore(10);
        world.getDoctor().setAlive(false);

        //when
        world.resetWorld();

        //then
        assertEquals(0,world.getScore().get());
        assertEquals(dalekNumber, world.getDalekList().size());
    }

    @Test
    public void scoreAfterWonTest(){
        //given
        prepare(dalekNumber);
        world.setScore(10);
        world.getDalekList().forEach((dal) -> dal.setAlive(false));
        world.onWorldAction();
        int score = world.getScore().get();

        //when
        world.resetWorld();

        //then
        assertEquals(score+MainApp.SCORE_ON_WON_GAME,world.getScore().get());
        assertEquals(dalekNumber+1, world.getDalekList().size());
    }

    @Test
    public void hasWonTest(){
        //given
        prepare(dalekNumber);

        //when
        world.getDalekList().forEach((dal) -> dal.setAlive(false));
        world.onWorldAction();

        //then
        assertTrue(world.hasWon());
    }

    @Test
    public void gameOverTest(){
        //given
        prepare(0);
        world.setScore(10);

        //when
        world.getDoctor().setAlive(false);

        //then
        assertTrue(world.isGameOver());
    }

    @Test
    public void increaseScoreAfterMoveTest() {
        //given
        prepare(0);

        //when
        world.onWorldAction();

        //then
        assertEquals(1,world.getScore().get());
    }

    @Test
    public void increaseScoreAfterDeathTest() {
        //given
        prepare(0);

        //when
        doctor.setAlive(false);
        world.onWorldAction();

        //then
        assertEquals(0,world.getScore().get());
    }

    @Test
    public void runOutOfBombsTest() {
        //given
        prepare(0);

        //when //then
        assertEquals(2, doctor.getBombs().get());
        for(int i = 0; i<3; i++) {
            doctor.useBomb();
        }
        assertEquals(0, doctor.getBombs().get());
        assertFalse(doctor.useBomb());
        assertEquals(0, doctor.getBombs().get());
    }

    @Test
    public void runOutOfTeleportationTest() {
        //given
        prepare(0);

        //when //then
        assertEquals(3, doctor.getTeleports().get());
        for(int i = 0; i<4; i++) {
            world.makeTeleport();
        }
        assertEquals(0, doctor.getTeleports().get());
        world.makeTeleport();
        assertEquals(0, doctor.getTeleports().get());
    }

}
