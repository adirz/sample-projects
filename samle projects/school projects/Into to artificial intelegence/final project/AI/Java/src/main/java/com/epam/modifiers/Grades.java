package com.epam.modifiers;

import java.util.Random;

/**
 * possible move variable and pharaoh defense variable are each a tuple where the first is by how much we multiply the value and the second is to the power of what we rais it
 * score = x[0] * value ^ x[1]
 * enemy num and allie num each represnt a type of pawn that can be killed
 * score = enemynum[i]*value^poerenemy[i]
 */
public class Grades {
    // all scores are arrays- of two first one is the doubling factor the second one is the power factor
    public static final double MUTATION_PROBABILITY = 0.5;
    public static final int MULTIPLICITY = 0;
    public static final int POWER = 1;

    // Grades for state.
    public double[] myPossibleMoves;
    public double[] opponentPossibleMoves;

    // grades for defending the pharaoh
    public double[] myPharaohDefense;

    //grades for not letting the enemy defend the pharaoh
    public double[] enemyPharaohDefense;

    /**
     * 0 - Pyramid
     * 1 - Anubis
     * 2 - Pharaoh
     */
    public double[] enemyNum;
    public double[] alliesNum;
    public double[] powerEnemy;
    public double[] powerAllies;
    
    public static Grades getPlannedGrades() {
    	Grades g = new Grades();
    	g.myPossibleMoves = new double[] {1,1};
    	g.opponentPossibleMoves = new double[] {1,1};
		g.myPharaohDefense = new double[] {1,1};
		g.enemyPharaohDefense = new double[] {1,1};
		g.alliesNum = new double[] {3,2,1};
		g.powerAllies = new double[] {1,1,1};
		g.enemyNum = new double[] {4,3,1};
		g.powerEnemy = new double[] {3,3,3};
		return g;
    }


    /**
     *
     */
    public Grades() {
        Random r = new Random();
        myPossibleMoves = new double[] {r.nextDouble() * 100, r.nextDouble() * 2};
        opponentPossibleMoves = new double[] {r.nextDouble() * 100, r.nextDouble() * 2};
        myPharaohDefense = new double[] {r.nextDouble() * 100, r.nextDouble() * 2};
        enemyPharaohDefense = new double[] {r.nextDouble() * 100, r.nextDouble() * 2};
        alliesNum = new double[] {r.nextDouble() * 100, r.nextDouble() * 100, 1};
        powerAllies = new double[] {r.nextDouble() * 2, r.nextDouble() * 2, 1};
        enemyNum = new double[] {r.nextDouble() * 100, r.nextDouble() * 100, 1};
        powerEnemy = new double[] {r.nextDouble() * 2, r.nextDouble() * 2, 1};
    }

    /**
     *
     * @param grades
     */
    public Grades(Grades grades) {
        myPossibleMoves = grades.myPossibleMoves.clone();
        opponentPossibleMoves = grades.opponentPossibleMoves.clone();
        myPharaohDefense = grades.myPharaohDefense.clone();
        enemyPharaohDefense = grades.enemyPharaohDefense.clone();
        alliesNum = grades.alliesNum.clone();
        powerAllies = grades.powerAllies.clone();
        enemyNum = grades.enemyNum.clone();
        powerEnemy = grades.powerEnemy.clone();
    }

    /**
     *
     * @param A
     * @param B
     * @param chance
     * @return
     */
    private Grades chooseRandom(Grades A, Grades B, double chance) {
        Random r = new Random();
        if (r.nextDouble() < chance) {
            return A;
        }
        return B;
    }

    /**
     *
     * @param chance
     * @return
     */
    private boolean prob(double chance) {
        return ((new Random()).nextDouble() < chance);
    }

    private static double getRelativeChange(double mutationFactor) {
        Random r = new Random();
        return 1 + mutationFactor * (r.nextDouble() * 2 - 1);
    }

    /**
     *
     * @param mutationFactor
     */
    public void mutate(double mutationFactor) {
        myPossibleMoves[MULTIPLICITY] *= getRelativeChange(mutationFactor);
        myPossibleMoves[POWER] *= getRelativeChange(mutationFactor);
        opponentPossibleMoves[MULTIPLICITY] *= getRelativeChange(mutationFactor);
        opponentPossibleMoves[POWER] *= getRelativeChange(mutationFactor);
        myPharaohDefense[MULTIPLICITY] *= getRelativeChange(mutationFactor);
        myPharaohDefense[POWER] *= getRelativeChange(mutationFactor);
        enemyPharaohDefense[MULTIPLICITY] *= getRelativeChange(mutationFactor);
        enemyPharaohDefense[POWER] *= getRelativeChange(mutationFactor);
        for (int i = 0; i < alliesNum.length; i++) {
            alliesNum[i] *= getRelativeChange(mutationFactor);
            powerAllies[i] *= getRelativeChange(mutationFactor);
            enemyNum[i] *= getRelativeChange(mutationFactor);
            powerEnemy[i] *= getRelativeChange(mutationFactor);
        }
    }

    /**
     *
     * @param grade1
     * @param score1
     * @param grade2
     * @param score2
     * @param mutationFactor
     */
    public Grades(Grades grade1, double score1, Grades grade2, double score2, double mutationFactor) {
        double chance = score1 / (score1 + score2);
        myPossibleMoves = chooseRandom(grade1, grade2, chance).myPossibleMoves;
        opponentPossibleMoves = chooseRandom(grade1, grade2, chance).opponentPossibleMoves;
        myPharaohDefense = chooseRandom(grade1, grade2, chance).myPharaohDefense;
        enemyPharaohDefense = chooseRandom(grade1, grade2, chance).enemyPharaohDefense;

        enemyNum = new double[3];
        alliesNum = new double[3];
        powerEnemy = new double[3];
        powerAllies = new double[3];
        for (int i = 0; i < 3; i++) {
            alliesNum[i] = chooseRandom(grade1, grade2, chance).alliesNum[i];
            powerAllies[i] = chooseRandom(grade1, grade2, chance).powerAllies[i];
            enemyNum[i] = chooseRandom(grade1, grade2, chance).enemyNum[i];
            powerEnemy[i] = chooseRandom(grade1, grade2, chance).powerEnemy[i];
        }
        if (prob(MUTATION_PROBABILITY)) {
            mutate(mutationFactor);
        }
    }
}
