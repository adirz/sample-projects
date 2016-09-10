package com.epam.sdk.figures;

import com.epam.sdk.Color;
import com.epam.sdk.Orientation;

/**
 * Sphinx absorb all laser ray.
 */
public class Sphinx extends Figure {
    public Sphinx(Orientation orientation, Color color) {
        super(orientation, color);
    }

    public boolean isReflectLaser(Orientation orientation) {
        return false;
    }

    public boolean isKilledByLaser(Orientation orientation) {
        return false;
    }

    public Orientation getDirectionOfReflectedLaser(Orientation orientation) {
        throw new UnsupportedOperationException("Sphinx doesn't reflect lazer from this direction: "
                + orientation.name());
    }

}

