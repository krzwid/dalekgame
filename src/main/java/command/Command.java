package command;

import game.World;
import game.entity.Doctor;
import model.Vector2D;
import java.util.LinkedList;
import java.util.List;

public abstract class Command {

    protected final World world;
    protected final Vector2D prevDoctorPosition;
    protected final Vector2D doctorPosition;
    protected final List<Vector2D> daleksPosition;
    protected final List<Vector2D> deadDaleksPosition;

    public Command(World world) {
        this.world = world;
        prevDoctorPosition = world.getDoctor().getPrevPosition();
        doctorPosition = world.getDoctor().getPosition();
        daleksPosition = new LinkedList<>();
        daleksPosition.addAll(world.getWorldMap().getPositionsOfAlive().keySet());;
        daleksPosition.remove(doctorPosition);
        deadDaleksPosition = new LinkedList<>();
        deadDaleksPosition.addAll(world.getWorldMap().getPositionsOfDead().keySet());
    }

    public abstract boolean execute();

    public void undo() {
        world.getWorldMap().clearAllEntities();
        world.getDalekList().clear();
        world.getDalekList().addAll(world.getWorldMap().addDaleksToMap(daleksPosition, true));
        world.getDalekList().addAll(world.getWorldMap().addDaleksToMap(deadDaleksPosition, false));
        Doctor doctor = world.getDoctor();
        doctor.setPrevPosition(prevDoctorPosition);
        doctor.setPosition(doctorPosition);
        world.getWorldMap().getPositionsOfAlive().put(doctor.getPosition(), doctor);
    }
}