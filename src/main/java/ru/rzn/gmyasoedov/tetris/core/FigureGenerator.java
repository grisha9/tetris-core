package ru.rzn.gmyasoedov.tetris.core;

import java.util.concurrent.ThreadLocalRandom;

public interface FigureGenerator {

    int FIGURE_COUNT = Figures.values().length;

    String register();

    Figure getNext(String sessionId);

    default void gameOver(String sessionId) {};

    default Figure generateFigure() {
        Figures figures = Figures.values()[ThreadLocalRandom.current().nextInt(FIGURE_COUNT)];
        int position = ThreadLocalRandom.current().nextInt(4);
        return new Figure(figures.getPositions(), position);
    }
}
