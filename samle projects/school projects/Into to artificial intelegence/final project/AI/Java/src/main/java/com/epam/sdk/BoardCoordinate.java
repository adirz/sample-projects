package com.epam.sdk;

/**
 * This class only store coordinate. This class is immutable./
 */
public class BoardCoordinate implements Cloneable {
    private final int x;
    private final int y;

    public BoardCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method convert raw string to board coordinate.
     * Raw string have 2 symbols. Every symbols is a number from '0' to '9'.
     * WARING!!! First symbols indicate Y coordinate
     */
    public static BoardCoordinate parse(String bc) {
        if (bc.length() != 2) {
            throw new IllegalArgumentException("Board coordinate must have 2 symbol");
        }

        int y = Integer.parseInt(bc.substring(0, 1));
        int x = Integer.parseInt(bc.substring(1, 2));

        return new BoardCoordinate(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardCoordinate that = (BoardCoordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public String toString() {
        return String.format("%s%s", y, x);
    }

    /**
     * This method move coordinate on one step, in necessary direction, and return new Coordinate
     * WARING!!! This method doesn't change coordinate
     */
    public BoardCoordinate add(Orientation direction) {
        if (direction == null) {
            // do nothing
            return new BoardCoordinate(x, y);
        }

        return new BoardCoordinate(x + direction.getDx(), y + direction.getDy());
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
