package com.epam.sdk;

import com.epam.sdk.figures.*;
import com.epam.sdk.move.Rotate;
import com.epam.sdk.move.Shift;
import org.junit.Test;

import static com.epam.sdk.Color.RED;
import static com.epam.sdk.Color.WHITE;
import static com.epam.sdk.Orientation.*;
import static com.epam.sdk.Rotation.CLOCKWISE;
import static com.epam.sdk.Rotation.COUNTERCLOCKWISE;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testGetSquare() throws Exception {
        Board board = Board.createBoardFromPosition("s300");
        Square square = board.getSquare(new BoardCoordinate(0, 0));
        assertTrue(square.figure instanceof Sphinx);
        assertEquals(square.color, RED);
    }

    @Test
    public void testIsOutOfBounds() throws Exception {
        Board board = new Board();

        assertTrue(board.isOutOfBounds(new BoardCoordinate(-1, 0)));
        assertTrue(board.isOutOfBounds(new BoardCoordinate(10, 0)));
        assertFalse(board.isOutOfBounds(new BoardCoordinate(0, 0)));
    }

    @Test
    public void testGetFigureCoordinates() throws Exception {
        Board board = Board.createBoardFromPosition("s300");
        BoardCoordinate bc = board.getFigureCoordinates(board.getSquare(new BoardCoordinate(0, 0)).figure);
        assertEquals(bc.getX(), 0);
        assertEquals(bc.getY(), 0);
        assertNull(board.getFigureCoordinates(new Sphinx(NORTH, WHITE)));
    }

    @Test
    public void testGetFigureToBeKilledByLaser() throws Exception {
        Board board = Board.createBoardFromPosition("PH 00");
        Figure pharaoh = board.getSquare(new BoardCoordinate(0, 0)).figure;
        Figure figureToBeKilled = board.getFigureToBeKilledByLaser(new BoardCoordinate(1, 0), WEST);

        assertEquals(figureToBeKilled, pharaoh);
        figureToBeKilled = board.getFigureToBeKilledByLaser(new BoardCoordinate(1, 0), EAST);

        assertNull(figureToBeKilled);
    }

    @Test
    public void testGetFigureToBeKilledBySphinx() throws Exception {
        Board board = Board.createBoardFromPosition("s2 00, P4 02, c1 22, p2 20");
        assertNull(board.getFigureToBeKilledBySphinx(RED));

        board = Board.createBoardFromPosition("s2 00, P2 01, p2 02");
        assertEquals(board.getFigureToBeKilledBySphinx(RED), board.getSquare(new BoardCoordinate(1, 0)).figure);

        board = Board.createBoardFromPosition("s3 00, a3 04, ph05, a3 06, p2 07, p4 12, P2 23, p2 30, P3 32, c2 34, p3 37, P1 39, p1 40, C1 44, C2 45, c1 46, p2 47, P4 49, p3 65, P2 67, A1 73, PH 74, A1 75, S2 79");
        assertTrue(board.getFigureToBeKilledBySphinx(RED) instanceof Pyramid);
    }

    @Test
    public void testIsMoveValid() throws Exception {
        Board board = Board.createBoardFromPosition("c300,p310");
        assertTrue(board.isMoveValid(new Shift(new BoardCoordinate(0, 0), new BoardCoordinate(1, 1)), RED));


        assertFalse(board.isMoveValid(new Shift(new BoardCoordinate(2, 2), new BoardCoordinate(1, 1)), RED));
        assertFalse(board.isMoveValid(new Shift(new BoardCoordinate(0, 0), new BoardCoordinate(1, 1)), WHITE));
        assertTrue(board.isMoveValid(new Shift(new BoardCoordinate(0, 0), new BoardCoordinate(0, 1)), RED));
    }

    @Test

    public void testIsMoveValid_Rotate() throws Exception {
        Board board = Board.createBoardFromPosition("a300");
        assertFalse(board.isMoveValid(new Rotate(new BoardCoordinate(10, 110), COUNTERCLOCKWISE), RED));
        assertFalse(board.isMoveValid(new Rotate(new BoardCoordinate(0, 0), COUNTERCLOCKWISE), WHITE));
        assertTrue(board.isMoveValid(new Rotate(new BoardCoordinate(0, 0), COUNTERCLOCKWISE), RED));

    }

    @Test
    public void testGetAvailableMoves() throws Exception {
        Board board = Board.createBoardFromPosition("a300");
        assertTrue(board.getAvailableMoves(RED).size() > 0);
    }

    @Test
    public void testMakeMove() throws Exception {
        Board board = Board.createBoardFromPosition("a300");
        Figure f = board.getSquare(new BoardCoordinate(0, 0)).figure;
        board.makeMove(new Shift(new BoardCoordinate(0, 0), new BoardCoordinate(1, 1)), RED);
        BoardCoordinate bc = board.getFigureCoordinates(f);
        assertEquals(bc.getX(), 1);
        assertEquals(bc.getY(), 1);

        try {
            board.makeMove(new Shift(new BoardCoordinate(110, 110), new BoardCoordinate(111, 111)), RED);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Not valid move:110110111111");
        }

        try {
            board.makeMove(new Rotate(new BoardCoordinate(110, 110), CLOCKWISE), RED);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Not valid move:110110+1");
        }
    }


    @Test
    public void testCreateBoardFromPosition() throws Exception {

        Board board = Board.createBoardFromPosition("P3 42, ph 09");

        assertEquals(new Pyramid(SOUTH, WHITE), board.getSquare(new BoardCoordinate(2, 4)).figure);
        assertEquals(new Pharaoh(NORTH, RED), board.getSquare(new BoardCoordinate(9, 0)).figure);
        assertNull(board.getSquare(new BoardCoordinate(4, 2)).figure);
    }


    public void checkFigure(Board board, int x, int y, Figure figure) {
        assertEquals(board.getSquare(new BoardCoordinate(x, y)).figure, figure);
    }

    @Test
    public void testCreateBoardFromStandartPosition() throws Exception {
        String standardBoardPositon = "PH 74, S1 79, C1 44, C2 45, A1 73, A1 75, P1 23, P1 39, P1 42, P1 72, P2 67, P4 32, P4 49, ph 05, s3 00, c1 35, c2 34, a3 04, a3 06, p2 30, p2 47, p3 07, p3 37, p3 40, p3 56, p4 12";
        Board board = Board.createBoardFromPosition(standardBoardPositon);
        checkFigure(board, 4, 7, new Pharaoh(NORTH, WHITE));
        checkFigure(board, 4, 7, new Pharaoh(NORTH, WHITE));
        checkFigure(board, 9, 7, new Sphinx(NORTH, WHITE));
        checkFigure(board, 4, 4, new Scarab(NORTH, WHITE));
        checkFigure(board, 5, 4, new Scarab(EAST, WHITE));
        checkFigure(board, 3, 7, new Anubis(NORTH, WHITE));
        checkFigure(board, 5, 7, new Anubis(NORTH, WHITE));
        checkFigure(board, 3, 2, new Pyramid(NORTH, WHITE));
        checkFigure(board, 9, 3, new Pyramid(NORTH, WHITE));
        checkFigure(board, 2, 4, new Pyramid(NORTH, WHITE));
        checkFigure(board, 2, 7, new Pyramid(NORTH, WHITE));
        checkFigure(board, 7, 6, new Pyramid(EAST, WHITE));
        checkFigure(board, 2, 3, new Pyramid(WEST, WHITE));
        checkFigure(board, 9, 4, new Pyramid(WEST, WHITE));
        checkFigure(board, 5, 0, new Pharaoh(NORTH, RED));
        checkFigure(board, 0, 0, new Sphinx(SOUTH, RED));
        checkFigure(board, 5, 3, new Scarab(NORTH, RED));
        checkFigure(board, 4, 3, new Scarab(EAST, RED));
        checkFigure(board, 4, 0, new Anubis(SOUTH, RED));
        checkFigure(board, 6, 0, new Anubis(SOUTH, RED));
        checkFigure(board, 0, 3, new Pyramid(EAST, RED));
        checkFigure(board, 7, 4, new Pyramid(EAST, RED));
        checkFigure(board, 7, 0, new Pyramid(SOUTH, RED));
        checkFigure(board, 7, 3, new Pyramid(SOUTH, RED));
        checkFigure(board, 0, 4, new Pyramid(SOUTH, RED));
        checkFigure(board, 6, 5, new Pyramid(SOUTH, RED));
        checkFigure(board, 2, 1, new Pyramid(WEST, RED));
    }

    @Test
    public void testClone() {
        Board board = Board.createBoardFromPosition("P3 42, ph 09");
        assertTrue(board.getSquare(new BoardCoordinate(2, 4)).figure instanceof Pyramid);
        Board boardClone = board.clone();
        assertTrue(boardClone.getSquare(new BoardCoordinate(2, 4)).figure instanceof Pyramid);
        boardClone.getSquare(new BoardCoordinate(2, 4)).figure = new Sphinx(NORTH, RED);
        assertTrue(board.getSquare(new BoardCoordinate(2, 4)).figure instanceof Pyramid);
        assertTrue(boardClone.getSquare(new BoardCoordinate(2, 4)).figure instanceof Sphinx);
    }
}
