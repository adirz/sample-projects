package com.epam.sdk.figures;

import org.junit.Test;

import static com.epam.sdk.Color.WHITE;
import static com.epam.sdk.Orientation.*;
import static org.junit.Assert.*;

public class AnubisTest {

    @Test
    public void testDoesReflectLaserFrom() throws Exception {
        Anubis anubis = new Anubis(NORTH, WHITE);
        assertFalse(anubis.isReflectLaser(SOUTH));
        assertFalse(anubis.isReflectLaser(EAST));
        assertFalse(anubis.isReflectLaser(NORTH));
        assertFalse(anubis.isReflectLaser(WEST));
    }

    @Test
    public void testWillBeKilledByLaserFrom() throws Exception {
        Anubis anubis = new Anubis(NORTH, WHITE);
        assertFalse(anubis.isKilledByLaser(SOUTH));
        assertTrue(anubis.isKilledByLaser(EAST));
        assertTrue(anubis.isKilledByLaser(NORTH));
        assertTrue(anubis.isKilledByLaser(WEST));
    }

    @Test
    public void testGetDirectionOfReflectedLaser() throws Exception {
        try {
            Anubis anubis = new Anubis(NORTH, WHITE);
            anubis.getDirectionOfReflectedLaser(NORTH);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Anubis doesn't reflect lazer from this direction: NORTH");
        }


        try {
            Anubis anubis = new Anubis(NORTH, WHITE);
            anubis.getDirectionOfReflectedLaser(EAST);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Anubis doesn't reflect lazer from this direction: EAST");
        }


        try {
            Anubis anubis = new Anubis(NORTH, WHITE);
            anubis.getDirectionOfReflectedLaser(SOUTH);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Anubis doesn't reflect lazer from this direction: SOUTH");
        }
        try {
            Anubis anubis = new Anubis(NORTH, WHITE);
            anubis.getDirectionOfReflectedLaser(WEST);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Anubis doesn't reflect lazer from this direction: WEST");
        }


    }
}
