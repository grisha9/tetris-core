package ru.rzn.gmyasoedov.tetris.core;

public class SimpleFigureGenerator implements FigureGenerator {

    @Override
    public String register() {
        return null;
    }

    @Override
    public Figure getNext(String sessionId) {
        return generateFigure();
    }
}
