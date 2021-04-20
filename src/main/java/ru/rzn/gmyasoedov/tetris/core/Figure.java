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

    public int[][] getState() {
        return positions.get(position);
    }

    public int[][] getNextRotate() {
        return positions.get(getIndex(position + 1, positions));
    }

    public int[][] getNextRotateReverse() {
        return positions.get(getPrevPosition());
    }

    public int[][] rotate() {
        position = getIndex(position + 1, positions);
        return getState();
    }

    public int[][] rotateReverse() {
        position = getPrevPosition();
        return getState();
    }

    private int getPrevPosition() {
        return position == 0 ? positions.size() - 1 : position - 1;
    }

    private int getIndex(int i, List<int[][]> positions) {
        return i % positions.size();
    }
}
