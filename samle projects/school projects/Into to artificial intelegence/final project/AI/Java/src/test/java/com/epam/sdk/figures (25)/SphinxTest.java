package com.epam.sdk.figures;

import junit.framework.TestCase;

import static com.epam.sdk.Color.WHITE;
import static com.epam.sdk.Orientation.NORTH;

public class SphinxTest extends TestCase {

    public void testIsReflectLaser() throws Exception {
        Figure sphinx = new Sphinx(NORTH, WHITE);
        assertFalse(sphinx.isReflectLaser(NORTH));
    }

    public void testIsKilledByLaser() throws Exception {
        Figure sphinx = new Sphinx(NORTH, WHITE);
        assertFalse(sphinx.isKilledByLaser(NORTH));

    }

    public void testGetDirectionOfReflectedLaser() throws Exception {
        Figure sphinx = new Sphinx(NORTH, WHITE);
        try {
            sphinx.getDirectionOfReflectedLaser(NORTH);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Sphinx doesn't reflect lazer from this direction: NORTH");
        }


    }
}