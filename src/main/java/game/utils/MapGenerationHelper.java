package game.utils;

import game.WorldMap;
import game.entity.Dalek;
import game.entity.Doctor;
import mainApp.MainApp;
import model.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class MapGenerationHelper {

    public static Doctor randomPlaceDoctor(WorldMap worldMap) {
        Vector2D doctorPosition = worldMap.getRandomVector(true);
        Doctor doctor = new Doctor(doctorPosition,  MainApp.INITIAL_BOMBS, MainApp.INITIAL_TELEPORTS, MainApp.INITIAL_REWINDS);
        worldMap.addEntity(doctor);
        return doctor;
    }

    public static List<Dalek> randomPlaceDaleks(WorldMap worldMap, int daleksToCreate){
        List<Dalek> dalekList = new ArrayList<>();

        for(int i = 0; i < daleksToCreate; i++) {
            Vector2D dalekPosition = worldMap.getRandomVector(true);
            Dalek dalek = new Dalek(dalekPosition);
            worldMap.addEntity(dalek);
            dalekList.add(dalek);
        }

        return dalekList;
    }

    public static void clearDaleksFromWorldAndList(WorldMap worldMap, List<Dalek> dalekList) {
        if(dalekList != null && !dalekList.isEmpty()) {
            dalekList.clear();
        }
        worldMap.clearAllEntities();
    }

}
