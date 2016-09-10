package com.epam.sdk.move;

import com.epam.sdk.BoardCoordinate;
import com.epam.sdk.Rotation;
import junit.framework.TestCase;

public class RotateTest extends TestCase {

    public void testToString() throws Exception {
        Rotate rotate = new Rotate(new BoardCoordinate(0, 0), Rotation.COUNTERCLOCKWISE);
        assertEquals(rotate.getOrigin(), new BoardCoordinate(0, 0));
        assertEquals(rotate.getRotation(), Rotation.COUNTERCLOCKWISE);
        assertEquals(rotate.toString(), "00-1");
    }
}
