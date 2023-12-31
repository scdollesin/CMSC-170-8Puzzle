package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stages.GameStage;
import java.util.ArrayList;

public class Main extends Application{
	private static Stage stage;
	private static ArrayList<Integer> input;
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\github_repo\\eclipse2023-workspace\\CMSC-170-8Puzzle\\Eight_Puzzle\\src\\application\\puzzle.in");
		
		if (file.exists()) {
			// read initial state from puzzle.in
			Scanner fscanner = new Scanner(file);
			String strInput = "";
			while (fscanner.hasNextLine()) {
				String line = fscanner.nextLine();
				System.out.println(line);
				strInput = strInput.concat(line + " ");
			}
			// split and store numeric values
	        String[] strSplit = strInput.split(" ");
	        input = new ArrayList<Integer>();
	        for (String s : strSplit)
	        	input.add(Integer.parseInt(s));
			
			fscanner.close();
			
			// the app will only launch if puzzle.in exists and contains valid input
			if (!input.isEmpty()) {
				if (input.size() == 9) {
					launch(args);
				} else {
					System.out.println("ERROR: Input must contain 9 valid integers.");
					System.exit(0);
				}
			} else {
				System.out.println("ERROR: Input is empty.");
				System.exit(0);
			}
		} else {
			System.out.println("ERROR: Input file not found.");
			System.exit(0);
		}

	}
	
	public void start(Stage stage) {
		//Initialize the scene
		Main.stage = stage;
		
		GameStage theGameStage = new GameStage(input);
		theGameStage.setStage(stage);
		
		Main.changeScene(theGameStage.getScene());
	}
	
	public static void changeScene(Scene scene) {
		Main.stage.setScene(scene);
	}

}

