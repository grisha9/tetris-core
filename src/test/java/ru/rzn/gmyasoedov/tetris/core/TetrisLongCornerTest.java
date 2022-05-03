package ru.rzn.gmyasoedov.tetris.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static ru.rzn.gmyasoedov.tetris.core.Figures.FIGURE_LONG;
import static ru.rzn.gmyasoedov.tetris.core.TestUtils.printMatrix;

class TetrisLongCornerTest {

    private static final List<int[][]> POSITIONS = FIGURE_LONG.getPositions();

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
    void toLeftField1() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/field1.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 0), 2, 6);
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
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 1), 0, 6);
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
    void toLeft1Field1() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/field1.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 1), 0, 6);
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
    }

    @Test
    void toRightField1() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/field1.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 0), 4, 6);
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

    @Test
    void toRight1() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 1), 6, 6);
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

    @Test
    void toRight1Field1() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/field1.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 1), 4, 6);
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());

        tetris.toLeftInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
    }

    @Test
    void toRight1Field2() throws IOException {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/field2.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(POSITIONS, 1), 4, 6);
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.toRightInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());

        tetris.toLeftInner();
        printMatrix(tetris.getFieldState());
        tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
        printMatrix(tetris.getFieldState());
    }
}