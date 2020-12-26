package game.entity;

import mainApp.MainApp;
import model.Vector2D;

public class Doctor extends MapObject {
    private int bombs;
    private int teleports;
    private Vector2D prevPosition;

    public Doctor(Vector2D position, int bombs, int teleports) {
        super(position);
        this.bombs = bombs;
        this.teleports = teleports;
        this.prevPosition = position;
    }

    public void move(Vector2D newPosition) {
        this.prevPosition = this.position;
        this.position = newPosition;
    }

    public boolean teleport(Vector2D newPosition) {
        if(teleports > 0) {
            this.move(newPosition);
            teleports--;
            return true;
        }
        return false;
    }

    public boolean useBomb() {
        if(bombs > 0) {
            this.move(getPosition());
            bombs--;
            return true;
        }
        return false;
    }

    public void setBombs(int bombs) {
        if(bombs > MainApp.INITIAL_BOMBS) this.bombs = MainApp.INITIAL_BOMBS;
        this.bombs = bombs;
    }
    public void setTeleports(int teleports) {
        if(teleports > MainApp.INITIAL_TELEPORTS) this.teleports = MainApp.INITIAL_TELEPORTS;
        this.teleports = teleports;
    }
    public int getBombs() {
        return bombs;
    }
    public int getTeleports() {
        return teleports;
    }
    public Vector2D getPrevPosition() {
        return this.prevPosition;
    }
}
