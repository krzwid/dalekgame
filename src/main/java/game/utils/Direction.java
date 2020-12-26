package game.utils;

import model.Vector2D;

public enum Direction {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public Vector2D toVector() {
        return switch (this) {
            case SOUTH -> new Vector2D(0, 1);
            case SOUTHEAST -> new Vector2D(1, 1);
            case EAST -> new Vector2D(1, 0);
            case NORTHEAST -> new Vector2D(1, -1);
            case NORTH -> new Vector2D(0, -1);
            case NORTHWEST -> new Vector2D(-1, -1);
            case WEST -> new Vector2D(-1, 0);
            case SOUTHWEST -> new Vector2D(-1, 1);
        };
    }


}

