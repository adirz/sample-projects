package com.epam.sdk;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardCoordinateTest {

    @Test
    public void testEquals() throws Exception {
        BoardCoordinate bc1 = new BoardCoordinate(1, 2);
        BoardCoordinate bc2 = new BoardCoordinate(1, 2);
        BoardCoordinate bc3 = new BoardCoordinate(2, 1);

        assertEquals(bc1, bc1);
        assertEquals(bc2, bc2);
        assertEquals(bc1, bc2);
        assertNotEquals(bc1, bc3);
        assertNotEquals(bc1, null);
        assertNotEquals(bc1, new Integer(2));


    }

    @Test
    public void testGet() throws Exception {
        assertEquals(new BoardCoordinate(2, 3), new BoardCoordinate(2, 3));
    }

    @Test
    public void testToString() throws Exception {
        BoardCoordinate bc = new BoardCoordinate(1, 2);

        assertEquals(bc.toString(), "21");
    }

    @Test
    public void testParse() throws Exception {
        BoardCoordinate bc = BoardCoordinate.parse("32");

        assertEquals(new BoardCoordinate(2, 3), bc);

        try {
            bc = BoardCoordinate.parse("322");
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Board coordinate must have 2 symbol");
        }

    }

    @Test
    public void testAdd() throws Exception {
        BoardCoordinate bc1 = new BoardCoordinate(2, 2);
        BoardCoordinate bc2 = bc1.add(Orientation.SOUTH);
        BoardCoordinate bc3 = new BoardCoordinate(2, 3);
        assertEquals(bc2, bc3);

        bc2 = bc1.add(Orientation.NORTH);
        bc3 = new BoardCoordinate(2, 1);
        assertEquals(bc2, bc3);

        bc2 = bc1.add(Orientation.EAST);
        bc3 = new BoardCoordinate(3, 2);
        assertEquals(bc2, bc3);

        bc2 = bc1.add(Orientation.WEST);
        bc3 = new BoardCoordinate(1, 2);
        assertEquals(bc2, bc3);

        assertNotNull(bc1.add(null));
    }
}
