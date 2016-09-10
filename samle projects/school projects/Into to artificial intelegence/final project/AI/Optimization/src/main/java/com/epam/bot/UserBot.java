package com.epam.bot;

import com.epam.runner.Runner;
import com.epam.sdk.Board;
import com.epam.sdk.Color;
import com.epam.sdk.IBot;
import com.epam.sdk.Tuple;
import com.epam.sdk.move.Move;
import com.epam.sdk.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.modifiers.Grades;

import java.lang.Math;

import java.util.List;


/**
 * An alpha- beta agent for the khet game
 */
public class UserBot extends IBot {
    // logs our moves
    private static final Logger LOG = LoggerFactory.getLogger(UserBot.class);

    // the time we were asked to move (used to save us from runing out)
    private long startTime;

    // how much time we asstimate it will take to quit
    public static final long QUIT_TIME = 5;

    // the after wich we think we jave enough time to go even deeper
    public static final int SMALL_BRANCHING_BAR = 900;

    // how much move to the future will we check
    public static final double MAX_DEPTH = 4;

    // the index of the pharaoh in the enemies/allies
    private static final short PHARAOH_INDEX = 2;

    /**
     * a constructor
     */
    public UserBot() {
        this.grades = new Grades();
    }

    /**
     * copy constructor
     * @param grades
     */
    public UserBot(Grades grades) {
        this.grades = grades;
    }

    /**
    * logs our moves
    */
    private Move log(Move move, Color color) {
        LOG.debug("[UserBot] color: {} move: {}\n", color.toString(), move);
        return move;
    }


    /**
     * runs a minimax alpha beta with our heuristic greediness
     */
    private Tuple<Double, Move> miniMaxAB(Board board, Color color, boolean maxPlayer,
                                          double depth, double alpha, double beta) {
        State state = board.getState(color);
        List<Move> availableMoves = state.getMyAvailableMoves();
        short[] allies = state.getAllies(), enemies = state.getEnemies();

        // Is this state terminal?
        if (availableMoves.size() == 0) {
            return null;
        }
        if (allies[PHARAOH_INDEX] == 0) {
            return new Tuple<>(-bestScore(maxPlayer), null);
        } else if (enemies[PHARAOH_INDEX] == 0) {
            return new Tuple<>(bestScore(maxPlayer), null);
        }
        if (depth <= 0) {
            return new Tuple<>(stateGrader(state, maxPlayer), null);
        }
			
		  if (availableMoves.size() * state.getEnemyAvailableMoves().size() < SMALL_BRANCHING_BAR) {
		  		depth -= 0.74;
		  } else {
        		depth--;
		  }
        Tuple<Double, Move> bestNode = new Tuple<>(-bestScore(maxPlayer), chooseRandomMove(availableMoves));

        for (Move move : availableMoves) {
            if (System.currentTimeMillis() - startTime >= Runner.MAX_MOVE_TIME - QUIT_TIME) {
                return bestNode;
            }
            // creating child node
            Board newBoard = board.clone();
            newBoard.makeMoveNew(move, color);

            double tempScore = miniMaxAB(newBoard, Color.getRival(color), !maxPlayer, depth, alpha, beta).x;
            // checking if better than current best worst score
            // changing alpha-beta
            if (maxPlayer) {
                if (tempScore > bestNode.x) {
                    bestNode.x = tempScore;
                    bestNode.y = move;
                }
                alpha = Math.max(alpha, bestNode.x);
            } else {
                if (tempScore < bestNode.x) {
                    bestNode.x = tempScore;
                    bestNode.y = move;
                }
                beta = Math.min(beta, bestNode.x);
            }

            // ending it
            if (alpha >= beta) {
                return bestNode;
            }
        }

        return bestNode;
    }

    /**
     * This function return String view of next Move.
     * In this implementation, we choose random moves, but some moves have a higher priority.
     */
    public String nextMove(Board board, Color myColor) {
        this.startTime = System.currentTimeMillis();
        Move move = miniMaxAB(board, myColor, true, MAX_DEPTH, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).y;
        return log(move, myColor).toString();
    }
}
