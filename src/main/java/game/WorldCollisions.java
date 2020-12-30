package game;

import com.google.inject.Inject;
import game.entity.Dalek;
import game.entity.Doctor;
import game.entity.MapObject;

import java.util.List;

public class WorldCollisions {
    private final WorldMap worldMap;

    @Inject
    public WorldCollisions(WorldMap worldMap){
        this.worldMap = worldMap;
    }

    private void checkObjectCollision(MapObject mapObject) {
        if(worldMap.isOccupied(mapObject.getPosition())) {
            MapObject otherObject = worldMap.objectAt(mapObject.getPosition())
                    .orElseThrow(() -> new RuntimeException("Object not found at occupied field"));

            worldMap.makeEntityDead(mapObject);
            mapObject.setAlive(false);
            otherObject.setAlive(false);
        }
        else {
            worldMap.positionChange(mapObject);
        }
    }

    public void checkDoctorCollision(Doctor doctor) {
        //removes doctor's previous position so he won't collide with himself while using bomb or teleporting to same place
//        worldMap.getPositionsOfAlive().remove(doctor.getPrevPosition());
        worldMap.removeAlivePosition(doctor.getPrevPosition());
        this.checkObjectCollision(doctor);
    }

    public void checkDaleksCollisions(List<Dalek> dalekList, Doctor doctor) {
        //clears all positions but doctors so daleks won't bump into each other while moving same direction
        worldMap.getPositionsOfAlive().clear();
        if(doctor.isAlive()) worldMap.addEntity(doctor);

        dalekList.stream()
                .filter(Dalek::isAlive)
                .forEach(this::checkObjectCollision);
    }
}
