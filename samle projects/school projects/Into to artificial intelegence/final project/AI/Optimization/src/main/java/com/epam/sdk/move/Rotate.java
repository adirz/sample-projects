package com.epam.sdk.move;

import com.epam.sdk.BoardCoordinate;
import com.epam.sdk.Rotation;

/**
 * Describe figure doTurn.
 */
public class Rotate implements Move {
    private final BoardCoordinate origin;
    private final Rotation rotation;

    public Rotate(BoardCoordinate origin, Rotation rotation) {
        this.origin = origin;
        this.rotation = rotation;
    }

    public BoardCoordinate getOrigin() {
        return origin;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public String toString() {
        return String.format("%s%s", origin, rotation.toString());
    }
}
