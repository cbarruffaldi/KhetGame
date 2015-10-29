package graphics;

import java.util.HashMap;
import java.util.Map;

import com.sun.corba.se.impl.orbutil.graph.Graph;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import poo.khet.Piece;
import poo.khet.Anubis;
import poo.khet.BeamCannon;
import poo.khet.Board;
import poo.khet.CannonPositions;
import poo.khet.Pyramid;
import poo.khet.Pharaoh;
import poo.khet.Scarab;
import poo.khet.Team;
import poo.khet.gameutils.Direction;
import poo.khet.gameutils.Position;


public class GameDrawer implements CannonPositions{
	
    static final int COLUMNS = 10;
    static final int ROWS = 8;
    static final int SQUARE_SIZE = 75;
    
	Map<Piece, Image> imageMap = new HashMap<Piece, Image>();
	Map<BeamCannon, Image> cannonImg = new HashMap<BeamCannon, Image>();
	
	Board board;
	BeamCannon redCannon;
	BeamCannon silverCannon;
	
	public GameDrawer(Board board, BeamCannon redCannon, BeamCannon silverCannon) {
		mapFiller();
		this.board = board;
		this.redCannon = redCannon;
		this.silverCannon = silverCannon;
		
	} 
	
	
	public void draw(GraphicsContext graphicsContext){
		
		
		graphicsContext.clearRect(SQUARE_SIZE*RED_CANNON_POSITION.getCol(), SQUARE_SIZE*RED_CANNON_POSITION.getRow(),
				SQUARE_SIZE, SQUARE_SIZE);
	graphicsContext.drawImage(cannonImg.get(redCannon), SQUARE_SIZE*RED_CANNON_POSITION.getCol(), SQUARE_SIZE*RED_CANNON_POSITION.getRow());
		
		graphicsContext.clearRect(SQUARE_SIZE*SILVER_CANNON_POSITION.getCol(), SQUARE_SIZE*SILVER_CANNON_POSITION.getRow(),
				SQUARE_SIZE, SQUARE_SIZE);
		graphicsContext.drawImage(cannonImg.get(silverCannon), SQUARE_SIZE*(SILVER_CANNON_POSITION.getCol()), SQUARE_SIZE*(SILVER_CANNON_POSITION.getRow()));

		Position pos;
		Piece piece;
		for(int i=0; i<ROWS ;i++){
			for(int j=0; j<COLUMNS; j++){
				pos = new Position(i, j);
				if(board.isInBounds(pos) && !board.isEmptyPosition(pos)){ //Me protejo contra las esquinas que no son del tablero
					piece = board.getOccupantIn(pos);
					graphicsContext.clearRect(SQUARE_SIZE*j, SQUARE_SIZE*i, SQUARE_SIZE, SQUARE_SIZE);
					graphicsContext.drawImage(imageMap.get(piece), SQUARE_SIZE*j + 1, SQUARE_SIZE*i + 1);
				}
			}
		}
	}

	
	
	/**
	 * Cargar recursos en un mapa de imagenes
	 * @param imageMap - el mapa
	 */
	void mapFiller(){
		imageMap.put(new Anubis(Team.RED, Direction.NORTH), new Image("file:assets/pieces/anubis/red_north.png"));
		imageMap.put(new Anubis(Team.RED, Direction.EAST), new Image("file:assets/pieces/anubis/red_east.png"));
		imageMap.put(new Anubis(Team.RED, Direction.WEST), new Image("file:assets/pieces/anubis/red_west.png"));
		imageMap.put(new Anubis(Team.RED, Direction.SOUTH), new Image("file:assets/pieces/anubis/red_south.png"));

		imageMap.put(new Anubis(Team.SILVER, Direction.NORTH), new Image("file:assets/pieces/anubis/silver_north.png"));
		imageMap.put(new Anubis(Team.SILVER, Direction.EAST), new Image("file:assets/pieces/anubis/silver_east.png"));
		imageMap.put(new Anubis(Team.SILVER, Direction.WEST), new Image("file:assets/pieces/anubis/silver_west.png"));
		imageMap.put(new Anubis(Team.SILVER, Direction.SOUTH), new Image("file:assets/pieces/anubis/silver_south.png"));

		imageMap.put(new Scarab(Team.RED, Direction.NORTH), new Image("file:assets/pieces/scarab/red_north_south.png"));
		imageMap.put(new Scarab(Team.RED, Direction.SOUTH), new Image("file:assets/pieces/scarab/red_north_south.png"));
		imageMap.put(new Scarab(Team.RED, Direction.EAST), new Image("file:assets/pieces/scarab/red_east_west.png"));
		imageMap.put(new Scarab(Team.RED, Direction.WEST), new Image("file:assets/pieces/scarab/red_east_west.png"));

		imageMap.put(new Scarab(Team.SILVER, Direction.NORTH), new Image("file:assets/pieces/scarab/silver_north_south.png"));
		imageMap.put(new Scarab(Team.SILVER, Direction.SOUTH), new Image("file:assets/pieces/scarab/silver_north_south.png"));
		imageMap.put(new Scarab(Team.SILVER, Direction.EAST), new Image("file:assets/pieces/scarab/silver_east_west.png"));
		imageMap.put(new Scarab(Team.SILVER, Direction.WEST), new Image("file:assets/pieces/scarab/silver_east_west.png"));

		imageMap.put(new Pyramid(Team.RED, Direction.NORTH), new Image("file:assets/pieces/pyramid/red_north.png"));
		imageMap.put(new Pyramid(Team.RED, Direction.EAST), new Image("file:assets/pieces/pyramid/red_east.png"));
		imageMap.put(new Pyramid(Team.RED, Direction.WEST), new Image("file:assets/pieces/pyramid/red_west.png"));
		imageMap.put(new Pyramid(Team.RED, Direction.SOUTH), new Image("file:assets/pieces/pyramid/red_south.png"));

		imageMap.put(new Pyramid(Team.SILVER, Direction.NORTH), new Image("file:assets/pieces/pyramid/silver_north.png"));
		imageMap.put(new Pyramid(Team.SILVER, Direction.EAST), new Image("file:assets/pieces/pyramid/silver_east.png"));
		imageMap.put(new Pyramid(Team.SILVER, Direction.WEST), new Image("file:assets/pieces/pyramid/silver_west.png"));
		imageMap.put(new Pyramid(Team.SILVER, Direction.SOUTH), new Image("file:assets/pieces/pyramid/silver_south.png"));

		imageMap.put(new Pharaoh(Team.RED), new Image("file:assets/pieces/pharaoh/red.png"));
		imageMap.put(new Pharaoh(Team.SILVER), new Image("file:assets/pieces/pharaoh/silver.png"));
		
		
		cannonImg.put(new BeamCannon(Team.RED), new Image("file:assets/cannons/red_regular.png"));
		cannonImg.put(new BeamCannon(Team.SILVER), new Image("file:assets/cannons/silver_regular.png"));
		
		BeamCannon redSwitched = new BeamCannon(Team.RED);
		redSwitched.switchFacing();
		cannonImg.put(redSwitched, new Image("file:assets/cannons/red_switched.png"));
	
		BeamCannon silverSwitched = new BeamCannon(Team.SILVER);
		silverSwitched.switchFacing();
		cannonImg.put(silverSwitched, new Image("file:assets/cannons/silver_switched.png"));
	}

	
}