package com.epam.bot;

import com.epam.sdk.Board;
import com.epam.sdk.Color;
import com.epam.sdk.IBot;
import com.epam.sdk.move.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


/**
 * User bot implementation.
 * You need to implements your logic in method nextMove().
 * You can build example in class DefaultBot
 */
public class UserBot implements IBot {
    private static final Logger LOG = LoggerFactory.getLogger(UserBot.class);
    private final Random random = new Random();

    @Override
    public String nextMove(Board b, Color c) {
        List<Move> moves = b.getAvailableMoves(c);

        if (moves.size() == 0) {
            return null;
        }
        int index = random.nextInt(moves.size());
        Move move = moves.get(index);

        LOG.info("[UserBot] color: {} move: {}\n", c.toString(), move);
        return move.toString();
    }
}
