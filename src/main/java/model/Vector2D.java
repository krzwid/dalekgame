package model;

import game.utils.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Vector2D {
    private final int x;
    private final int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static List<Vector2D> getPositionsAround(Vector2D position) {
        List<Vector2D> positionsAround = new ArrayList<>();

        Arrays.asList(Direction.values()).forEach(dir -> positionsAround.add(position.add(dir.toVector())));

        return positionsAround;
    }

    public Vector2D getCloseTo(Vector2D to){
        int x = this.x;
        int y = this.y;

        if(this.x < to.getX()) x++;
        else if (this.x > to.getX()) x--;

        if(this.y < to.getY()) y++;
        else if (this.y > to.getY()) y--;

        return new Vector2D(x,y);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.getX(), this.y + other.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString(){
        return "x: "+ this.x +", y: "+this.y;
    }

    //getters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}


