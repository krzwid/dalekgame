package controller;

import com.google.inject.Inject;
import command.BombCommand;
import command.CommandRegistry;
import command.MoveCommand;
import command.TeleportCommand;
import game.World;
import game.utils.Direction;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class MapController {

    @FXML private Canvas canvas;
    @FXML private VBox movementButtons;
    @FXML private Label scoreLabel;
    @FXML private Button teleportationButton;
    @FXML private Button restartButton;
    @FXML private Button undoButton;
    @FXML private Button bombButton;
    @FXML private Label remainingTeleports;
    @FXML private Label remainingBombs;
    @FXML private Label remainingRewinds;

    private final World world;
    private final MapDrafter mapDrafter;
    private final CommandRegistry commandRegistry;

    @Inject
    public MapController(World world, MapDrafter mapDrafter, CommandRegistry commandRegistry) {
        this.world = world;
        this.mapDrafter = mapDrafter;
        this.commandRegistry = commandRegistry;
    }

    public void initialize() {
        mapDrafter.initialize(canvas, world.getWorldMap());
        setButtonsAndLabelsBinding();
        setResetButtonState(true);
    }

    public void addKeyboardEventToScene(Scene scene){
        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            String keyChar = ke.getText();
            ke.consume();// <-- stops passing the event to next node
            executeKeyFunction(keyChar);
        });
    }

    private void executeKeyFunction(String keyChar){
        if(world.isGameOver()|| world.hasWon()) {
            if(KeyBindings.isResetKey(keyChar)) {
                onResetWorld();
                setResetButtonState(true);
                setButtonsAndLabelsBinding();
            }
        }
        else {
            switch (keyChar) {
                case KeyBindings.USE_TELEPORT, KeyBindings.USE_TELEPORT_NUMERICAL -> {
                    onUseTeleport();
                }
                case KeyBindings.USE_BOMB -> {
                    onUseBomb();
                }
                case KeyBindings.USE_REWIND -> {
                    onUseRewind();
                }
                default -> {
                    if (KeyBindings.isMovementKey(keyChar)) {
                        onMoveKeyPress(KeyBindings.keyToDirection(keyChar));
                        System.out.println("Your score: " + world.getScore().get());
                    }
                }
            }
        }
        mapDrafter.drawScreen(world.getWorldMap());
        this.checkEndGame();
    }

    private void checkEndGame(){
        if(world.hasWon()) {
            System.out.println("Y O U   W O N!!!");
            mapDrafter.drawTextOnVictory(world.getScore().get());
            setResetButtonState(false);
        }
        if(world.isGameOver()) {
            System.out.println("Y O U   L O S T  :(");
            mapDrafter.drawTextOnLosing(world.getScore().get());
            setResetButtonState(false);
        }
    }

    private void setButtonsAndLabelsBinding() {
        movementButtons.disableProperty().bind(restartButton.disabledProperty().not());
        bombButton.disableProperty().bind(world.getDoctor().getBombs().isEqualTo(0)
                .or(restartButton.disabledProperty().not()));
        teleportationButton.disableProperty().bind(world.getDoctor().getTeleports().isEqualTo(0)
                .or(restartButton.disabledProperty().not()));
        undoButton.disableProperty().bind(world.getDoctor().getRewinds().isEqualTo(0)
                .or(commandRegistry.getStackSizeProperty().isEqualTo(0))
                .or(restartButton.disabledProperty().not()));

        remainingTeleports.textProperty().bind(Bindings.format("Remaining teleports: %d",world.getDoctor().getTeleports()));
        remainingBombs.textProperty().bind(Bindings.format("Remaining bombs: %d",world.getDoctor().getBombs()));
        remainingRewinds.textProperty().bind(Bindings.format("Remaining rewinds: %d",world.getDoctor().getRewinds()));
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", world.getScore()));
    }

    private void setResetButtonState(boolean disable){
        restartButton.setDisable(disable);
    }

    @FXML
    private void onTeleportationButtonPress(){
        executeKeyFunction(KeyBindings.USE_TELEPORT);
    }

    @FXML
    private void onBombButtonPress(){
        executeKeyFunction(KeyBindings.USE_BOMB);
    }

    @FXML
    private void onResetButtonPress(){
        executeKeyFunction(KeyBindings.USE_RESET);
    }

    @FXML
    private void onUndoButtonPress() { executeKeyFunction(KeyBindings.USE_REWIND);}

    @FXML
    private void onMoveButtonPress(ActionEvent event){
        String key = ((Button)event.getSource()).getId();
        executeKeyFunction(key);
    }

    private void onMoveKeyPress(Direction direction) {
        commandRegistry.executeCommand(
                new MoveCommand(this.world, direction)
        );
    }

    private void onUseTeleport() {
        commandRegistry.executeCommand(
                new TeleportCommand(this.world)
        );
    }

    private void onUseBomb() {
        commandRegistry.executeCommand(
                new BombCommand(this.world)
        );
    }

    private void onUseRewind() {
        if(commandRegistry.getStackSizeProperty().get() > 0 && world.getDoctor().useRewind()) {
            commandRegistry.undo();
            System.out.println("It's rewind time!");
            world.setScore(world.getScore().get() -1);
        }
        else {
            System.out.println("Cannot rewind: empty command stack or no rewinds left");
        }
    }

    private void onResetWorld() {
        world.resetWorld();
        commandRegistry.clearCommandStack();
    }
}