package ru.rzn.gmyasoedov.tetris.core;

import java.util.List;
import java.util.Objects;

class Figure {
    private final List<int [][]> positions;
    private int position;

    Figure(List<int[][]> positions, int position) {
        Objects.checkIndex(position, Integer.MAX_VALUE);
        this.positions = positions;
        this.position = getIndex(position, positions);
    }

    protected Figure clone() {
        return new Figure(positions, position);
    }

    int[][] getState() {
        return positions.get(position);
    }

    int[][] getNextRotate() {
        return positions.get(getIndex(position + 1, positions));
    }

    int[][] getNextRotateReverse() {
        return positions.get(getPrevPosition());
    }

    void rotate() {
        position = getIndex(position + 1, positions);
    }

    void rotateReverse() {
        position = getPrevPosition();
    }

    private int getPrevPosition() {
        return position == 0 ? positions.size() - 1 : position - 1;
    }

    private int getIndex(int i, List<int[][]> positions) {
        return i % positions.size();
    }
}
