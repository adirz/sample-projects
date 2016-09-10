package com.epam.sdk.figures;

import com.epam.sdk.Rotation;
import junit.framework.TestCase;

import static com.epam.sdk.Color.*;
import static com.epam.sdk.Orientation.*;

public class FigureTest extends TestCase {

    public void testEquals() throws Exception {
        Figure figure = new Anubis(WEST, WHITE);
        Figure figure2 = new Anubis(WEST, WHITE);
        Figure figure3 = new Anubis(SOUTH, WHITE);
        Figure figure4 = new Anubis(WEST, RED);
        Figure figure5 = new Scarab(WEST, RED);

        assertTrue(figure.equals(figure2));
        assertFalse(figure.equals(null));
        assertFalse(figure.equals(figure3));
        assertFalse(figure.equals(figure4));
        assertFalse(figure.equals(figure5));


    }

    public void testDoTurn() throws Exception {
        Figure figure = new Anubis(WEST, WHITE);
        figure.doTurn(Rotation.CLOCKWISE);
        assertEquals(figure.getOrientation(), NORTH);

        figure.doTurn(Rotation.CLOCKWISE);
        assertEquals(figure.getOrientation(), EAST);


        figure.doTurn(Rotation.CLOCKWISE);
        assertEquals(figure.getOrientation(), SOUTH);


        figure.doTurn(Rotation.CLOCKWISE);
        assertEquals(figure.getOrientation(), WEST);

        figure.doTurn(Rotation.COUNTERCLOCKWISE);
        assertEquals(figure.getOrientation(), SOUTH);

        figure.doTurn(Rotation.COUNTERCLOCKWISE);
        assertEquals(figure.getOrientation(), EAST);


        figure.doTurn(Rotation.COUNTERCLOCKWISE);
        assertEquals(figure.getOrientation(), NORTH);


        figure.doTurn(Rotation.COUNTERCLOCKWISE);
        assertEquals(figure.getOrientation(), WEST);


    }

    public void testIsEnemyColor() throws Exception {
        Figure figure = new Anubis(WEST, WHITE);
        assertTrue(figure.isEnemyColor(RED));
        assertFalse(figure.isEnemyColor(WHITE));
        assertFalse(figure.isEnemyColor(NEUTRAL));


    }

    public void testSetOrientation() throws Exception {
        Figure figure = new Anubis(WEST, WHITE);
        assertEquals(figure.getOrientation(), WEST);
        figure.setOrientation(EAST);
        assertEquals(figure.getOrientation(), EAST);

    }

    public void testGetColor() throws Exception {
        Figure figure = new Anubis(WEST, WHITE);
        assertEquals(figure.getColor(), WHITE);

    }

}