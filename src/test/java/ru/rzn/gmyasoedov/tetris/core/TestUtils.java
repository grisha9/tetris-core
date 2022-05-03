package ru.rzn.gmyasoedov.tetris.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

abstract class TestUtils {

    public static void printMatrix(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.printf("%2d", anInt);
            }
            System.out.println();
        }
        System.out.println("=====================");
    }

    public static int[][] readMatrix(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath.toAbsolutePath());
        while (lines.size() < 24) {
            lines.add(0, "0 0 0 0 0 0 0 0 0 0");
        }
        int[][] ints = new int[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            ints[i] = Stream.of(lines.get(i).split(" ")).mapToInt(Integer::valueOf).toArray();

        }
        return ints;
    }

    public static void clearConsole() throws IOException {

        final String os = System.getProperty("os.name");

        if (os.toLowerCase().contains("windows")) {
            Runtime.getRuntime().exec("cls");
        } else {
            Runtime.getRuntime().exec("clear");
        }

    }
}
