package ru.rzn.gmyasoedov.tetris.core;

public class TetrisSettings {
    public static final int SCORE_LEVEL_DELTA = 50;
    public static final int SPEED_DELTA = 35;

    private int scoreLevelDelta = SCORE_LEVEL_DELTA;
    private int speedDelta = SPEED_DELTA;

    public TetrisSettings() {}

    public int getScoreLevelDelta() {
        return scoreLevelDelta;
    }

    public TetrisSettings setScoreLevelDelta(int scoreLevelDelta) {
        if (speedDelta < 10) return null;
        this.scoreLevelDelta = scoreLevelDelta;
        return this;
    }

    public int getSpeedDelta() {
        return speedDelta;
    }

    public TetrisSettings setSpeedDelta(int speedDelta) {
        if (speedDelta > 200) return null;
        this.speedDelta = speedDelta;
        return this;
    }
}
