package com.epam.sdk;

/**
 * Rotation define rotation direction.
 */
public enum Rotation {
    CLOCKWISE("+1"),
    COUNTERCLOCKWISE("-1");
    private final String code;

    Rotation(final String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
