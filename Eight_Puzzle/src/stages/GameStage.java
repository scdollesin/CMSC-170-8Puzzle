package stages;

import java.util.ArrayList;

import components.Tile;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class GameStage {
	private Stage stage;
    private Scene scene;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;
    private GridPane board;
	private ArrayList<Tile> tiles;
	private ArrayList<Integer> input;
    
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
	
	private final boolean IS_GAME_DONE = false;
	
	private final Image bg = new Image("assets/background1.png",WINDOW_WIDTH,WINDOW_HEIGHT,false,false);
	
	
	public GameStage(ArrayList<Integer> input) {
		this.root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		this.board = new GridPane();
		this.tiles = new ArrayList<Tile>();
		this.input = input;
//		this.input = new ArrayList<Integer>();
//		//TODO: remove hard-coded input
//		for (int x = 0; x < MAX_CELLS; x++) input.add(x+1);
		setProperties();
	}
	
	//method to initialize the scene elements
	private void setProperties(){
		this.gc.drawImage(this.bg, 0, 0);
		this.createBoard();
		this.root.getChildren().addAll(canvas, board);
	}
	
	private void createBoard(){
		int tiles_created = 0;
		//create 9 tiles
		for(int i=0;i<GameStage.BOARD_NUM_ROWS;i++){
			for(int j=0;j<GameStage.BOARD_NUM_COLS;j++){
				
				// Instantiate tile elements
				Tile newTile = new Tile(this.input.get(tiles_created));
				newTile.initRowCol(i,j);
				tiles_created++;

				//add each tile to the array list tiles
				this.tiles.add(newTile);
//				System.out.println("> tile " + tiles_created + " created. " + "["+i+","+j+"]");

			}
		}

		this.board.setPrefSize(GameStage.BOARD_WIDTH, GameStage.BOARD_HEIGHT);
		//set the map to x and y location; add border color
	    this.board.setStyle("-fx-border-color: white ;");
		this.board.setLayoutX(GameStage.BOARD_WIDTH*0.07);
	    this.board.setLayoutY(GameStage.BOARD_WIDTH*0.07);

	    //add each tile element to the board
		for(Tile tile: tiles){
		    this.board.add(tile.getImageView(), tile.getCol(), tile.getRow());
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


