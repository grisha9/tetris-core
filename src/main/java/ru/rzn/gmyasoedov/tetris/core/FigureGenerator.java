package ru.rzn.gmyasoedov.tetris.core;

import java.util.Random;

public class FigureGenerator {
    int FIGURE_COUNT = Figures.values().length;

    private final Random random;

    public FigureGenerator() {
        random = new Random();
    }

    public FigureGenerator(long seed) {
        random = new Random(seed);
    }

    public Figure getNext() {
        Figures figures = Figures.values()[random.nextInt(FIGURE_COUNT)];
        int position = random.nextInt(4);
        return new Figure(figures.getPositions(), position);
    }
}
