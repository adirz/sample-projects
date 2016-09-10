package com.epam.bot;

import com.epam.runner.Runner;
import com.epam.sdk.*;
import com.epam.sdk.move.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.modifiers.Grades;

import java.lang.Math;

import java.util.*;


/**
 * Heuristic bot implementation.
 * Based on alpha beta search, its an agent that is greedy about the moves and expands only on those who seems best at the moment.
 * It can look much further into the future than the regular alpha beta search.
 */
public class HeuristicBot extends IBot {
    // logs our moves
    private static final Logger LOG = LoggerFactory.getLogger(HeuristicBot.class);

    // the time we were asked to move (used to save us from runing out)
    private long startTime;

    // how much time we asstimate it will take to quit
    private static final long QUIT_TIME = 10;

    // the branching factor and the number of best moves we'll explore
    private static final int MAX_MOVE_NUM = 10;

    // how much move to the future will we check
    private static final double MAX_DEPTH = 6;

    // a function to compare between state. used to sort the moves
    private static final Comparator whiteComp = new Comparator<Triple<Board, Move, Double>>() {
        @Override
        public int compare(Triple<Board, Move, Double> a, Triple<Board, Move, Double> b) {
            return (int)(a.z - b.z + 0.5);
        }
    };

    private static final Comparator redComp = new Comparator<Triple<Board, Move, Double>>() {
        @Override
        public int compare(Triple<Board, Move, Double> a, Triple<Board, Move, Double> b) {
            return (int)(-a.z + b.z - 0.5);
        }
    };

    // the index of the pharaoh in the enemies/allies
    private static final short PHARAOH_INDEX = 2;

    /**
     * a constructor
     */
    public HeuristicBot() {
        this.grades = new Grades();
    }

    /**
     * a copy constructor
     * @param grades
     */
    public HeuristicBot(Grades grades) {
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

        // Grade all the moves according to the heuristics
        PriorityQueue<Triple<Board, Move, Double>> tempNodes;

        if (color == Color.WHITE) {
            tempNodes = new PriorityQueue<>(MAX_MOVE_NUM, whiteComp);
        } else {
            tempNodes = new PriorityQueue<>(MAX_MOVE_NUM, redComp);
        }

        for (Move move : availableMoves) {
            // creating child node
            Board newBoard = board.clone();
            newBoard.makeMoveNew(move, color);
            double score = stateGrader(newBoard.getState(color), maxPlayer);
            tempNodes.add(new Triple<>(newBoard , move, score));
            if (tempNodes.size() > MAX_MOVE_NUM) {
                tempNodes.poll();
            }
        }

        Triple<Board, Move, Double>[] orderedNodes = new Triple[Math.min(MAX_MOVE_NUM, tempNodes.size())];
        for (int i = orderedNodes.length - 1; i >= 0; i--) {
            orderedNodes[i] = tempNodes.poll();
        }

        //depth = (depth - 1) / availableMoves.size();
        //depth -= Math.sqrt(availableMoves.size() / BRANCHING_FACTOR);
        depth--;
        Tuple<Double, Move> bestNode = new Tuple<>(-bestScore(maxPlayer), chooseRandomMove(availableMoves));

        for (Triple<Board, Move, Double> node : orderedNodes) {
            if (System.currentTimeMillis() - startTime >= Runner.MAX_MOVE_TIME - QUIT_TIME) {
                return bestNode;
            }

            Board newBoard = node.x;
            Move move = node.y;
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
