package graphics;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import poo.khet.Anubis;
import poo.khet.Board;
import poo.khet.Pharaoh;
import poo.khet.Piece;
import poo.khet.Pyramid;
import poo.khet.Scarab;
import poo.khet.Team;
import poo.khet.gameutils.Direction;
import poo.khet.gameutils.Position;

public class Main extends Application{
	
	GameManager gameManager;
	GraphicsContext piecesGC;
	Canvas graphicBoard;
	Canvas piecesLayer;
	Canvas rotateButtons;
	BoardDrawer boardDrawer;
	
	public void start(Stage primaryStage) throws Exception{
		Group root = new Group();

		graphicBoard = new Canvas(750,600);
		graphicBoard.getGraphicsContext2D().drawImage(new Image("file:assets/Board.png"),0,0);

		piecesLayer = new Canvas(graphicBoard.getWidth(), graphicBoard.getHeight());
		piecesGC = piecesLayer.getGraphicsContext2D();
		
		boardDrawer = new BoardDrawer(piecesGC);
		
		rotateButtons = new Canvas(200,80);
		rotateButtons.getGraphicsContext2D().drawImage(new Image("file:assets/RotButtons.png"), 0, 0);
		rotateButtons.setTranslateY(graphicBoard.getHeight()+10);
		rotateButtons.setTranslateX(20);
	
		Button rotateCWButton = new Button("Rotate clockwise");
		rotateCWButton.setTranslateY(graphicBoard.getHeight() + 10);
		rotateCWButton.setTranslateX(10);

		Button rotateCCWButton= new Button("Rotate counterclockwise");
		rotateCCWButton.setTranslateY(graphicBoard.getHeight() + 10);
		rotateCCWButton.setTranslateX(120);

		
		gameManager = new GameManager(null);
	
		//--->Deprecated
		//new BoardDrawer().draw(graphicsContext);
	
		root.getChildren().add(graphicBoard);
		root.getChildren().add(piecesLayer);
		root.getChildren().add(rotateButtons);

	/*	root.getChildren().add(rotateCWButton);
		root.getChildren().add(rotateCCWButton);
      
		rotateCWButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				gameManager.handleRotation(true);
			}
			
		});
		
		
		rotateCCWButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				gameManager.handleRotation(false);
			}
			
		});
		
		*/
		redraw();
        piecesLayer.addEventHandler(MouseEvent.MOUSE_CLICKED, 
        		new EventHandler<MouseEvent>(){
        			public void handle(MouseEvent e) {
    					Position coord = getPositionFromMouse(e.getX(), e.getY());
        				piecesGC.clearRect((coord.getRow()*75), (coord.getCol()*75), 75, 75);
        				if(e.getButton() == MouseButton.PRIMARY){
        					new UIPiece().draw(piecesGC, coord);
        					gameManager.handle(coord);
        				}
        					redraw(); 
        			}
        });
		

        rotateButtons.addEventHandler(MouseEvent.MOUSE_CLICKED, 
        		new EventHandler<MouseEvent>(){
        			public void handle(MouseEvent e) {
        			gameManager.handleRotation(e.getX() < 98);
        			redraw();
        			}
        });

       
		primaryStage.setTitle("Preview");
        primaryStage.setScene(new Scene(root, graphicBoard.getWidth() + 50, graphicBoard.getHeight()+ rotateButtons.getHeight() + 50));
        primaryStage.show();
	}

	private void redraw(){
		Map<Position, Piece> pMap= new HashMap<Position, Piece>();
		pMap.put(new Position(0,4), new Anubis(Team.RED, Direction.SOUTH));
		pMap.put(new Position(0,5), new Pharaoh(Team.RED));
		pMap.put(new Position(0,6), new Anubis(Team.RED, Direction.SOUTH));
		pMap.put(new Position(0,7), new Pyramid(Team.RED, Direction.EAST));
		
		pMap.put(new Position(1,2), new Pyramid(Team.RED, Direction.SOUTH));
		
		pMap.put(new Position(2,3), new Pyramid(Team.SILVER, Direction.WEST));

		
		Board board = new Board(pMap);
		
		boardDrawer.draw(board);
	}

	
	/**
	 * Devuelve una coordenada a partir de la posicion del mouse
	 * @param x: 
	 * @param y
	 * @return - Coordinate: la coordenada del tablero correspondiente al click
	 */
	private Position getPositionFromMouse(double x, double y) {
		int squareSize = 75;
		Position c = new Position((int)x / squareSize, (int)y / squareSize);
		System.out.println("Coord: (" + c.getRow() + ", " + c.getCol() + ")");
		return c;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
