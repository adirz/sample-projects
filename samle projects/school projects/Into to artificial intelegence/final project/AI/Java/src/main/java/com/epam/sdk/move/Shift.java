package com.epam.sdk.move;

import com.epam.sdk.BoardCoordinate;

/**
 * Describe figure shift.
 */
public class Shift implements Move {

    public final BoardCoordinate origin, destination;

    public Shift(BoardCoordinate origin, BoardCoordinate destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public String toString() {
        return String.format("%s%s", origin, destination);
    }
}
