package game.entity;

import javafx.beans.property.SimpleIntegerProperty;
import mainApp.MainApp;
import model.Vector2D;

public class Doctor extends MapObject {
    private final SimpleIntegerProperty bombs;
    private final SimpleIntegerProperty teleports;
    private final SimpleIntegerProperty rewinds;
    private Vector2D prevPosition;

    public Doctor(Vector2D position, int bombs, int teleports, int rewinds) {
        super(position);
        this.bombs = new SimpleIntegerProperty(bombs);
        this.teleports = new SimpleIntegerProperty(teleports);
        this.rewinds = new SimpleIntegerProperty(rewinds);
        this.prevPosition = position;
    }

    public void move(Vector2D newPosition) {
        this.prevPosition = this.position;
        this.position = newPosition;
    }

    public boolean teleport(Vector2D newPosition) {
        if(teleports.get() > 0) {
            this.move(newPosition);
            teleports.set(teleports.getValue() - 1);
            return true;
        }
        return false;
    }

    public boolean useBomb() {
        if(bombs.get() > 0) {
            this.move(getPosition());
            bombs.set(bombs.getValue() - 1);
            return true;
        }
        return false;
    }

    public boolean useRewind() {
        if(rewinds.get() > 0) {
            rewinds.set(rewinds.getValue() - 1);
            return true;
        }
        return false;
    }

    public void setBombs(int bombs) {
        this.bombs.set(Math.min(bombs, MainApp.INITIAL_BOMBS));
    }

    public void setTeleports(int teleports) {
        this.teleports.set(Math.min(teleports, MainApp.INITIAL_TELEPORTS));
    }

    public void setRewinds(int rewinds) {
        this.rewinds.set(Math.min(rewinds, MainApp.INITIAL_REWINDS));
    }

    public SimpleIntegerProperty getBombs() {
        return bombs;
    }

    public SimpleIntegerProperty getTeleports() {
        return teleports;
    }

    public SimpleIntegerProperty getRewinds() {
        return rewinds;
    }

    public Vector2D getPrevPosition() {
        return this.prevPosition;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setPrevPosition(Vector2D prevPosition) {
        this.prevPosition = prevPosition;
    }
}
