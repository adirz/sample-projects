package com.epam.sdk;

import java.util.Map;

/**
 * GameState describe state of game.
 */
public class GameState {
    final Board board;
    Color color;

    private GameState(Board board, Color color) {
        this.board = board;
        this.color = color;
    }

    public static GameState parse(Map<String, Object> body) {
        Color color = Color.intToEnum((Integer) body.get("color"));
        Board board = Board.createBoardFromPosition((String) body.get("board"));
        return new GameState(board, color);
    }

    public Board getBoard() {
        return board;
    }

    public Color getColor() {
        return color;
    }
}
