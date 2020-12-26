package controller;

import game.WorldMap;
import game.entity.Doctor;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import mainApp.MainApp;
import model.Vector2D;

public class MapDrafter {
    private Canvas canvas;

    private Image doctorImage;
    private Image dalekImage;
    private Image rockImage;
    private GraphicsContext context;
    private int cellWidth;
    private int cellHeight;

    private final static String DOCTOR_PATH = "/doctor.png";
    private final static String ROCK_PATH = "/rock.png";
    private final static String DALEK_PATH = "/dalek.png";

    private static final double TILE_LINE_WIDTH = 2.0;

    private final static String FONT_FAMILY = "Verdana";
    private final static int FONT_SIZE = 115;
    private final static int FONT_SIZE_SECONDARY = 60;
    private final static int FONT_SIZE_TERTIARY = 40;
    private final static int STROKE_WIDTH = 3;
    private final static int STROKE_WIDTH_SECONDARY = 2;
    private final static int STROKE_WIDTH_TERTIARY = 1;

    public void initialize(Canvas canvas, WorldMap worldMap) {
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();
        doctorImage = new Image( getClass().getResourceAsStream(DOCTOR_PATH));
        rockImage = new Image( getClass().getResourceAsStream(ROCK_PATH));
        dalekImage = new Image( getClass().getResourceAsStream(DALEK_PATH));

        cellWidth = ((int) canvas.getWidth() - (worldMap.getWidth()-1) * 2 ) / worldMap.getWidth();
        cellHeight = ( (int) canvas.getHeight() - (worldMap.getHeight()-1) * 2 ) / worldMap.getHeight();
        this.drawScreen(worldMap);
    }

    public void drawScreen(WorldMap worldMap){
        Platform.runLater( () -> {
            context.setFill(Color.WHITE);
            context.clearRect(0,0, canvas.getWidth(), canvas.getHeight());

            for (int i=0; i<MainApp.HEIGHT-1; i++) {
                context.setLineWidth(2.0);
                context.setFill(Color.BLACK);
                context.strokeLine(0,  0.5+(i+1)*cellHeight + i*2, canvas.getWidth(), 0.5+(i+1)*cellHeight + i*2);
            }
            for (int i=0; i<MainApp.WIDTH; i++) {
                context.setLineWidth(TILE_LINE_WIDTH);
                context.setFill(Color.BLACK);
                context.strokeLine( 0.5+(i+1)*cellWidth + i*2, 0, 0.5+(i+1)*cellWidth + i*2, canvas.getHeight());
            }

            for (int i=0; i<MainApp.HEIGHT; i++) {
                for (int j=0; j<MainApp.WIDTH; j++) {
                    int finalI = i;
                    int finalJ = j;
                    worldMap
                            .objectAt(new Vector2D(i,j))
                            .ifPresent( object -> {
                                if (object instanceof Doctor && object.isAlive()) {
                                    context.drawImage(doctorImage, (cellWidth * finalI) + finalI * 2, (cellHeight * finalJ) + finalJ * 2, cellWidth - 1, cellHeight - 1);
                                } else {
                                    if (object.isAlive()) {
                                        context.drawImage(dalekImage, (cellWidth * finalI) + finalI * 2, (cellHeight * finalJ) + finalJ * 2, cellWidth - 1, cellHeight - 1);
                                    } else {
                                        context.drawImage(rockImage, (cellWidth * finalI) + finalI * 2, (cellHeight * finalJ) + finalJ * 2, cellWidth - 1, cellHeight - 1);
                                    }
                                }
                            });
                }
            }
        });
    }

    public void drawTextOnVictory(int score) {
        double screenHeightHalf = canvas.getHeight() / 2;
        Platform.runLater( () -> {
            addStrokeText(
                    Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE),
                    "YOU WON",
                    screenHeightHalf - 40,
                    Color.BLUE,
                    STROKE_WIDTH
            );

            addStrokeText(
                    Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE_SECONDARY),
                    "Current Score: " + score,
                    screenHeightHalf + 40,
                    Color.BLUE,
                    STROKE_WIDTH_SECONDARY
            );

            addStrokeText(
                    Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE_TERTIARY),
                    "Press " + KeyBindings.USE_RESET.toUpperCase() + " to continue",
                    screenHeightHalf + 100,
                    Color.BLUE,
                    STROKE_WIDTH_TERTIARY
            );
        });
    }

    public void drawTextOnLosing(int score) {
        double screenHeightHalf = canvas.getHeight() / 2;
        Platform.runLater( () -> {
            addStrokeText(
                    Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE),
                    "YOU LOST",
                    screenHeightHalf - 40,
                    Color.RED,
                    STROKE_WIDTH
            );

            addStrokeText(
                    Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE_SECONDARY),
                    "Your Score: " + score,
                    screenHeightHalf + 40,
                    Color.RED,
                    STROKE_WIDTH_SECONDARY
            );

            addStrokeText(
                    Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE_TERTIARY),
                    "Press " + KeyBindings.USE_RESET.toUpperCase() + " restart",
                    screenHeightHalf + 100,
                    Color.RED,
                    STROKE_WIDTH_TERTIARY
            );
        });
    }

    private void addStrokeText(Font font, String string, double height, Color fillColor, int strokeWidth) {
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);

        context.setFont(font);
        context.setLineWidth(strokeWidth);
        context.setFill(fillColor);
        context.setStroke(Color.BLACK);

        context.fillText(string, canvas.getWidth()/2, height);
        context.strokeText(string, canvas.getWidth()/2, height);
    }
}
