package ru.rzn.gmyasoedov.tetris.core;

import java.util.concurrent.ThreadLocalRandom;

public class SimpleFigureGenerator {

    private static final int FIGURE_COUNT = Figures.values().length;

    public Figure getNext() {
        Figures figures = Figures.values()[ThreadLocalRandom.current().nextInt(FIGURE_COUNT)];
        int position = ThreadLocalRandom.current().nextInt(4);
        return new Figure(figures.getPositions(), position);
    }
}
