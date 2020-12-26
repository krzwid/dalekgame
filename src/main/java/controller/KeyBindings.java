package controller;

import game.utils.Direction;
import static game.utils.Direction.*;
import java.util.Map;

//class made for easier remapping buttons
public class KeyBindings {
    public final static String USE_TELEPORT = "t";
    public final static String USE_TELEPORT_NUMERICAL = "5";
    public final static String USE_BOMB = "b";
    public final static String USE_RESET = "r";

    //newer version of ke.getText().matches("[1-4|6-9]")
    private static final Map<String, Direction> moveControls = Map.of(
            "moveSW", SOUTHWEST,
            "moveS", SOUTH,
            "moveSE", SOUTHEAST,
            "moveW", WEST,
            "moveE", EAST,
            "moveNW", NORTHWEST,
            "moveN", NORTH,
            "moveNE", NORTHEAST
    );

    public static boolean isResetKey(String key) {
        return USE_RESET.equals(key);
    }
    public static boolean isMovementKey(String key) {
        return moveControls.containsKey(key);
    }

    public static Direction keyToDirection(String key) {
        if(!isMovementKey(key)) throw new IllegalArgumentException("This button isn't mapped to movement direction");
        return moveControls.get(key);
    }
}
