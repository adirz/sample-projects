package com.epam.sdk.figures;

import org.junit.Test;

import static com.epam.sdk.Color.RED;
import static com.epam.sdk.Orientation.*;
import static org.junit.Assert.*;

public class PyramidTest {

    @Test
    public void testDoesReflectLaserFrom() throws Exception {

        Pyramid pyramid = new Pyramid(NORTH, RED);
        assertTrue(pyramid.isReflectLaser(SOUTH));
        assertTrue(pyramid.isReflectLaser(EAST));
        assertFalse(pyramid.isReflectLaser(NORTH));
        assertFalse(pyramid.isReflectLaser(WEST));
    }

    @Test
    public void testWillBeKilledByLaserFrom() throws Exception {
        Pyramid pyramid = new Pyramid(NORTH, RED);
        assertFalse(pyramid.isKilledByLaser(SOUTH));
        assertFalse(pyramid.isKilledByLaser(EAST));
        assertTrue(pyramid.isKilledByLaser(NORTH));
        assertTrue(pyramid.isKilledByLaser(WEST));
    }

    @Test
    public void testGetOrientationOfReflectedLaserFrom() throws Exception {
        Pyramid pyramid = new Pyramid(NORTH, RED);
        assertEquals(pyramid.getDirectionOfReflectedLaser(SOUTH), WEST);
        assertEquals(pyramid.getDirectionOfReflectedLaser(EAST), NORTH);


        try {
            pyramid.getDirectionOfReflectedLaser(NORTH);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Pyramid doesn't reflect lazer from this direction: NORTH");
        }


        try {
            pyramid.getDirectionOfReflectedLaser(WEST);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Pyramid doesn't reflect lazer from this direction: WEST");
        }
    }
}
