package com.epam.sdk.figures;

import com.epam.sdk.Color;
import com.epam.sdk.Orientation;

/**
 * Pyramid reflect by laser way from front and back.
 * Otherwise it dies.
 */
public class Pyramid extends Figure {
    public Pyramid(Orientation orientation, Color color) {
        super(orientation, color);
    }

    public boolean isReflectLaser(Orientation orientation) {
        return this.orientation == orientation.reverse() || this.orientation.clockwise() == orientation;
    }

    public boolean isKilledByLaser(Orientation orientation) {
        return this.orientation != orientation.reverse() && this.orientation.clockwise() != orientation;
    }

    public Orientation getDirectionOfReflectedLaser(Orientation orientation) {
        if (this.orientation == orientation.reverse()) {
            return this.orientation.counterclockwise();
        }
        if (this.orientation == orientation.counterclockwise()) {
            return this.orientation;
        }

        throw new UnsupportedOperationException("Pyramid doesn't reflect lazer from this direction: "
                + orientation.name());
    }

}

