package com.epam.sdk;

import org.junit.Test;

import static com.epam.sdk.Orientation.*;
import static org.junit.Assert.assertEquals;

public class OrientationTest {

    @Test
    public void testReverse() throws Exception {
        assertEquals(NORTH.reverse(), SOUTH);
        assertEquals(EAST.reverse(), WEST);
        assertEquals(SOUTH.reverse(), NORTH);
        assertEquals(WEST.reverse(), EAST);
    }

    @Test
    public void testClockwise() throws Exception {
        assertEquals(NORTH.clockwise(), EAST);
        assertEquals(EAST.clockwise(), SOUTH);
        assertEquals(SOUTH.clockwise(), WEST);
        assertEquals(WEST.clockwise(), NORTH);
    }

    @Test
    public void testCounterclockwise() throws Exception {
        assertEquals(NORTH.counterclockwise(), WEST);
        assertEquals(EAST.counterclockwise(), NORTH);
        assertEquals(SOUTH.counterclockwise(), EAST);
        assertEquals(WEST.counterclockwise(), SOUTH);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(NORTH.toString(), "1");
        assertEquals(EAST.toString(), "2");
        assertEquals(SOUTH.toString(), "3");
        assertEquals(WEST.toString(), "4");
    }
}
