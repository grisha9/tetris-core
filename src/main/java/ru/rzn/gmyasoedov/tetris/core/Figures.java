package ru.rzn.gmyasoedov.tetris.core;

import java.util.Collections;
import java.util.List;

enum Figures {

    FIGURE_LONG(List.of(FigureLong.POS1, FigureLong.POS2)),
    FIGURE_Z(List.of(FigureZ.POS1, FigureZ.POS2)),
    FIGURE_Z_R(List.of(FigureZR.POS1, FigureZR.POS2)),
    FIGURE_KUB(Collections.singletonList(FigureKub.POS1)),
    FIGURE_Y(List.of(FigureY.POS1, FigureY.POS2, FigureY.POS3, FigureY.POS4)),
    FIGURE_G(List.of(FigureG.POS1, FigureG.POS2, FigureG.POS3, FigureG.POS4)),
    FIGURE_G_R(List.of(FigureGR.POS1, FigureGR.POS2, FigureGR.POS3, FigureGR.POS4));

    private final List<int[][]> positions;

    Figures(List<int[][]> positions) {
        this.positions = positions;
    }

    public List<int[][]> getPositions() {
        return positions;
    }

    private static class FigureLong {
        static final int[][] POS1 = new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        static final int[][] POS2 = new int[][]{
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 1, 0}
        };
    }

    private static class FigureZ {
        static final int[][] POS1 = new int[][]{
                {0, 0, 0},
                {0, 1, 1},
                {1, 1, 0}
        };
        static final int[][] POS2 = new int[][]{
                {0, 1, 0},
                {0, 1, 1},
                {0, 0, 1}
        };
    }

    private static class FigureZR {
        static final int[][] POS1 = new int[][]{
                {0, 0, 0},
                {1, 1, 0},
                {0, 1, 1}
        };
        static final int[][] POS2 = new int[][]{
                {0, 0, 1},
                {0, 1, 1},
                {0, 1, 0}
        };
    }

    private static class FigureKub {
        static final int[][] POS1 = new int[][]{
                {1, 1},
                {1, 1}
        };
    }

    private static class FigureY {
        static final int[][] POS1 = new int[][]{
                {0, 1, 0},
                {0, 1, 1},
                {0, 1, 0}
        };
        static final int[][] POS2 = new int[][]{
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        static final int[][] POS3 = new int[][]{
                {0, 1, 0},
                {1, 1, 0},
                {0, 1, 0}
        };

        static final int[][] POS4 = new int[][]{
                {0, 0, 0},
                {1, 1, 1},
                {0, 1, 0}
        };
    }

    private static class FigureG {
        static final int[][] POS1 = new int[][]{
                {0, 0, 0},
                {1, 1, 1},
                {0, 0, 1}
        };
        static final int[][] POS2 = new int[][]{
                {0, 1, 1},
                {0, 1, 0},
                {0, 1, 0}
        };

        static final int[][] POS3 = new int[][]{
                {1, 0, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        static final int[][] POS4 = new int[][]{
                {0, 1, 0},
                {0, 1, 0},
                {1, 1, 0}
        };
    }

    private static class FigureGR {
        static final int[][] POS1 = new int[][]{
                {0, 0, 1},
                {1, 1, 1},
                {0, 0, 0}
        };
        static final int[][] POS2 = new int[][]{
                {1, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
        };

        static final int[][] POS3 = new int[][]{
                {0, 0, 0},
                {1, 1, 1},
                {1, 0, 0}
        };

        static final int[][] POS4 = new int[][]{
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 1}
        };
    }
}
