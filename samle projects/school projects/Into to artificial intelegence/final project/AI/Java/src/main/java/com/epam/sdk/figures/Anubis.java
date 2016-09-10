package com.epam.sdk.figures;

import com.epam.sdk.Color;
import com.epam.sdk.Orientation;

/**
 * Anubis is a figure, witch doesn't reflect laser, but if laser ray falls on the figure front,
 * figure absorbs laser ray, but doesn't die.
 */
public class Anubis extends Figure {
    public Anubis(Orientation orientation, Color color) {
        super(orientation, color);
    }
    
    
    public boolean isReflectLaser(Orientation orientation) {
        return false;
    }
    
    public boolean isKilledByLaser(Orientation orientation) {
        return this.orientation != orientation.reverse();
    }

    public Orientation getDirectionOfReflectedLaser(Orientation orientation) {
        throw new UnsupportedOperationException("Anubis doesn't reflect lazer from this direction: "
                + orientation.name());
    }

}

