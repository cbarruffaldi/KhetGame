package poo.khet.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import poo.khet.Beam;
import poo.khet.BeamAction;
import poo.khet.BeamCannon;
import poo.khet.BeamManager;
import poo.khet.Board;
import poo.khet.CannonPositions;
import poo.khet.Game;
import poo.khet.Team;
import poo.khet.gameutils.BoardDimensions;
import poo.khet.gameutils.Position;

public class AIMover implements CannonPositions, BoardDimensions {
    private Game game;
    static Team team = Team.RED; // Siempre juega con el equipo Rojo
    private BeamManager beamManager;
    private Board auxiliarBoard;

    public AIMover(Game game) {
        this.game = game;
    }

    /**
     * Se encarga de elegir la mejor jugada posible, con un criterio establecido, y de realizarla,
     * luego llama a game para que finalize el turno
     */
    public void makeMove() {
        auxiliarBoard = new Board(game.getBoard().getPiecesPosition());
        beamManager = new BeamManager(auxiliarBoard);
        List<Action> possibleActions = new ArrayList<>();
        possibleActions.addAll(possibleMoves());
        possibleActions.addAll(possibleRotations());
        Action destroyChoice = null;
        Action secondChoice = null;
        Collections.shuffle(possibleActions);

        for (Action action : possibleActions) {
            BeamAction beamFate = simulateAction(action);
            if (beamFate == BeamAction.DESTROYED_PIECE
                    && isOpponentPiece(beamManager.getLastPos())) {
                destroyChoice = action;
            } else if (beamFate != BeamAction.DESTROYED_PIECE) {
                secondChoice = action;
                // Guarda una jugada aleatoria en caso de que no se pueda destruir una ficha rival
            }

            // Revierte la jugada simulada para seguir simulando otras
            Action restore = action.getRevertedAction(action);
            restore.executeActionIn(auxiliarBoard);
        }
        if (destroyChoice != null) {
            destroyChoice.updateGame(game);
        } else if (secondChoice != null) {
            secondChoice.updateGame(game);
        } else {
            possibleActions.get(0).updateGame(game);
        }
        game.nextTurn();// No lo tendria que llamar el gameManager a esto?
    }

    private boolean isOpponentPiece(Position pos) {
        return auxiliarBoard.getOccupantIn(pos).getTeam().equals(Team.SILVER);
    }

    private BeamAction simulateAction(Action action) {
        action.executeActionIn(auxiliarBoard);
        BeamCannon cannon = game.getBeamCannon(team);
        Beam beam = cannon.generateBeam();
        Position startingPosition = RED_CANNON_POSITION;
        return beamManager.manageBeam(beam, startingPosition);
    }

    private List<Action> possibleMoves() {
        List<Action> possibleMoves = new ArrayList<Action>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Position from = new Position(i, j);
                if (game.isValidSelection(from)) {
                    for (Position to : getAdjacentPositions(from)) {
                        if (game.isValidMove(from, to)) {
                            possibleMoves.add(new Move(from, to));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    private List<Position> getAdjacentPositions(Position start) {
        List<Position> ends = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Position end = new Position(start.getRow() + i, start.getCol() + j);
                if (auxiliarBoard.isInBounds(end)) {
                    ends.add(end);
                }
            }
        }
        return ends;
    }

    private List<Action> possibleRotations() {
        List<Action> possibleRotations = new ArrayList<Action>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Position start = new Position(i, j);
                if (game.isValidSelection(start)) {
                    // Rotacion Clockwise y Counterclockwise
                    possibleRotations.add(new Rotation(start, true));
                    possibleRotations.add(new Rotation(start, false));
                }
            }
        }
        return possibleRotations;
    }
}