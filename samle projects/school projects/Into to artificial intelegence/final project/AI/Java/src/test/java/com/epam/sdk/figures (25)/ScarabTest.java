package com.epam.sdk.figures;

import org.junit.Test;

import static com.epam.sdk.Color.RED;
import static com.epam.sdk.Orientation.*;
import static org.junit.Assert.*;

public class ScarabTest {

    @Test
    public void testDoesReflectLaserFrom() throws Exception {
        Scarab scarab = new Scarab(NORTH, RED);
        assertTrue(scarab.isReflectLaser(SOUTH));
        assertTrue(scarab.isReflectLaser(EAST));
        assertTrue(scarab.isReflectLaser(NORTH));
        assertTrue(scarab.isReflectLaser(WEST));
    }

    @Test
    public void testWillBeKilledByLaserFrom() throws Exception {
        Scarab scarab = new Scarab(NORTH, RED);
        assertFalse(scarab.isKilledByLaser(SOUTH));
        assertFalse(scarab.isKilledByLaser(EAST));
        assertFalse(scarab.isKilledByLaser(NORTH));
        assertFalse(scarab.isKilledByLaser(WEST));
    }

    @Test
    public void testGetOrientationOfReflectedLaserFrom() throws Exception {
        Scarab scarab = new Scarab(NORTH, RED);
        assertEquals(scarab.getDirectionOfReflectedLaser(SOUTH), WEST);
        assertEquals(scarab.getDirectionOfReflectedLaser(EAST), NORTH);
        assertEquals(scarab.getDirectionOfReflectedLaser(NORTH), EAST);
        assertEquals(scarab.getDirectionOfReflectedLaser(WEST), SOUTH);
    }
}
