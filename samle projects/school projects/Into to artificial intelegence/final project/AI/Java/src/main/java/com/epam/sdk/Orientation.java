package com.epam.sdk;

/**
 * Class witch describes orientation, and implements simple operation.
 */
public enum Orientation {
    NORTH('1') {
        public Orientation reverse() {
            return SOUTH;
        }

        public Orientation clockwise() {
            return EAST;
        }

        public Orientation counterclockwise() {
            return WEST;
        }

        @Override
        public int getDx() {
            return 0;
        }

        @Override
        public int getDy() {
            return -1;
        }
    },
    EAST('2') {
        public Orientation reverse() {
            return WEST;
        }

        public Orientation clockwise() {
            return SOUTH;
        }

        public Orientation counterclockwise() {
            return NORTH;
        }

        @Override
        public int getDx() {
            return 1;
        }

        @Override
        public int getDy() {
            return 0;
        }
    },
    SOUTH('3') {
        public Orientation reverse() {
            return NORTH;
        }

        public Orientation clockwise() {
            return WEST;
        }

        public Orientation counterclockwise() {
            return EAST;
        }

        @Override
        public int getDx() {
            return 0;
        }

        @Override
        public int getDy() {
            return 1;
        }
    },
    WEST('4') {
        public Orientation reverse() {
            return EAST;
        }

        public Orientation clockwise() {
            return NORTH;
        }

        public Orientation counterclockwise() {
            return SOUTH;
        }

        @Override
        public int getDx() {
            return -1;
        }

        @Override
        public int getDy() {
            return 0;
        }
    };

    private final char code;

    Orientation(char num) {
        this.code = num;
    }

    public static Orientation getOrientation(char code) {
        for (Orientation orientation : Orientation.values()) {
            if (orientation.code == code) {
                return orientation;
            }
        }
        throw new IllegalStateException("There are no supported orientations");
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }

    public abstract Orientation reverse();

    public abstract Orientation clockwise();

    public abstract Orientation counterclockwise();

    /**
     * Method witch return x coordinate shift after one step in current direction.
     */
    public abstract int getDx();

    /**
     * Method witch return y coordinate shift after one step in current direction.
     */
    public abstract int getDy();
}
