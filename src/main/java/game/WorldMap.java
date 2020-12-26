package game;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import game.entity.Dalek;
import game.entity.Doctor;
import game.entity.MapObject;
import model.Vector2D;

import java.util.*;

public class WorldMap  {
    private final int height;
    private final int width;
    private final Map<Vector2D, MapObject> positionsOfAlive;
    private final Map<Vector2D, MapObject> positionsOfDead;

    @Inject
    public WorldMap(@Named("Width") int width, @Named("Height") int height) {
        this.width = width;
        this.height = height;
        positionsOfAlive = new HashMap<>();
        positionsOfDead = new HashMap<>();
    }

    public int aliveDaleks(){
        return positionsOfAlive.size() - 1;
    }

    public Vector2D getRandomVector(boolean mustBeUnoccupied) {
        Random random = new Random();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        Vector2D vector = new Vector2D(x,y);
        while(mustBeUnoccupied && this.isOccupied(vector)) {
            x = random.nextInt(width);
            y = random.nextInt(height);
            vector = new Vector2D(x,y);
        }
        return vector;
    }

    public boolean isOccupied(Vector2D vector2D) {
        return positionsOfAlive.containsKey(vector2D) || positionsOfDead.containsKey(vector2D);
    }

    public boolean isInMapBounds(Vector2D vec){
        if(vec.getX() >= width || vec.getX() < 0){
            return false;
        }
        else return vec.getY() < width && vec.getY() >= 0;
    }

    public void addEntity(MapObject mapObject) {
        if (this.isOccupied(mapObject.getPosition())) {
            System.out.println(mapObject.getPosition().getX() + " "+mapObject.getPosition().getY()+"  " +mapObject);
            throw new RuntimeException("Place is already occupied!");
        }
        positionsOfAlive.put(mapObject.getPosition(), mapObject);
    }

    public void makeEntityDead(MapObject obj){
        if(positionsOfAlive.containsKey(obj.getPosition())) {
            positionsOfAlive.remove(obj.getPosition());
            positionsOfDead.put(obj.getPosition(), obj);
        }
    }

    public void clearAllEntities() {
        positionsOfAlive.clear();
        positionsOfDead.clear();
    }

    public Optional<MapObject> objectAt (Vector2D position) {
        Optional<MapObject> optional = Optional.ofNullable(positionsOfAlive.get(position));
        if (optional.isPresent()) {
            return optional;
        }
        return Optional.ofNullable(positionsOfDead.get(position));
    }

    public void positionChange(MapObject object) {
        positionsOfAlive.put(object.getPosition(), object);
    }



    public void destroyObjectsOnVectors(List<Vector2D> positionsToDestroy) {
        positionsToDestroy.stream()
                .filter(this::isInMapBounds)
                .filter(this::isOccupied)
                .forEach(position -> {
                    MapObject obj = this.objectAt(position)
                            .orElseThrow(() -> new RuntimeException("Object not found at occupied field"));
                    this.makeEntityDead(obj);
                    obj.setAlive(false);
                });
    }

    //getters/setters
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public Map<Vector2D, MapObject> getPositionsOfAlive() {
        return positionsOfAlive;
    }
}
