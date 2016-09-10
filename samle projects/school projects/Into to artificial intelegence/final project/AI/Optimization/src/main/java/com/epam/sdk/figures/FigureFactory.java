package com.epam.sdk.figures;

import com.epam.sdk.Color;
import com.epam.sdk.Orientation;

import static com.epam.sdk.Color.RED;
import static com.epam.sdk.Color.WHITE;
import static com.epam.sdk.Orientation.NORTH;
import static com.epam.sdk.Orientation.getOrientation;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

/**
 * FigureFactory build figure from code.
 */
public class FigureFactory {

    public static Figure build(String figure) {
        try {
            char figureChar = figure.charAt(0);
            char orientationChar = figure.charAt(1);

            Color color = isUpperCase(figureChar) ? WHITE : RED;

            if (toLowerCase(orientationChar) == 'h') {
                //pharaoh always orient to the north
                return new Pharaoh(NORTH, color);
            }

            Orientation orientation = getOrientation(orientationChar);
            switch (toLowerCase(figureChar)) {
                case 'p':
                    return new Pyramid(orientation, color);
                case 'c':
                    return new Scarab(orientation, color);
                case 's':
                    return new Sphinx(orientation, color);
                case 'a':
                    return new Anubis(orientation, color);
                default:
                    throw new RuntimeException();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Illegal figure string: " + figure);
        }
    }
}
