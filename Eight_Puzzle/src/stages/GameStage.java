package stages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import components.Tile;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
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
	private static int zeroIndex;
	public static final ArrayList<Integer> WIN_CONDITION = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,0));
    
	// Window Dimensions
	public static final int WINDOW_WIDTH = 720;
	public static final int WINDOW_HEIGHT = 720;
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
	
	private final Image bg = new Image("assets/background1.png",WINDOW_WIDTH,WINDOW_HEIGHT,false,false);
	private final static Image win_img = new Image("assets/win.png",BOARD_WIDTH,BOARD_HEIGHT,false,false);
	private final static ImageView win_imgView = new ImageView();

	
	public GameStage(ArrayList<Integer> input) {
		root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		board = new GridPane();
		tiles = new ArrayList<Tile>();
		this.input = input;
		
		setProperties();
		checkBoard();	//checks if input is already arranged in the right order
		//TODO: check if solvable
	}
	
	//method to initialize the scene elements
	private void setProperties(){
		win_imgView.setImage(win_img);
		win_imgView.setLayoutX(GameStage.BOARD_WIDTH*0.072);
		win_imgView.setLayoutY(GameStage.BOARD_WIDTH*0.072);
		win_imgView.setPreserveRatio(true);
		win_imgView.setFitWidth(GameStage.BOARD_WIDTH);
		win_imgView.setFitHeight(GameStage.BOARD_HEIGHT);
		
		this.gc.drawImage(this.bg, 0, 0);
		this.createBoard();
		this.root.getChildren().addAll(canvas, board);
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
			Tile newTile = new Tile(tile_number, tiles_created);
			if (tile_number == 0) zeroIndex = tiles_created;

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
				if (tile.getNumber() == 0) {
					
				}
			}
		}
	}
	
	public static void swapTiles(Tile clicked){
		Collections.swap(tiles, zeroIndex, clicked.index);
		System.out.println("Swapped indexes: " + zeroIndex + " " + clicked.index);
		int temp = zeroIndex;
		zeroIndex = clicked.index;
		clicked.index = temp;
		
		//refresh the board
		board.getChildren().clear();
		addTiles();
		//check for winning condition
		checkBoard();
	}
	
	private static void checkBoard(){
		ArrayList<Integer> current = new ArrayList<Integer>();
		for (Tile tile: tiles) {
			current.add(tile.getNumber());
		}
		if (current.equals(WIN_CONDITION)){
			gameDone = true;
			System.out.println(">>>>>>> YOU WIN! <<<<<<<<");
			root.getChildren().remove(board);
			root.getChildren().addAll(win_imgView);
		}
	}

	
	// Get the game's screen.
	public Scene getScene() {
		return this.scene;
	}
    
	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setTitle("8-Puzzle (Brawl Stars Edition)");		// Sets window title.
		this.stage.setResizable(false);								// Prevents the user from resizing the window.
		this.stage.show();
	}
}


