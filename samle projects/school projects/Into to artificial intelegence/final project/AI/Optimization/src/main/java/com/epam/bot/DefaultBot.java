package com.epam.bot;

import com.epam.sdk.Board;
import com.epam.sdk.Color;
import com.epam.sdk.IBot;
import com.epam.sdk.figures.Figure;
import com.epam.sdk.figures.Pharaoh;
import com.epam.sdk.move.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;


/**
 * Default Bot implementation.
 */
public class DefaultBot extends IBot {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBot.class);

    private Move log(Move move, Color color) {
        LOG.debug("[DefaultBot] color: {} move: {}\n", color.toString(), move);
        return move;
    }

    /**
     * This function return String view of next Move.
     * In this implementation, we choose random moves, but some moves have a higher priority.
     */
    public String nextMove(Board board, Color myColor) {
        List<Move> availableMoves = board.getAvailableMoves(myColor);
        if (availableMoves.size() == 0) {
            return null;
        }
        /* First, we try to choose the moves, who allow to kill enemy pharaoh by sphinx */
        List<Move> killPharaohMoves = getMovesToKillPharaohBySphinx(board, myColor, availableMoves);
        if (killPharaohMoves.size() > 0) {
            return log(chooseRandomMove(killPharaohMoves), myColor).toString();
        }
        /* If does not exist such moves, we try to kill someone*/
        List<Move> killSomeoneMoves = getMovesToKillSomeoneBySphinx(board, myColor, availableMoves);
        if (killSomeoneMoves.size() > 0) {
            return log(chooseRandomMove(killSomeoneMoves), myColor).toString();
        }
        /* If it is impossible, we try to save my all figure*/
        List<Move> saveMyFigures = getSafeMovesList(board, myColor, availableMoves);
        if (saveMyFigures.size() > 0) {
            return log(chooseRandomMove(saveMyFigures), myColor).toString();
        }
        /*If we can't do that, we try to save my pharaoh*/
        List<Move> saveMyPharaoh = getSaveMyPharaohMovesList(board, myColor, availableMoves);
        if (saveMyPharaoh.size() > 0) {
            return log(chooseRandomMove(saveMyPharaoh), myColor).toString();
        }
        /*Otherwise, we chooseRandomMove*/
        return log(chooseRandomMove(availableMoves), myColor).toString();
    }

    /**
     * This method return moves, who allow my sphinx to kill enemy pharaoh.
     */
    private List<Move> getMovesToKillPharaohBySphinx(Board board, Color myColor,
                                                     List<Move> availableMoves) {
        final List<Move> result = new ArrayList<>();

        for (Move move : availableMoves) {

            Board newBoard = board.clone();
            newBoard.makeMove(move, myColor);

            Figure figureToKill = newBoard.getFigureToBeKilledBySphinx(myColor);
            if (isEnemySphinx(myColor, figureToKill)) {
                result.add(move);
            }
        }
        return result;
    }

    /**
     * Return moves, which allow be to kill enemy figure by my sphinx.
     */
    private List<Move> getMovesToKillSomeoneBySphinx(Board board, Color myColor,
                                                     List<Move> availableMoves) {
        List<Move> result = new ArrayList<>();

        for (Move move : availableMoves) {
            Board newBoard = board.clone();
            newBoard.makeMove(move, myColor);

            Figure figureToKill = newBoard.getFigureToBeKilledBySphinx(myColor);
            if (isEnemy(figureToKill, myColor)) {
                result.add(move);
            }
        }
        return result;
    }

    /**
     * build moves, witch allow me to save all my figure.
     */
    private List<Move> getSafeMovesList(Board board, Color color,
                                        List<Move> availableMoves) {
        List<Move> result = new ArrayList<>();

        for (Move move : availableMoves) {
            Board newBoard = board.clone();
            newBoard.makeMove(move, color);

            Figure figureToKill = newBoard.getFigureToBeKilledBySphinx(color);
            if (figureToKill == null) {
                result.add(move);
            }

        }


        return result;
    }

    /**
     * build moves, witch allow me to save  my pharaoh.
     * If i can't save all my figure, i try to save pharaoh.
     */
    private List<Move> getSaveMyPharaohMovesList(Board board, Color color,
                                                 List<Move> availableMoves) {
        List<Move> result = new ArrayList<>();

        for (Move move : availableMoves) {
            Board newBoard = board.clone();
            newBoard.makeMove(move, color);

            Figure figureToKill = newBoard.getFigureToBeKilledBySphinx(color);
            if (!(figureToKill instanceof Pharaoh)) {
                result.add(move);
            }

        }

        return result;
    }
}


