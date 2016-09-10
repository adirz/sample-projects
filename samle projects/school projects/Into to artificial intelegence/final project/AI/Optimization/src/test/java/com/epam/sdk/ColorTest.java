package com.epam.sdk;

import junit.framework.TestCase;

import static com.epam.sdk.Color.*;

public class ColorTest extends TestCase {

    public void testIntToEnum() throws Exception {
        assertEquals(intToEnum(1), WHITE);
        assertEquals(intToEnum(2), RED);
        assertEquals(intToEnum(3), NEUTRAL);
        try {
            intToEnum(4);
            assertTrue(true);
        } catch (Exception e) {

        }
    }

    public void testToString() throws Exception {
        assertEquals(WHITE.toString(), "1");
        assertEquals(RED.toString(), "2");
        assertEquals(NEUTRAL.toString(), "3");
    }
}