package com.epam.sdk;

import com.epam.sdk.figures.Figure;

import java.io.Serializable;

/**
 * Created by numitus on 11/11/14.
 */
public class Square implements Serializable {
    public final Color color;
    public Figure figure;

    public Square(Color color) {
        this.color = color;
    }

    public boolean isAvailable(Color myColor) {
        return this.color == Color.NEUTRAL || this.color == myColor;
    }
}
