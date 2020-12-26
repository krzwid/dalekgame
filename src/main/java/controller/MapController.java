package controller;

import com.google.inject.Inject;
import game.World;
import game.utils.Direction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.util.List;


//TODO add score, bombs left, teleports left to UI, not console
//TODO: make whole key/button events disabled by boolean flag?
//TODO make better injection to MapDrawer? Maybe pass it canvas somehow?
public class MapController {

    @FXML
    private Canvas canvas;

    @FXML
    private Button restartButton;

    @FXML private Button moveS;
    @FXML private Button moveN;
    @FXML private Button moveE;
    @FXML private Button moveW;
    @FXML private Button moveSW;
    @FXML private Button moveSE;
    @FXML private Button moveNW;
    @FXML private Button moveNE;

    @FXML private Label scoreLabel;

    @FXML
    private Button teleportationButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button bombButton;

    private final World world;
    private final MapDrafter mapDrafter;


    @Inject
    public MapController(World world, MapDrafter mapDrafter) {
        this.world = world;
        this.mapDrafter = mapDrafter;
    }

    public void initialize() {
        mapDrafter.initialize(canvas, world.getWorldMap());
        setAllButtonsToGameState();
    }

    public void addKeyboardEventToScene(Scene scene){
        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            String keyChar = ke.getText();
            ke.consume();// <-- stops passing the event to next node
            executeKeyFunction(keyChar);
        });
    }

    private void executeKeyFunction(String keyChar){
        //  if\else used to disable other buttons when the game is over
        if(world.isGameOver() || world.hasWon()) {
            if(KeyBindings.isResetKey(keyChar)) {
                this.onResetWorld();
                setAllButtonsToGameState();
            }
        }
        else {
            switch (keyChar) {
                case KeyBindings.USE_TELEPORT, KeyBindings.USE_TELEPORT_NUMERICAL -> {
                    onUseTeleport();
                    if(world.howManyTeleports() == 0) {
                        teleportationButton.setDisable(true);
                    }
                }
                case KeyBindings.USE_BOMB -> {
                    onUseBomb();
                    if(world.howManyBombs() == 0) {
                        bombButton.setDisable(true);
                    }
                }
                default -> {
                    if (KeyBindings.isMovementKey(keyChar)) {
                        this.onMoveKeyPress(KeyBindings.keyToDirection(keyChar));
                        System.out.println("Your score: " + world.getScore());
                    }
                }
            }
        }

        mapDrafter.drawScreen(world.getWorldMap());
        this.checkEndGame();
        setScore();
    }

    private void checkEndGame(){
        if(world.hasWon()) {
            System.out.println("Y O U   W O N!!!");
            mapDrafter.drawTextOnVictory(world.getScore());
            setAllButtonsToWonOrLostState();
        }
        if(world.isGameOver()) {
            System.out.println("Y O U   L O S T  :(");
            mapDrafter.drawTextOnLosing(world.getScore());
            setAllButtonsToWonOrLostState();
        }
    }

    private void setAllButtonsToGameState() {
        bombButton.setDisable(false);
        teleportationButton.setDisable(false);
        restartButton.setDisable(true);
        undoButton.setDisable(false);
        moveS.setDisable(false);
        moveN.setDisable(false);
        moveE.setDisable(false);
        moveW.setDisable(false);
        moveSW.setDisable(false);
        moveSE.setDisable(false);
        moveNW.setDisable(false);
        moveNE.setDisable(false);
    }

    private void setAllButtonsToWonOrLostState() {
        bombButton.setDisable(true);
        teleportationButton.setDisable(true);
        restartButton.setDisable(false);
        undoButton.setDisable(true);
        moveS.setDisable(true);
        moveN.setDisable(true);
        moveE.setDisable(true);
        moveW.setDisable(true);
        moveSW.setDisable(true);
        moveSE.setDisable(true);
        moveNW.setDisable(true);
        moveNE.setDisable(true);
    }

    private void setScore() {
        int score = world.getScore();
        String scoreText = "Score: " + score;
        scoreLabel.setText(scoreText);
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
    private void onMoveButtonPress(ActionEvent event){
        String key = ((Button)event.getSource()).getId();
        executeKeyFunction(key);
    }

    private void onMoveKeyPress(Direction direction) {
        world.makeMove(direction);
    }

    private void onUseTeleport() {
        world.makeTeleport();
    }

    private void onUseBomb() {
        world.useBomb();
    }

    private void onResetWorld() {
        world.resetWorld();
    }
}