package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stages.GameStage;

public class Main extends Application{
	private static Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) {
		//Initialize the scene
		Main.stage = stage;
		
		GameStage theGameStage = new GameStage();
		theGameStage.setStage(stage);
		
		Main.changeScene(theGameStage.getScene());
	}
	
	public static void changeScene(Scene scene) {
		Main.stage.setScene(scene);
	}

}

