package stages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
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
	public static Deque<ArrayList<ArrayList<String>>> frontier;	//data structure for BFS
    
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
		CLICKABLES.put(0, Arrays.asList(1,3));
		CLICKABLES.put(1, Arrays.asList(0,2,4));
		CLICKABLES.put(2, Arrays.asList(1,5));
		CLICKABLES.put(3, Arrays.asList(0,4,6));
		CLICKABLES.put(4, Arrays.asList(1,3,5,7));
		CLICKABLES.put(5, Arrays.asList(2,4,8));
		CLICKABLES.put(6, Arrays.asList(3,7));
		CLICKABLES.put(7, Arrays.asList(4,6,8));
		CLICKABLES.put(8, Arrays.asList(5,7));
		
		setProperties();
		checkWin();	//checks if input is already arranged in the right order
		//TODO: check if solvable
		checkIfSolvable();
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
					System.out.println(choice);
					GameStage.mode = choice;
					modeSelect.setDisable(true);
					solution_imgView.setImage(next_btn);
					gameDone = true;	//disables ability to click tiles
					
					//reset board
					tiles.clear();
					createBoard();
				}
			} else {
				System.out.println("Next");
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
//		System.out.println("Swapped indexes: " + zeroIndex + " " + clicked.index);
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
			System.out.println(">>>>>>> YOU WIN! <<<<<<<<");
			root.getChildren().remove(board);
			root.getChildren().addAll(win_imgView);
			modeSelect.setDisable(true);
			solution_imgView.setImage(exit_btn);
			solution_imgView.setOnMouseClicked(event -> {System.exit(0);});
			showMessage("Puzzle solved!");
		}
	}
	
//	Checks if the input configuration is solvable by counting inversions
	@SuppressWarnings("unchecked")
	private void checkIfSolvable(){
		int inversions = 0;
		
		for(int i=1; i< MAX_CELLS; i++) {
			for(int j=0; j<i; j++) {
				if (input.get(i) != 0 && input.get(j)!= 0 && input.get(j)>input.get(i)) {
					//System.out.println("inversion: " + input.get(j) + " > " + input.get(i));
					inversions++;
				}
			}
		}
		
		//System.out.println("= "+ inversions + " inversions");
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
		popup.setContentText(message);
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


