package stages;

import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Stack;
import javafx.scene.layout.HBox;
import components.Tile;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

public class GameStage {
	private Stage stage;
    private Scene scene;
    private static Group root;
    private Canvas canvas;
    private GraphicsContext gc;
    private static GridPane board;
	private static ArrayList<Tile> tiles;
	private ArrayList<Integer> input;
	public static int zeroIndex;
	public static final ArrayList<Integer> WIN_CONDITION = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,0));
	public static final HashMap<Integer, List<Integer>> CLICKABLES = new HashMap<Integer, List<Integer>>();
	private static ChoiceBox<String> modeSelect;
	public static String mode;
	public static ArrayList<Integer> path = new ArrayList<Integer>();
	public static int nextCount = 0;
	
	// Window Dimensions
	public static final double WINDOW_HEIGHT = 720;
	public static final double WINDOW_WIDTH = WINDOW_HEIGHT*0.75; //720
	
	// Grid configurations
	public final static int MAX_CELLS = 9;
	public final static int BOARD_NUM_ROWS = 3;
	public final static int BOARD_NUM_COLS = 3;
	// Board dimensions
	public final static double BOARD_WIDTH = WINDOW_WIDTH*0.875;
	public final static double BOARD_HEIGHT = WINDOW_WIDTH*0.875;
	// Tile dimensions
	public final static double TILE_WIDTH = BOARD_WIDTH/3;
	public final static double TILE_HEIGHT = BOARD_WIDTH/3;
	
	public static boolean gameDone = false;
	
	private final Image bg = new Image("assets/background.png",WINDOW_WIDTH,WINDOW_HEIGHT,false,false);
	private final static Image win_img = new Image("assets/win.png",BOARD_WIDTH,BOARD_HEIGHT,false,false);
	private final static ImageView win_imgView = new ImageView(win_img);
	private final static Image solution_btn = new Image("assets/solution_btn.png",BOARD_WIDTH/3.5,BOARD_HEIGHT/8.5,false,false);
	private final static Image next_btn = new Image("assets/next_btn.png",BOARD_WIDTH/3.5,BOARD_HEIGHT/8.5,false,false);
	private final static Image exit_btn = new Image("assets/exit_btn.png",BOARD_WIDTH/3.5,BOARD_HEIGHT/8.5,false,false);
	private final static ImageView solution_imgView = new ImageView(solution_btn);
	
	public GameStage(ArrayList<Integer> input) {
		root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		board = new GridPane();
		tiles = new ArrayList<Tile>();
		this.input = input;
		GameStage.mode = "--";
		GameStage.modeSelect = new ChoiceBox<String>();
		
		// index 0=Up 1=Right 2=Down 3=Left; a value of 9 in an index means that it is not a valid move
		CLICKABLES.put(0, Arrays.asList(9,1,3,9));		//example: valid moves are Right and Down only
		CLICKABLES.put(1, Arrays.asList(9,2,4,0));
		CLICKABLES.put(2, Arrays.asList(9,9,5,1));
		CLICKABLES.put(3, Arrays.asList(0,4,6,9));
		CLICKABLES.put(4, Arrays.asList(1,5,7,3));
		CLICKABLES.put(5, Arrays.asList(2,9,8,4));
		CLICKABLES.put(6, Arrays.asList(3,7,9,9));
		CLICKABLES.put(7, Arrays.asList(4,8,9,6));
		CLICKABLES.put(8, Arrays.asList(5,9,9,7));
		
		setProperties();
		checkWin();	//checks if input is already arranged in the right order
		checkIfSolvable(); //checks if solvable
	}
	
	//method to initialize the scene elements
	private void setProperties(){		
		win_imgView.setLayoutX(GameStage.BOARD_WIDTH*0.072);
		win_imgView.setLayoutY(GameStage.BOARD_WIDTH*0.072);
		win_imgView.setPreserveRatio(true);
		win_imgView.setFitWidth(GameStage.BOARD_WIDTH);
		win_imgView.setFitHeight(GameStage.BOARD_HEIGHT);
		
		HBox solution_hb = new HBox();
		modeSelect.getItems().addAll("--", "Breadth First Search (BFS)", "Depth First Search (DFS)");
		modeSelect.setValue("--");
		modeSelect.setPrefHeight(BOARD_HEIGHT/9);
		modeSelect.setPrefWidth(BOARD_WIDTH/1.5);
		modeSelect.setStyle("-fx-font-size: 20;");
		
		solution_imgView.setOnMouseClicked(event -> {
			String choice = modeSelect.getValue();
			if (mode == "--") {
				if (choice == "Breadth First Search (BFS)" || choice == "Depth First Search (DFS)"){
					System.out.println("\n" + choice);
					GameStage.mode = choice;
					modeSelect.setDisable(true);
					solution_imgView.setImage(next_btn);
					gameDone = true;	//disables ability to click tiles
					
					//reset board
					tiles.clear();
					createBoard();
					//search for solution
					ArrayList<ArrayList<Integer>> solution = treeSearch();
					path = solution.get(1);
					writeSolution(path);
				}
			} else {
				nextMove();
			}
		});
		solution_imgView.setOnMouseEntered(event -> {this.scene.setCursor(Cursor.HAND);});
		solution_imgView.setOnMouseExited(event -> {this.scene.setCursor(Cursor.DEFAULT);});
		
		solution_hb.getChildren().addAll(modeSelect, solution_imgView);
		solution_hb.setLayoutX(GameStage.BOARD_WIDTH*0.072);
		solution_hb.setLayoutY(GameStage.BOARD_WIDTH + 120);
		solution_hb.setSpacing(20);
		
		this.gc.drawImage(this.bg, 0, 0);
		this.createBoard();
		root.getChildren().addAll(canvas, solution_hb, board);
	}
	
	private void writeSolution(ArrayList<Integer> path){
		ArrayList<String> moves = new ArrayList<String>();
		for(int p : path) {
			switch(p) {
			case 0: moves.add("U"); break;
			case 1: moves.add("R"); break;
			case 2: moves.add("D"); break;
			case 3: moves.add("L"); break;
			default: System.out.println("Error: Invalid write to file.");
			}
		}
		String output = String.join(" ", moves);
		System.out.println("Cost: "+ PathCost(path) + "\nPath: \n"+output);
		
		try (FileWriter writer = new FileWriter("puzzle.out")){
			writer.write(output);
			writer.close();
			showMessage("Solution:\t"+output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void nextMove(){
		swapTiles(tiles.get(CLICKABLES.get(zeroIndex).get(path.get(nextCount++))));
	}

	// A state is composed of two lists, that is the configuration and the path
	private ArrayList<ArrayList<Integer>> createState(ArrayList<Integer> configuration){
		ArrayList<ArrayList<Integer>> newState = new ArrayList<ArrayList<Integer>>();
		
		//add configuration
		newState.add(configuration);
		
		//add path
		ArrayList<Integer> path = new ArrayList<>();
		newState.add(path);
		
		return newState;
	}
	
	
	// treeSearch()
	private ArrayList<ArrayList<Integer>> treeSearch(){
		final long startTime = System.nanoTime();
		
		ArrayList<ArrayList<Integer>> solution = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> explored = new ArrayList<ArrayList<Integer>>();				//explored list
		
		if(mode == "Breadth First Search (BFS)"){
			Deque<ArrayList<ArrayList<Integer>>> frontier = new ArrayDeque<ArrayList<ArrayList<Integer>>>();
			frontier.addFirst(createState(input));
			
			while(!frontier.isEmpty()){
				ArrayList<ArrayList<Integer>> currentState = frontier.removeFirst();
				
				while(explored.contains(currentState.get(0))) {currentState = frontier.removeFirst();}	//make sure that the current state hasnt been explored yet
				
				if(GoalTest(currentState)) {
					final long endTime = System.nanoTime();
					final double execTime = endTime - startTime;
					
					System.out.println("Execution time: " + Math.round((execTime/1000000000) * 100.0) / 100.0 + "s");
					return currentState;
				}
				
				else{
					if(!explored.contains(currentState.get(0))) explored.add(currentState.get(0));
					
					for(int action : Action(currentState)){
						frontier.addLast(Result(currentState, action));
					}
				}
			}
		} else if (mode == "Depth First Search (DFS)") {
			Stack<ArrayList<ArrayList<Integer>>> frontier = new Stack<ArrayList<ArrayList<Integer>>>();
			frontier.push(createState(input));
			
			while(!frontier.isEmpty()){
				ArrayList<ArrayList<Integer>> currentState = frontier.pop();
				
				
				while(explored.contains(currentState.get(0))) {currentState = frontier.pop();}	//make sure that the current state hasnt been explored yet
			    
				if(GoalTest(currentState)) {
					final long endTime = System.nanoTime();
					final double execTime = endTime - startTime;
					
					System.out.println("Execution time: " + Math.round((execTime/1000000000) * 100.0) / 100.0 + "s");
					return currentState;
				}
				
				else{
					if(!explored.contains(currentState.get(0))) explored.add(currentState.get(0));
					
					for(int action : Action(currentState)){
						frontier.push(Result(currentState, action));
					}
				}
			}
		}
		return solution;
	}
	
	// Action()
	private ArrayList<Integer> Action(ArrayList<ArrayList<Integer>> state){
		ArrayList<Integer> actions = new ArrayList<Integer>(CLICKABLES.get(state.get(0).indexOf(0)));
		actions.removeAll(Collections.singleton(9));	//remove placeholder 9 from valid actions
		return actions;
	}
	
	// GoalTest()
	private Boolean GoalTest(ArrayList<ArrayList<Integer>> state){
		if (state.get(0).equals(WIN_CONDITION))return true;
		else return false;
	}
	
	
	// Result()
	private ArrayList<ArrayList<Integer>> Result(ArrayList<ArrayList<Integer>> state, int action){
	    ArrayList<ArrayList<Integer>> result = new ArrayList<>(state.stream().map(x -> new ArrayList<>(x)).collect(Collectors.toList()));

		int emptyIndex = result.get(0).indexOf(0);
		int move = GameStage.CLICKABLES.get(emptyIndex).indexOf(action);
		result.get(1).add(move);
		Collections.swap(result.get(0), emptyIndex, action);
		
		return result;
	}
	
	// PathCost()
	private static int PathCost(ArrayList<Integer> p){
		return p.size();
	}
	
	private void createBoard(){
		board.setPrefSize(GameStage.BOARD_WIDTH, GameStage.BOARD_HEIGHT);
		//set the map to x and y location; add border color
	    board.setStyle("-fx-border-color: white ;");
		board.setLayoutX(GameStage.BOARD_WIDTH*0.07);
	    board.setLayoutY(GameStage.BOARD_WIDTH*0.07);
		
		int tiles_created = 0;
		
		//create 9 tiles
		for(int i=0; i<MAX_CELLS; i++) {
			// Instantiate tile elements
			int tile_number = this.input.get(tiles_created);
			Tile newTile = new Tile(tile_number, tiles_created, scene);
			if (tile_number == 0) zeroIndex = tiles_created;
			// configure tile's clickability with respect to the empty tile's position
			newTile.setMouseHandlers();
			
			tiles_created++;

			//add each tile to the array list tiles
			tiles.add(newTile);
		}
	    
	    addTiles();
	}
	
	private static void addTiles(){
	    //add each tile element to the board
		int t = 0;
		for(int i=0;i<GameStage.BOARD_NUM_ROWS;i++){
			for(int j=0;j<GameStage.BOARD_NUM_COLS;j++){
				Tile tile = tiles.get(t++);
				board.add(tile.getImageView(), j, i);
			}
		}
	}
	
	public static void swapTiles(Tile clicked){
		Collections.swap(tiles, zeroIndex, clicked.index);
		int temp = zeroIndex;
		zeroIndex = clicked.index;
		clicked.index = temp;
		
		//refresh the board
		board.getChildren().clear();
		addTiles();
		//check for winning condition
		checkWin();
	}
	
	private static void checkWin(){
		ArrayList<Integer> current = new ArrayList<Integer>();
		for (Tile tile: tiles) {
			current.add(tile.getNumber());
		}
		if (current.equals(WIN_CONDITION)){
			gameDone = true;
			System.out.println("\n>>>>>>> PUZZLE SOLVED! <<<<<<<<");
			root.getChildren().remove(board);
			root.getChildren().addAll(win_imgView);
			modeSelect.setDisable(true);
			solution_imgView.setImage(exit_btn);
			solution_imgView.setOnMouseClicked(event -> {System.exit(0);});
			
			if(!(mode == "--")) showMessage("Puzzle solved!\nPath cost: " + PathCost(path));
			else showMessage("Puzzle solved!");
		}
	}
	
//	Checks if the input configuration is solvable by counting inversions
	private void checkIfSolvable(){
		int inversions = 0;
		
		for(int i=1; i< MAX_CELLS; i++) {
			for(int j=0; j<i; j++) {
				if (input.get(i) != 0 && input.get(j)!= 0 && input.get(j)>input.get(i)) {
					inversions++;
				}
			}
		}
		
		if (inversions%2 == 0) System.out.println("Solvable.");
		else {
			System.out.println("Not solvable.");
			gameDone = true;
			showMessage("The puzzle is not solvable.");
			System.exit(0);
		}
	}
	
	private static void showMessage(String message){
		Alert popup = new Alert(AlertType.NONE);
		popup.setTitle("Message");
		popup.setHeaderText(null);
		Label label = new Label(message);
		label.setWrapText(true);
		popup.getDialogPane().setContent(label);
		popup.getDialogPane().getButtonTypes().add(ButtonType.OK);
		((Stage) popup.getDialogPane().getScene().getWindow()).getIcons().add(new Image("assets/icon.png"));
		popup.showAndWait();
	}

	
	// Get the game's screen.
	public Scene getScene() {
		return this.scene;
	}
    
	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setTitle("8-Puzzle (Brawl Stars Edition)");		// Sets window title.
		this.stage.getIcons().add(new Image("assets/icon.png"));
		this.stage.setResizable(false);								// Prevents the user from resizing the window.
		this.stage.show();
	}
}


