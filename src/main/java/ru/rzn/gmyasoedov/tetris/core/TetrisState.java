package ru.rzn.gmyasoedov.tetris.core;

public class TetrisState {
    private final int[][] field;
    private final int[][] nextFigure;
    private final int score;
    private final String state;

    public TetrisState(int[][] field, int[][] nextFigure, int score, String state) {
        this.field = field;
        this.nextFigure = nextFigure;
        this.score = score;
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

    public String getState() {
        return state;
    }
}
