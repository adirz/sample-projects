package com.epam.sdk;

import com.epam.sdk.move.Move;
import com.epam.sdk.BoardCoordinate;
import java.util.List;

/**
* an easy way to bond all the data we need in the alpha-beta search
*/
public class State {
    private List<Move> myAvailableMoves;
    private List<Move> enemyAvailableMoves;
    private short myPharaohDefenses, enemyPharaohDefenses;
    private short[] allies, enemies;

    public State(List<Move> myAvailableMoves, List<Move> enemyAvailableMoves,
                 short myPharaohDefenses, short enemyPharaohDefenses, short[] enemies, short[] allies) {
        this.myAvailableMoves = myAvailableMoves;
        this.enemyAvailableMoves = enemyAvailableMoves;
        this.myPharaohDefenses = myPharaohDefenses;
        this.enemyPharaohDefenses = enemyPharaohDefenses;
        this.enemies = enemies;
        this.allies = allies;
    }

    public List<Move> getMyAvailableMoves() {
        return myAvailableMoves;
    }

    public List<Move> getEnemyAvailableMoves() {
        return enemyAvailableMoves;
    }

    public short getMyPharaohDefenses() {
        return myPharaohDefenses;
    }

    public short getEnemyPharaohDefenses() {
        return enemyPharaohDefenses;
    }

    public short[] getAllies() {
        return allies;
    }

    public short[] getEnemies() {
        return enemies;
    }
}
