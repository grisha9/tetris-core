package ru.rzn.gmyasoedov.tetris.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.rzn.gmyasoedov.tetris.core.MultiPlayerFigureGenerator.INIT_FIGURE_COUNT;
import static ru.rzn.gmyasoedov.tetris.core.MultiPlayerFigureGenerator.LIMIT_TO_RESIZE;

class MultiPlayerFigureGeneratorTest {

    private MultiPlayerFigureGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new MultiPlayerFigureGenerator();
    }

    @Test
    void getFigure() {
        Assertions.assertThrows(IllegalStateException.class, () -> generator.getNext("no session"));
    }

    @Test
    void getFigure1() {
        String sessionId = generator.register();
        String sessionId2 = generator.register();
        int endExclusive = INIT_FIGURE_COUNT - LIMIT_TO_RESIZE;
        IntStream.range(0, endExclusive).forEach(i -> generator.getNext(sessionId));
        assertEquals(LIMIT_TO_RESIZE, generator.getSessionFigures(sessionId).size());
        generator.getNext(sessionId);
        assertEquals(LIMIT_TO_RESIZE + INIT_FIGURE_COUNT - 1, generator.getSessionFigures(sessionId).size());
        assertEquals(INIT_FIGURE_COUNT * 2, generator.getSessionFigures(sessionId2).size());
    }

    @Test
    void getFigure3() {
        String sessionId = generator.register();
        String sessionId2 = generator.register();
        generator.getNext(sessionId);
        generator.getNext(sessionId2);
        Assertions.assertThrows(IllegalStateException.class, () -> generator.register());
    }
}