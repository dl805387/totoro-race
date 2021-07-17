package application;

import java.util.Random;

import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
		
public class Main extends Application{
	
	private Pane layout;
	private Image totoro;
	private ImageView totoroImage;
	private ColorAdjust colorAdjust;
	private Path path;
	private MoveTo moveTo;
	private CubicCurveTo cubicCurveTo ;
	private PathTransition pathTransition;
	private int seconds = 10;
	private int[] totalTimeArray = new int[5];
	private PauseTransition pauseTransition;
	private Label winnerText;
	
	@Override
	public void start(Stage primaryStage) {
		// create the GUI and setting stage and scene
		primaryStage.setTitle("Totoro Race");  
		layout = new Pane(); 
		Scene scene = new Scene(layout, 1100, 600);
		primaryStage.setScene(scene);   
		primaryStage.show();
		
		// add label
        Label text = new Label("Press the restart button to restart the race");
        text.setTextFill(Color.BLACK);
        text.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 40));
        text.setLayoutX(200);
        text.setLayoutY(0);
        layout.getChildren().add(text);
        
        // add button 
        Button button = new Button("restart"); 
        button.setLayoutX(525);
        button.setLayoutY(60);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) { 
            	layout.getChildren().remove(winnerText);
            	addTotoro(0.0, 0, 100, 0);
        		addTotoro(0.4, 0, 200, 1);
        		addTotoro(0.7, 0, 300, 2);
        		addTotoro(-0.3, 0, 400, 3);
        		addTotoro(-0.9, 0, 500, 4);
        		pauseTransition = new PauseTransition(Duration.seconds(16));
        		pauseTransition.setOnFinished(a -> declareWinner());
        		pauseTransition.play();
            } 
        }; 
        button.setOnAction(event); 
        layout.getChildren().add(button);
		
		// add totoros
		addTotoro(0.0, 0, 100, 0);
		addTotoro(0.4, 0, 200, 1);
		addTotoro(0.7, 0, 300, 2);
		addTotoro(-0.3, 0, 400, 3);
		addTotoro(-0.9, 0, 500, 4);
		
		pauseTransition = new PauseTransition(Duration.seconds(16));
		pauseTransition.setOnFinished(e -> declareWinner());
		pauseTransition.play();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// This method adds a totoro, then calls movePath method.
	public void addTotoro(double hueNum, int PosX, int PosY, int index) {
		totoro = new Image("file:totoro.gif");
		totoroImage = new ImageView(totoro);
		totoroImage.setFitWidth(120);
		totoroImage.setFitHeight(90);
		totoroImage.setLayoutX(PosX);
		totoroImage.setLayoutY(PosY);	
		colorAdjust = new ColorAdjust();
	    colorAdjust.setContrast(0.5);
	    colorAdjust.setHue(hueNum);
	    colorAdjust.setBrightness(0.1);
	    colorAdjust.setSaturation(0.1); 
	    totoroImage.setPreserveRatio(true);
	    totoroImage.setEffect(colorAdjust);
	    layout.getChildren().add(totoroImage);
	    // Calls on movePath to move the totoro's position.
	    movePath(300, randomNumber());
	    movePath(600, randomNumber());
	    movePath(900, randomNumber());
	    movePath(1200, randomNumber());
	    // Adds seconds of each totoro to an array.
	    totalTimeArray[index] = seconds;
	    // Resets time so that each totoro starts with 10 seconds for the duration.
	    seconds = 10;
	}
	
	// This method moves the totoros to the right based on randomNumber method.
	public void movePath(double endPosition, int randomSecs) {
		path = new Path(); 
	    moveTo = new MoveTo(0, 50);  
	    cubicCurveTo = new CubicCurveTo(endPosition, 50, endPosition , 50, endPosition, 50); 
	    path.getElements().add(moveTo); 
	    path.getElements().add(cubicCurveTo);
	    pathTransition = new PathTransition(); 
	    pathTransition.setDuration(Duration.seconds(seconds += randomSecs)); 
	    pathTransition.setNode(totoroImage);
	    pathTransition.setPath(path);  
	    pathTransition.setAutoReverse(true); 
	    pathTransition.play(); 
	}
	
	// Generates random integer number between 1 to 4.
	public int randomNumber() {
		Random random = new Random();
	    return random.nextInt(4) + 1;
	}
	
	// This method finds the smallest seconds value and return the index
	public int minIndex(int[] totalTimeArray) {
		int min = totalTimeArray[0];
		int minIndex = 0;
		for (int i = 0; i < totalTimeArray.length; i++) {
			if (totalTimeArray[i] < min) {
				min = totalTimeArray[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	// Produces a text for the winner.
	public void winnerText(String winner) {
		winnerText = new Label(winner);
		switch (minIndex(totalTimeArray)) {
		case 0:
			winnerText.setTextFill(Color.YELLOW);
			break;
		case 1:
			winnerText.setTextFill(Color.GREEN);
			break;
		case 2:
			winnerText.setTextFill(Color.BLUE);
			break;
		case 3:
			winnerText.setTextFill(Color.RED);
			break;
		case 4:
			winnerText.setTextFill(Color.PURPLE);
			break;
		}	
		winnerText.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 40));
		winnerText.setLayoutX(400);
		winnerText.setLayoutY(200);
        layout.getChildren().add(winnerText);
	}
	
	// Uses winnerText method to declare which totoro won.
	public void declareWinner() {
		switch (minIndex(totalTimeArray)) {
		case 0:
			winnerText("Yellow Totoro Wins!");
			break;
		case 1:
			winnerText("Green Totoro Wins!");
			break;
		case 2:
			winnerText("Blue Totoro Wins!");
			break;
		case 3:
			winnerText("Red Totoro Wins!");
			break;
		case 4:
			winnerText("Purple Totoro Wins!");
			break;
		}
	}
}