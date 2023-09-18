package components;

import stages.GameStage;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Tile {
	private int number;
	public int index;
	protected Image img;
	protected ImageView imgView;
	private Scene scene;
	
	public final static Image ONE_IMAGE = new Image("assets/one.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image TWO_IMAGE = new Image("assets/two.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image THREE_IMAGE = new Image("assets/three.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image FOUR_IMAGE = new Image("assets/four.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image FIVE_IMAGE = new Image("assets/five.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image SIX_IMAGE = new Image("assets/six.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image SEVEN_IMAGE = new Image("assets/seven.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image EIGHT_IMAGE = new Image("assets/eight.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image NINE_IMAGE = new Image("assets/nine.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);

	public static final Map<Integer, ArrayList<Integer>> movable = new HashMap<Integer, ArrayList<Integer>>();
	
	public Tile(int number, int index, Scene scene){
		this.number = number;
		this.index = index;
		this.scene = scene;

		// load image depending on the number designated to this tile
		switch(this.number) {
			case 1: this.img = ONE_IMAGE; break;
			case 2: this.img = TWO_IMAGE; break;
			case 3: this.img = THREE_IMAGE; break;
			case 4: this.img = FOUR_IMAGE; break;
			case 5: this.img = FIVE_IMAGE; break;
			case 6: this.img = SIX_IMAGE; break;
			case 7: this.img = SEVEN_IMAGE; break;
			case 8: this.img = EIGHT_IMAGE; break;
			case 0: this.img = NINE_IMAGE; break;
			default: System.out.println("ERROR: Input contains an invalid number."); System.exit(0);
		}
		
		this.setImageView();
	}
	
	public int getNumber() {
		return this.number;
	}
	
	private void setImageView() {
		// initialize the image view of this element
		this.imgView = new ImageView();
		this.imgView.setImage(this.img);
		this.imgView.setLayoutX(0);
		this.imgView.setLayoutY(0);
		this.imgView.setPreserveRatio(true);
		this.imgView.setFitWidth(GameStage.TILE_WIDTH);
		this.imgView.setFitHeight(GameStage.TILE_HEIGHT);
	}
	
	public ImageView getImageView(){
		return this.imgView;
	}

	
	public void setMouseHandlers( ){
		// swap tile clicked with empty tile
		if (this.number!=0) {
			this.imgView.setOnMouseClicked(event -> {
				if (!GameStage.gameDone) {
					if(GameStage.CLICKABLES.get(GameStage.zeroIndex) != null && GameStage.CLICKABLES.get(GameStage.zeroIndex).contains(this.index)) {
						int pos = GameStage.CLICKABLES.get(GameStage.zeroIndex).indexOf(this.index);
						switch(pos) {
							case 0: System.out.println("UP"); break;
							case 1: System.out.println("RIGHT"); break;
							case 2: System.out.println("DOWN"); break;
							case 3: System.out.println("LEFT"); break;
						}
						GameStage.swapTiles(this);
					}
//					else System.out.println("Invalid action.");
				}
			});
			this.imgView.setOnMouseEntered(event -> {
				if(GameStage.CLICKABLES.get(GameStage.zeroIndex) != null && GameStage.CLICKABLES.get(GameStage.zeroIndex).contains(this.index))
				this.scene.setCursor(Cursor.HAND);
				});
			this.imgView.setOnMouseExited(event -> {
				if(GameStage.CLICKABLES.get(GameStage.zeroIndex) != null && GameStage.CLICKABLES.get(GameStage.zeroIndex).contains(this.index))
					this.scene.setCursor(Cursor.DEFAULT);
				});
		}
	}
}
