package com.epam.sdk.move;

import com.epam.sdk.BoardCoordinate;
import junit.framework.TestCase;

public class ShiftTest extends TestCase {

    public void testToString() throws Exception {
        Shift rotate = new Shift(new BoardCoordinate(0, 1), new BoardCoordinate(0, 2));
        assertEquals(rotate.toString(), "1020");
    }
}