package com.epam.sdk;

import com.epam.sdk.figures.Pyramid;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.epam.sdk.Color.WHITE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameStateTest{

    @Test
    public void testParse() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("color", 1);
        data.put("board", "p123");

        GameState gameState = GameState.parse(data);
        assertEquals(gameState.getColor(), WHITE);
        assertTrue(gameState.getBoard().getSquare(new BoardCoordinate(3, 2)).figure instanceof Pyramid);
    }
}