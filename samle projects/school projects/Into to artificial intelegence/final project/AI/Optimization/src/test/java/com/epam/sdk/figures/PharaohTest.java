package com.epam.sdk.figures;

import junit.framework.TestCase;

import static com.epam.sdk.Color.WHITE;
import static com.epam.sdk.Orientation.WEST;

public class PharaohTest extends TestCase {

    public void testIsReflectLaser() throws Exception {
        Figure p = new Pharaoh(WEST, WHITE);
        assertFalse(p.isReflectLaser(WEST));


    }

    public void testIsKilledByLaser() throws Exception {
        Figure p = new Pharaoh(WEST, WHITE);
        assertTrue(p.isKilledByLaser(WEST));

    }

    public void testGetDirectionOfReflectedLaser() throws Exception {
        Figure p = new Pharaoh(WEST, WHITE);
        try {
            p.getDirectionOfReflectedLaser(WEST);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Pharaoh doesn't reflect lazer from this direction: WEST");
        }
    }
}