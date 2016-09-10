package com.epam.sdk;


/**
 * Created by numitus on 11/10/14.
 */
public enum Color {

    WHITE(1),
    RED(2),
    NEUTRAL(3);

    private final int num;

    Color(int i) {
        this.num = i;
    }

    static Color intToEnum(int code) {
        for (Color color : Color.values()) {
            if (color.num == code) {
                return color;
            }
        }
        throw new RuntimeException("can not convert int to color");

    }

    public static Color getRival(Color color){
        switch (color) {
            case WHITE:
            return RED;
            case RED:
            return WHITE;
            case NEUTRAL:
            return NEUTRAL;
	    }

        throw new RuntimeException("can not convert int to color");
    }

    @Override
    public String toString() {
        return String.valueOf(num);
    }
}
