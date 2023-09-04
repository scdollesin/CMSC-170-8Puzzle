package components;

import stages.GameStage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {
	private int number;
	protected Image img;
	protected ImageView imgView;
	protected int row, col;
	
	public final static Image ONE_IMAGE = new Image("assets/one.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image TWO_IMAGE = new Image("assets/two.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image THREE_IMAGE = new Image("assets/three.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image FOUR_IMAGE = new Image("assets/four.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image FIVE_IMAGE = new Image("assets/five.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image SIX_IMAGE = new Image("assets/six.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image SEVEN_IMAGE = new Image("assets/seven.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image EIGHT_IMAGE = new Image("assets/eight.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	public final static Image NINE_IMAGE = new Image("assets/nine.png",GameStage.TILE_WIDTH,GameStage.TILE_WIDTH,false,false);
	
	
	public Tile(int number){
		this.number = number;
		
		//TODO: remove print statements
		// load image depending on the number designated to this tile
		switch(this.number) {
			case 1: this.img = ONE_IMAGE; System.out.println("1"); break;
			case 2: this.img = TWO_IMAGE; System.out.println("2"); break;
			case 3: this.img = THREE_IMAGE; System.out.println("3"); break;
			case 4: this.img = FOUR_IMAGE; System.out.println("4"); break;
			case 5: this.img = FIVE_IMAGE; System.out.println("5"); break;
			case 6: this.img = SIX_IMAGE; System.out.println("6"); break;
			case 7: this.img = SEVEN_IMAGE; System.out.println("7"); break;
			case 8: this.img = EIGHT_IMAGE; System.out.println("8"); break;
			case 9: this.img = NINE_IMAGE; System.out.println("9"); break;
			default: System.out.println("ERROR: INVALID TILE NUMBER"); break;
		}
		
		this.setImageView();
		this.setMouseHandler();
	}
	
	public void initRowCol(int i, int j) {
		this.row = i;
		this.col = j;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
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

	
	private void setMouseHandler(){
	}
}
