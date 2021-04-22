package ru.rzn.gmyasoedov.tetris.core;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import static ru.rzn.gmyasoedov.tetris.core.Figures.FIGURE_Z;
import static ru.rzn.gmyasoedov.tetris.core.TestUtils.printMatrix;

class TetrisTest {


    public static void main(String[] args) throws IOException {
        int[][] matrix = TestUtils.readMatrix(Paths.get("src/test/resources/empty.txt"));
        Tetris tetris = new Tetris(matrix, new Figure(FIGURE_Z.getPositions(), 0), 0, 0);

        char c = (char) System.in.read();
        System.out.println(c);

        Scanner scanner = new Scanner(System.in);
        String next = "";
        while (!next.equalsIgnoreCase("q")) {
            next = scanner.next();
            switch (next) {
                case "a":
                    tetris.toLeftInner();
                    break;
                case "d":
                    tetris.toRightInner();
                    break;
                case "s":
                    tetris.downInner();
                    break;
                case "w":
                    tetris.rotateInner(Figure::getNextRotate, Figure::rotate);
                    break;
                case "e":
                    tetris.rotateInner(Figure::getNextRotateReverse, Figure::rotateReverse);
                    break;
            }
            TestUtils.clearConsole();
            printMatrix(tetris.getFieldState());
        }
    }
}