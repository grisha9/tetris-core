package ru.rzn.gmyasoedov.tetris.core;

public class TetrisState {
    private final int[][] field;
    private final int[][] nextFigure;
    private final int score;
    private final int level;
    private final Tetris.State state;

    public TetrisState(int[][] field, int[][] nextFigure, int score, int level, Tetris.State state) {
        this.field = field;
        this.nextFigure = nextFigure;
        this.score = score;
        this.level = level;
        this.state = state;
    }

    public int[][] getField() {
        return field;
    }

    public int[][] getNextFigure() {
        return nextFigure;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public Tetris.State getState() {
        return state;
    }
}
