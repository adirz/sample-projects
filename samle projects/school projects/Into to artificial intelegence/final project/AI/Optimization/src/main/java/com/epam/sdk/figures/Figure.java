package com.epam.sdk.figures;

import com.epam.sdk.Color;
import com.epam.sdk.Orientation;
import com.epam.sdk.Rotation;
import com.epam.sdk.Tuple;

import java.io.Serializable;

import static com.epam.sdk.Color.NEUTRAL;
import static com.epam.sdk.Rotation.CLOCKWISE;

/**
 * This class implements game figure. Every figure have orientation and color.
 * Figure doesn't know where it place.
 */
public abstract class Figure implements Serializable {
    public final Color color;
    public Orientation orientation;
//    private Tuple<int, int> location;

    public Figure(Orientation orientation, Color color) {
        this.orientation = orientation;
        this.color = color;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Color getColor() {
        return color;
    }

//    public Tuple<int, int> getLocation(){return location;}
//    public void setLocation(Tuple<int, int> newLoc){location = newLoc;}


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Figure figure = (Figure) o;

        return color == figure.color && orientation == figure.orientation;

    }

    public void doTurn(Rotation rotation) {
        if (rotation == CLOCKWISE) {
            orientation = orientation.clockwise();
        } else {
            orientation = orientation.counterclockwise();
        }
    }

    public abstract boolean isReflectLaser(Orientation orientation);

    /**
     * To check if Figure will be deleted by current Laser.
     *
     * @param orientation
     * @return true when will be dead
     */
    public abstract boolean isKilledByLaser(Orientation orientation);

    public abstract Orientation getDirectionOfReflectedLaser(Orientation orientation);

    public boolean isEnemyColor(Color color) {
        return this.color != color && color != NEUTRAL;
    }

    public boolean isMyColor(Color color) {
        return this.color == color;
    }
}
