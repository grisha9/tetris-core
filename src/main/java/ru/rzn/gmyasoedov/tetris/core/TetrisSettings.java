package ru.rzn.gmyasoedov.tetris.core;

public class TetrisSettings {
    public static final int SCORE_LEVEL_DELTA = 50;
    public static final int SPEED_DELTA = 35;

    private int scoreLevelDelta = SCORE_LEVEL_DELTA;
    private int speedDelta = SPEED_DELTA;
    private boolean commonMaxSpeed = false;
    private boolean badRowAfterTetris = false;

    public TetrisSettings() {}

    public int getScoreLevelDelta() {
        return scoreLevelDelta;
    }

    public TetrisSettings setScoreLevelDelta(int scoreLevelDelta) {
        if (speedDelta < 10) return this;
        this.scoreLevelDelta = scoreLevelDelta;
        return this;
    }

    public int getSpeedDelta() {
        return speedDelta;
    }

    public TetrisSettings setSpeedDelta(int speedDelta) {
        if (speedDelta > 200) return this;
        this.speedDelta = speedDelta;
        return this;
    }

    public boolean isCommonMaxSpeed() {
        return commonMaxSpeed;
    }

    public void setCommonMaxSpeed(boolean commonMaxSpeed) {
        this.commonMaxSpeed = commonMaxSpeed;
    }

    public boolean isBadRowAfterTetris() {
        return badRowAfterTetris;
    }

    public void setBadRowAfterTetris(boolean badRowAfterTetris) {
        this.badRowAfterTetris = badRowAfterTetris;
    }
}
