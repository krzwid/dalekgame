package game.utils;

import game.WorldMap;
import game.entity.Dalek;
import game.entity.Doctor;
import mainApp.MainApp;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MapGenerationHelperTest {

    @Test
    public void checkCreatingDoctor() {
        //given
        int mapWidth = 10;
        int mapHeight = 10;
        WorldMap worldMap = new WorldMap(mapWidth, mapHeight);

        //when
        Doctor generatedDoctor = MapGenerationHelper.randomPlaceDoctor(worldMap);

        //then
        assertEquals(2, generatedDoctor.getBombs());
        assertEquals(3, generatedDoctor.getTeleports());
        assertTrue(generatedDoctor.getPosition().getX() < MainApp.WIDTH);
        assertTrue(generatedDoctor.getPosition().getY() < MainApp.HEIGHT);
        assertTrue(worldMap.getPositionsOfAlive().containsValue(generatedDoctor));
    }

    @Test
    public void checkCreatingDaleks() {
        //given
        int mapWidth = 10;
        int mapHeight = 10;
        WorldMap worldMap = new WorldMap(mapWidth, mapHeight);
        int numberOfDaleksToCreate = 10;

        //when
        List<Dalek> generatedDaleks = MapGenerationHelper.randomPlaceDaleks(worldMap, numberOfDaleksToCreate);

        //then
        assertEquals(numberOfDaleksToCreate, generatedDaleks.size());
        assertEquals(numberOfDaleksToCreate, worldMap.getPositionsOfAlive().size());
    }
}