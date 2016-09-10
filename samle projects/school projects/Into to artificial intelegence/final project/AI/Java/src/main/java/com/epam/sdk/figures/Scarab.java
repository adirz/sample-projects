package com.epam.sdk.figures;

import com.epam.sdk.Color;
import com.epam.sdk.Orientation;

/**
 * Scarab if a figure witch always reflectLaser.
 * Scarab hasn't head and tail.
 */
public class Scarab extends Figure {
    public Scarab(Orientation orientation, Color color) {
        super(orientation, color);
    }

    public boolean isReflectLaser(Orientation orientation) {
        return true;
    }

    public boolean isKilledByLaser(Orientation orientation) {
        return false;
    }

    public Orientation getDirectionOfReflectedLaser(Orientation orientation) {
        return (this.orientation == orientation || this.orientation == orientation.reverse())
                ? orientation.clockwise()
                : orientation.counterclockwise();
    }
}

