package ru.rzn.gmyasoedov.tetris.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static ru.rzn.gmyasoedov.tetris.core.Figures.FIGURE_Y;
import static ru.rzn.gmyasoedov.tetris.core.TestUtils.printMatrix;

class TetrisYCornerTest {

    private static final List<int[][]> POSITIONS = FIGURE_Y.getPositions();

    @Test
    void toLeft() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 0), 0, 6);
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
    void toLeft1() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 1), 1, 6);
        printMatrix(tetris.getFieldState());
        tetris.toLeftInner();
        printMatrix(tetris.getFieldState());
        tetris.toLeftInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
    }

    @Test
    void toRight() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 0), 6, 6);
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
    }

    @Test
    void toRight1() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 2), 6, 6);
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