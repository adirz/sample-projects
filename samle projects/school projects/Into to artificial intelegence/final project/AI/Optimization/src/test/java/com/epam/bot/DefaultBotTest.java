package com.epam.bot;

import com.epam.sdk.Board;
import com.epam.sdk.IBot;
import junit.framework.TestCase;

import static com.epam.sdk.Color.RED;

public class DefaultBotTest extends TestCase {

    public void testNextMove() throws Exception {
        IBot defaultBot = new DefaultBot();
        assertNull(defaultBot.nextMove(new Board(), RED));
        String avaliblesMoves[] = {"0001", "0010", "00-1", "00+1", "0011"};
        String move = defaultBot.nextMove(Board.createBoardFromPosition("ph00"), RED);
        assertInArray(move, avaliblesMoves);


    }

    private void assertInArray(Object move, Object[] avaliblesMoves) {
        for (Object possMoves : avaliblesMoves) {
            if (move.equals(possMoves)) {
                return;
            }
        }

        assertTrue(false);

    }
}