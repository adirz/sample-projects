package com.epam.sdk;

import com.epam.sdk.figures.Figure;
import com.epam.sdk.figures.Pharaoh;
import com.epam.sdk.move.Move;
import com.epam.modifiers.Grades;

import java.lang.Math;

import java.util.List;
import java.util.Random;


/**
 * the base for all the agents
 */
public abstract class IBot {
    //
    protected Grades grades;

    //
    abstract public String nextMove(final Board b, final Color c);

    /* This function choose random moves from list */
    protected Move chooseRandomMove(List<Move> moves) {
        Random random = new Random();
        int index = random.nextInt(moves.size());
        if (moves.size() == 0) {
            throw new RuntimeException("Impossible to choose random move from empty list");
        }
        return moves.get(index);
    }

    protected boolean isEnemySphinx(Color color, Figure figureToKill) {
        return figureToKill instanceof Pharaoh && figureToKill.isEnemyColor(color);
    }

    protected boolean isMySphinx(Color color, Figure figureToKill) {
        return figureToKill instanceof Pharaoh && figureToKill.isMyColor(color);
    }

    protected boolean isEnemy(Figure figureToKill, Color color) {
        return figureToKill != null && figureToKill.isEnemyColor(color);
    }

    protected boolean isMe(Figure figureToKill, Color color) {
        return figureToKill != null && figureToKill.isMyColor(color);
    }

    /**
     *
     * @return
     */
    protected double stateGrader(State state, boolean maxPlayer) {
        double score = 0;
        short[] allies = state.getAllies(), enemies = state.getEnemies();
        List<Move> myAvailableMoves = state.getMyAvailableMoves(), enemyAvailableMoves = state.getEnemyAvailableMoves();

        // grade according to possible moves of enemy and me
        score += grades.myPossibleMoves[grades.MULTIPLICITY] * Math.pow(myAvailableMoves.size(),
                grades.myPossibleMoves[grades.POWER]);
        score -= grades.opponentPossibleMoves[grades.MULTIPLICITY] * Math.pow(enemyAvailableMoves.size(),
                grades.opponentPossibleMoves[grades.POWER]);

        // grade according to Pharaoh's defences
        score += grades.myPharaohDefense[grades.MULTIPLICITY] * Math.pow(state.getMyPharaohDefenses(),
                grades.myPharaohDefense[grades.POWER]);
        score -= grades.enemyPharaohDefense[grades.MULTIPLICITY] * Math.pow(state.getEnemyPharaohDefenses(),
                grades.enemyPharaohDefense[grades.POWER]);

        // count figures left
        for (int i = 0; i  < enemies.length; i++){
            score -= grades.enemyNum[i] * Math.pow(enemies[i], grades.powerEnemy[i]);
            score += grades.alliesNum[i] * Math.pow(allies[i], grades.powerAllies[i]);
        }

        // Red is the minimizing player
        return maxPlayer ? score : -score;
    }

    public Grades getGrades() {
    	return grades;
    }
    
    /**
     *
     * @param maxPlayer
     * @return
     */
    protected Double bestScore(boolean maxPlayer){
        return maxPlayer? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }
}
