package game.entity;

import model.Vector2D;

public abstract class MapObject {
    protected Vector2D position;
    protected boolean isAlive;

    public MapObject(Vector2D position) {
        this.position = position;
        this.isAlive = true;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }

}
