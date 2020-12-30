package command;

import game.World;

public class BombCommand extends Command {

    public BombCommand(World world) {
        super(world);
    }

    @Override
    public boolean execute() {
        return world.useBomb();
    }

    @Override
    public void undo() {
        super.undo();
        int b = world.getDoctor().getBombs().get();
        world.getDoctor().setBombs(b + 1);
    }
}
