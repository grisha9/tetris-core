package ru.rzn.gmyasoedov.tetris.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static ru.rzn.gmyasoedov.tetris.core.Figures.FIGURE_Z;
import static ru.rzn.gmyasoedov.tetris.core.TestUtils.printMatrix;

class TetrisTest {

    @Test
    void toLeft() throws IOException {
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(FIGURE_Z.getPositions(), 1), 1, 2);
        printMatrix(tetris.getFieldState());
        tetris.toLeftInner();
        printMatrix(tetris.getFieldState());
        tetris.toLeftInner();
        printMatrix(tetris.getFieldState());
        tetris.toLeftInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
    }

    @Test
    void toRight() throws IOException {
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(FIGURE_Z.getPositions(), 0), 6, 0);
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
    }
}