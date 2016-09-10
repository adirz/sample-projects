package com.epam.sdk;

import com.epam.sdk.figures.*;
import com.epam.sdk.Tuple;
import com.epam.sdk.move.Move;
import com.epam.sdk.move.Rotate;
import com.epam.sdk.move.Shift;
import com.epam.sdk.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import static com.epam.sdk.Rotation.*;
import static com.epam.sdk.Color.*;
import static com.epam.sdk.Orientation.*;


/**
 * Class board implements game board with standart size 8x10.
 */
public class Board implements Serializable {
    private static final int DEFAULT_BOARD_WIDTH = 10, DEFAULT_BOARD_HEIGHT = 8;

    private final Square[][] squares = new Square[DEFAULT_BOARD_HEIGHT][DEFAULT_BOARD_WIDTH];
    private final int width = DEFAULT_BOARD_WIDTH;
    private final int height = DEFAULT_BOARD_HEIGHT;

    public Board() {
        initBoardColor();
    }

    /**
     * This method build raw data and build board.
     *
     * @param rawBoardState It is String, with describe board state.
     *                      Example: PH 74, S1 79, C1 44, C2 45
     * @return Board witch matches input String
     */
    public static Board createBoardFromPosition(String rawBoardState) {
        Board board = new Board();
        //We skip all space symbols and split string. All this parts need to have 4 symbols.
        String[] sFigures = rawBoardState.replaceAll("\\s", "").split(",");

        for (String rawFigure : sFigures) {
            assert rawFigure.length() == 4;
            //raw figure describe on figure on the board.
            //example: ph00
            //rawFigure  have two parts.
            String figurePart = rawFigure.substring(0, 2); //first part describe figure state [FigureType,Orientation]
            String coordinatePart = rawFigure.substring(2, 4); //second part describe figure coordinate [X,Y]

            BoardCoordinate bc = BoardCoordinate.parse(coordinatePart);
            board.getSquare(bc).figure = FigureFactory.build(figurePart);
        }
        return board;
    }

    /**
     * This method creates squares and fill its by specific color.
     * Color pattern defined in game rules
     */
    private void initBoardColor() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Color squareColor;
                if (x == 0 || (x == 8 && (y == 0 || y == 7))) {
                    squareColor = RED;
                } else if (x == 9 || (x == 1 && (y == 0 || y == 7))) {
                    squareColor = WHITE;
                } else {
                    squareColor = NEUTRAL;
                }
                squares[y][x] = new Square(squareColor);
            }
        }
    }

    public Square getSquare(BoardCoordinate bc) {
        return squares[bc.getY()][bc.getX()];
    }

    /**
     * Return true if coordinate out of bounds.
     */
    public boolean isOutOfBounds(BoardCoordinate bc) {
        return bc.getX() < 0 || bc.getX() >= getWidth() || bc.getY() < 0 || bc.getY() >= getHeight();
    }


    /**
     * @param figure Figure object
     * @return Coordinate of this object on the board.
     * WARING!!! This method compare figures by reference
     */
    public BoardCoordinate getFigureCoordinates(Figure figure) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                BoardCoordinate boardCoordinate = new BoardCoordinate(x, y);
                if (getSquare(boardCoordinate).figure == figure) {
                    return boardCoordinate;
                }
            }
        }

        return null;
    }

    /**
     * Return figure witch will be killed by laser witch is located in rayCoordinate and have rayDirection.
     * If ray will not kill any figure, method return null
     */
    public Figure getFigureToBeKilledByLaser(BoardCoordinate rayCoordinate, Orientation rayDirection) {
        if (isOutOfBounds(rayCoordinate)) {
            return null;
        }

        Figure figure = getSquare(rayCoordinate).figure;
        if (figure != null) {
            if (figure.isKilledByLaser(rayDirection)) {
                return figure;
            }
            if (figure.isReflectLaser(rayDirection)) {
                //Laser ray receives new orientation
                Orientation newOrientation = figure.getDirectionOfReflectedLaser(rayDirection);
                return getFigureToBeKilledByLaser(rayCoordinate.add(newOrientation), newOrientation);
            }

            return null;
        } else {
            //Otherwise do one step in current direction
            return getFigureToBeKilledByLaser(rayCoordinate.add(rayDirection), rayDirection);
        }
    }

    public Figure getFigureToBeKilledBySphinx(Color myColor) {
        Figure mySphinx = getMySphinx(myColor);
        if (mySphinx == null) {
            return null;
        }
        //If my sphinx is live, I send ray.
        BoardCoordinate sphinxCoordinate = getFigureCoordinates(mySphinx);
        BoardCoordinate startLaserRayCoordinate = sphinxCoordinate.add(mySphinx.orientation);
        return getFigureToBeKilledByLaser(startLaserRayCoordinate, mySphinx.orientation);
    }


    public Figure killByLaser(BoardCoordinate rayCoordinate, Orientation rayDirection) {
        if (isOutOfBounds(rayCoordinate)) {
            return null;
        }

        Figure figure = getSquare(rayCoordinate).figure;
        if (figure != null) {
            if (figure.isKilledByLaser(rayDirection)) {
                squares[rayCoordinate.getY()][rayCoordinate.getX()].figure = null;
                return figure;
            }
            if (figure.isReflectLaser(rayDirection)) {
                //Laser ray receives new orientation
                Orientation newOrientation = figure.getDirectionOfReflectedLaser(rayDirection);
                return killByLaser(rayCoordinate.add(newOrientation), newOrientation);
            }

            return null;
        } else {
            //Otherwise do one step in current direction
            return killByLaser(rayCoordinate.add(rayDirection), rayDirection);
        }
    }

    public Figure killBySphinx(Color myColor) {
        Figure mySphinx = getMySphinx(myColor);
        if (mySphinx == null) {
            return null;
        }
        //If my sphinx is live, I send ray.
        BoardCoordinate sphinxCoordinate = getFigureCoordinates(mySphinx);
        BoardCoordinate startLaserRayCoordinate = sphinxCoordinate.add(mySphinx.orientation);
        return killByLaser(startLaserRayCoordinate, mySphinx.orientation);
    }
    
    private Figure getMySphinx(Color myColor) {
        BoardCoordinate boardCoordinate = (myColor == Color.WHITE) ? new BoardCoordinate(9, 7) : new BoardCoordinate(0, 0);
        Figure figure = getSquare(boardCoordinate).figure;
        
        if (figure instanceof Sphinx && figure.color == myColor) {
            return figure;
        }
        return null;
    }
    
    public boolean isMoveValid(Rotate rotate, Color myColor) {
        if (isOutOfBounds(rotate.getOrigin())) {
            return false;
        }
        
        Figure figure = getSquare(rotate.getOrigin()).figure;
        if (figure == null) return false;
        if (figure.color != myColor) return false;
        if (!(figure instanceof Sphinx)) return true; 

        Rotation r = rotate.getRotation();
        Orientation o = figure.getOrientation();
        Color c = figure.getColor();

        if (((c==Color.WHITE) && (o==Orientation.NORTH) && (r==Rotation.CLOCKWISE))
          ||((c==Color.WHITE) && (o==Orientation.WEST)  && (r==Rotation.COUNTERCLOCKWISE))
          ||((c==Color.RED)   && (o==Orientation.SOUTH) && (r==Rotation.CLOCKWISE))
          ||((c==Color.RED)   && (o==Orientation.EAST)  && (r==Rotation.COUNTERCLOCKWISE)))
        {
            return false;
        }

        return true;
    }

    public boolean isMoveValid(Shift shift, Color myColor) {
        if (isOutOfBounds(shift.origin) || isOutOfBounds(shift.destination)) {
            return false;
        }

        Square origin = getSquare(shift.origin);
        Figure figure = origin.figure;

        if (figure == null) {
            return false;
        }

        if (figure.color != myColor || figure instanceof Sphinx) {
            return false;
        }

        if (Math.abs(shift.origin.getX() - shift.destination.getX()) > 1
                || Math.abs(shift.origin.getY() - shift.destination.getY()) > 1
                || shift.origin.equals(shift.destination)) {
            return false;
        }

        Square destination = getSquare(shift.destination);
        if (!destination.isAvailable(figure.color)) {
            return false;
        }

        Figure destinationFigure = destination.figure;
        if (destinationFigure == null) {
            return true;
        }

        /*
            If destination cell isn't empty,
            only Scarab can be swapped with Pyramid and Anubis.
         */
        return figure instanceof Scarab
                && (destinationFigure instanceof Pyramid || destinationFigure instanceof Anubis)
                && origin.isAvailable(destinationFigure.color);
    }


    /**
     * This method check all possible moves of all my figures, and return available.
     */
    public List<Move> getAvailableMoves(Color myColor) {
        List<Move> list = new ArrayList<>();
        Rotation[] rotations = { Rotation.CLOCKWISE, Rotation.COUNTERCLOCKWISE };

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                BoardCoordinate origin = new BoardCoordinate(x, y);
                Figure figure = getSquare(origin).figure;
                if (figure != null && figure.color == myColor) {
                    for(Rotation rot : rotations) {
                        Rotate r =  new Rotate(origin, rot);
                        if (isMoveValid(r, myColor)) {
                            list.add(r);
                        }
                    }

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            Shift move = new Shift(origin, new BoardCoordinate(origin.getX() + dx, origin.getY() + dy));
                            if (isMoveValid(move, myColor)) {
                                list.add(move);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * similar to getAvailableMoves but returns a lot more- all we need for the alpha beta
     */
    public State getState(Color myColor) {
        List<Move> myAvailableMoves = new ArrayList<>(),
                enemyAvailableMoves = new ArrayList<>();
        Rotation[] rotations = { Rotation.CLOCKWISE, Rotation.COUNTERCLOCKWISE };
        short[] allies = {0, 0, 0}, enemies = {0, 0, 0};
        short myPharaohDefense = 0, enemyPharaohDefense = 0;
        BoardCoordinate myPharaohLocation = null, enemyPharaohLocation = null;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                BoardCoordinate origin = new BoardCoordinate(x, y);
                Figure figure = getSquare(origin).figure;
                if (figure != null) {
                    if (figure.color == myColor) {
                        // count allies
                        if (figure instanceof Pyramid) {
                            allies[0]++;
                        } else if (figure instanceof Anubis) {
                            allies[1]++;
                        } else if (figure instanceof Pharaoh) {
                            allies[2]++;
                            myPharaohDefense = countPharaohDefenses(myColor, origin);
                        }

                        // check for available moves
                        for(Rotation rot : rotations) {
                            Rotate r =  new Rotate(origin, rot);
                            if (isMoveValid(r, myColor)) {
                                myAvailableMoves.add(r);
                            }
                        }
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                Shift move = new Shift(origin,
                                                       new BoardCoordinate(origin.getX() + dx, origin.getY() + dy));
                                if (isMoveValid(move, myColor)) {
                                    myAvailableMoves.add(move);
                                }
                            }
                        }
                    } else if (figure.color == Color.getRival(myColor)) {
                        // count enemies
                        if (figure instanceof Pyramid) {
                            enemies[0]++;
                        } else if (figure instanceof Anubis) {
                            enemies[1]++;
                        } else if (figure instanceof Pharaoh) {
                            enemies[2]++;
                            enemyPharaohDefense = countPharaohDefenses(Color.getRival(myColor), origin);
                        }

                        // check for available moves
                        for(Rotation rot : rotations) {
                            Rotate r =  new Rotate(origin, rot);
                            if (isMoveValid(r, figure.color)) {
                                enemyAvailableMoves.add(r);
                            }
                        }
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                Shift move = new Shift(origin,
                                        new BoardCoordinate(origin.getX() + dx, origin.getY() + dy));
                                if (isMoveValid(move, figure.color)) {
                                    enemyAvailableMoves.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }

        return new State(myAvailableMoves, enemyAvailableMoves, myPharaohDefense,
                         enemyPharaohDefense, enemies, allies);
    }

    /**
     *
     * @param orientation
     * @param location
     * @param target
     * @return
     */
    private boolean facingCoordinate(Orientation orientation, BoardCoordinate location, BoardCoordinate target) {
        if (target.getX() == location.getX()) {
            if (target.getY() < location.getY()) {
                return orientation == Orientation.NORTH || orientation == Orientation.EAST;
            } else {
                return orientation == Orientation.SOUTH || orientation == Orientation.WEST;
            }
        }
        if (target.getX() < location.getX()) {
            return orientation == Orientation.EAST || orientation == Orientation.SOUTH;
        } else {
            return orientation == Orientation.WEST || orientation == Orientation.NORTH;
        }
    }

    /**
     *
     * @param myColor
     * @param pharaohLocation
     * @return
     */
    public short countPharaohDefenses(Color myColor, BoardCoordinate pharaohLocation) {
        short guards = 0;
        int x = pharaohLocation.getX(), y = pharaohLocation.getY();
        BoardCoordinate[] guardsLoc = new BoardCoordinate[] {
                                                                new BoardCoordinate(x + 1, y),
                                                                new BoardCoordinate(x, y + 1),
                                                                new BoardCoordinate(x - 1, y),
                                                                new BoardCoordinate(x, y - 1)
                                                             };
        for (int i = 0; i < 4; i++) {
            if (isOutOfBounds(guardsLoc[i])) {
                guards++;
            } else {
                if (squares[guardsLoc[i].getY()][guardsLoc[i].getX()].figure != null) {
                    Figure defense = squares[guardsLoc[i].getY()][guardsLoc[i].getX()].figure;
                    if (defense instanceof Anubis || defense instanceof Pharaoh) {
                        guards++;
                    } else if (defense instanceof Pyramid && !facingCoordinate(defense.orientation, guardsLoc[i],
                                                                               pharaohLocation)) {
                        guards++;
                    }
                }
            }
        }
        return guards;
    }

    /**
     * iterator of next move, out of available move
     *
     */
    public Move nextMove(Color myColor) {
        return null;
    }

    public void initMoveIter(Color myColor) {
    }

    /**
     * This method apply move figure rotation, and change board state.
     */
    public void makeMove(Move move, Color myColor) {
    	//ToDo remove figure from figures list if died
        if (move instanceof Rotate) {
            Rotate rotate = (Rotate) move;
            if (!isMoveValid(rotate, myColor)) {
                throw new RuntimeException(String.format("Not valid move:%s", rotate));
            }
            getSquare(rotate.getOrigin()).figure.doTurn(rotate.getRotation());

        } else {
            Shift shift = (Shift) move;
            if (!isMoveValid(shift, myColor)) {
                throw new RuntimeException(String.format("Not valid move:%s", shift));
            }
            Square origin = getSquare(shift.origin);
            Square destination = getSquare(shift.destination);
        /*
            We swap figures from origin and destination. If destination cell is empty, we swap origin figure with null.
         */
            Figure temp = origin.figure;
            origin.figure = destination.figure;
            destination.figure = temp;
        }
    }

    /**
     * This method apply move figure rotation, and change board state.
     */
    public void makeMoveNew(Move move, Color myColor) {
        //ToDo remove figure from figures list if died
        if (move instanceof Rotate) {
            Rotate rotate = (Rotate) move;
            if (!isMoveValid(rotate, myColor)) {
                throw new RuntimeException(String.format("Not valid move:%s", rotate));
            }
            getSquare(rotate.getOrigin()).figure.doTurn(rotate.getRotation());

        } else {
            Shift shift = (Shift) move;
            if (!isMoveValid(shift, myColor)) {
                throw new RuntimeException(String.format("Not valid move:%s", shift));
            }
            Square origin = getSquare(shift.origin);
            Square destination = getSquare(shift.destination);
        /*
            We swap figures from origin and destination. If destination cell is empty, we swap origin figure with null.
         */
            Figure temp = origin.figure;
            origin.figure = destination.figure;
            destination.figure = temp;
        }

        killBySphinx(myColor);
    }

    public Board clone() {
        Board newBoard = new Board();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Figure newFigure = null;
                if (squares[y][x].figure != null) {
                    newFigure = copyFigure(squares[y][x].figure);
                }

                Square newSquare = new Square(squares[y][x].color);
                newSquare.figure = newFigure;

                newBoard.squares[y][x] = newSquare;
            }
        }

        return newBoard;
    }

    private Figure copyFigure(Figure figure) {

        if (figure instanceof Anubis) {
            return new Anubis(figure.getOrientation(), figure.getColor());
        } else if (figure instanceof Pharaoh) {
            return new Pharaoh(figure.getOrientation(), figure.getColor());
        } else if (figure instanceof Pyramid) {
            return new Pyramid(figure.getOrientation(), figure.getColor());
        } else if (figure instanceof Scarab) {
            return new Scarab(figure.getOrientation(), figure.getColor());
        } else if (figure instanceof Sphinx) {
            return new Sphinx(figure.getOrientation(), figure.getColor());
        }

        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
