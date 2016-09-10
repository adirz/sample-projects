package com.epam.sdk.figures;

import com.epam.sdk.Color;
import com.epam.sdk.Orientation;

/**
 * Pharaoh can't reflect laser, laser always kill him.
 */
public class Pharaoh extends Figure {
    public Pharaoh(Orientation orientation, Color color) {
        super(orientation, color);
    }

    public boolean isReflectLaser(Orientation orientation) {
        return false;
    }

    public boolean isKilledByLaser(Orientation orientation) {
        return true;
    }

    public Orientation getDirectionOfReflectedLaser(Orientation orientation) {
        throw new UnsupportedOperationException("Pharaoh doesn't reflect lazer from this direction: "
                + orientation.name());
    }

}

