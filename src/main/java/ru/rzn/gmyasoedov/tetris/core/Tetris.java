package ru.rzn.gmyasoedov.tetris.core;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.String.format;


public class Tetris {
    private static final int FIELD_HEIGHT = 20;
    private static final int FIELD_WIDTH = 10;
    private static final int CELL_EMPTY = 0;


    private static final int BOOST = 8;
    private static final int START_Y_LEFT_TOP = -2;
    private static final int START_X_LEFT_TOP = 3;
    private static final int STEP = 1;
    private static final int START_SCORE = 0;
    private static final int SLEEP_TIME = 500;
    private final SimpleFigureGenerator figureGenerator;
    private Figure figure;
    private Figure nextFigure;
    private final int[][] field;
    private int xLeftTop;
    private int yLeftTop;
    private int score;
    private int speed;
    private volatile State state = State.NEW;
    private Lock lock = new ReentrantLock();
    private Thread gameThread;
    private List<Consumer<TetrisState>> observable;

    public Tetris() {
        speed = SLEEP_TIME;
        field = new int[FIELD_HEIGHT][FIELD_WIDTH];
        figureGenerator = new SimpleFigureGenerator();
        nextFigure = figureGenerator.getNext();
        observable = new CopyOnWriteArrayList<>();
    }

    Tetris(int[][] field, Figure figure, int x, int y) {
        speed = SLEEP_TIME;
        this.field = field;
        this.figure = figure;
        this.xLeftTop = x;
        this.yLeftTop = y;
        figureGenerator = new SimpleFigureGenerator();
        nextFigure = figureGenerator.getNext();
        observable = new CopyOnWriteArrayList<>();
    }

    public void start() {
        lock.lock();
        if (state != State.NEW) {
            throw new IllegalStateException(format("state should be %s but state %s", State.NEW, state));
        }
        score = START_SCORE;
        state = State.GAME;
        generateFigure();
        initField();
        gameThread = new Thread(this::game);
        gameThread.start();
        lock.unlock();
    }

    private void initField() {
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            Arrays.fill(field[i], CELL_EMPTY);
        }
    }

    private void generateFigure() {
        xLeftTop = START_X_LEFT_TOP;
        yLeftTop = START_Y_LEFT_TOP;

        figure = nextFigure;
        nextFigure = figureGenerator.getNext();
    }

    private void game() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(speed);
                lock.lock();
                if (state == State.GAME) {
                    downInner();
                }
            } catch (InterruptedException e) {
                return;
            } finally {
                lock.unlock();
            }
        }
    }

    void downInner() {
        if (contact()) {
            figureToField(field);
            doScore();
            checkGameOver();
            generateFigure();
            normalSpeed();
        } else {
            yLeftTop++;
        }
        notifyObserves();
    }

    public boolean isPause() {
        return state == State.PAUSE;
    }

    public void pause() {
        lock.lock();
        try {
            if (state == State.GAME) {
                state = State.PAUSE;
                notifyObserves();
            } else {
                throw new IllegalStateException(format("state should be %s but state %s", State.GAME, state));
            }
        } finally {
            lock.unlock();
        }

    }

    public void resume() {
        lock.lock();
        try {
            if (state == State.PAUSE) {
                state = State.GAME;
                notifyObserves();
            } else {
                throw new IllegalStateException(format("state should be %s but state %s", State.GAME, state));
            }
        } finally {
            lock.unlock();
        }
    }

    public void fastSpeed() {
        lock.lock();
        try {
            speed = SLEEP_TIME / BOOST;
        } finally {
            lock.unlock();
        }
    }

    public void normalSpeed() {
        lock.lock();
        try {
            speed = SLEEP_TIME;
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            gameThread.interrupt();
            gameThread = null;
            notifyObserves();
            observable = null;
            state = State.OVER;
        } finally {
            lock.unlock();
        }
    }

    public void addObserver(Consumer<TetrisState> observer) {
        observable.add(observer);
    }

    public boolean isRun() {
        return gameThread != null && gameThread.isInterrupted();
    }

    public void toLeft() {
        try {
            lock.lock();
            if (state != State.GAME) {
                return;
            }
            toLeftInner();
        } finally {
            lock.unlock();
        }
    }

    public void toRight() {
        lock.lock();
        try {
            if (state != State.GAME) {
                return;
            }
            toRightInner();
        } finally {
            lock.unlock();
        }
    }

    public void rotate() {
        lock.lock();
        if (state != State.GAME) {
            return;
        }
        try {
            rotateInner(Figure::getNextRotate, Figure::rotate);
        } finally {
            lock.unlock();
        }
    }

    public void rotateReverse() {
        lock.lock();
        if (state != State.GAME) {
            return;
        }
        try {
            rotateInner(Figure::getNextRotateReverse, Figure::rotateReverse);
        } finally {
            lock.unlock();
        }
    }

    void toLeftInner() {
        boolean doLeft = true;
        int[][] figureState = figure.getState();
        for (int i = 0; i < figureState.length; i++) {
            if ((yLeftTop + i) < 0) {
                continue;
            }
            int[] figureRow = figureState[i];
            for (int j = 0; j < figureRow.length; j++) {
                if (figureState[i][j] > CELL_EMPTY) {
                    if (((xLeftTop + j - STEP) < 0) || field[yLeftTop + i][xLeftTop + j - STEP] > CELL_EMPTY) {
                        doLeft = false;
                        break;
                    }
                    break;
                }
            }
            if (!doLeft) {
                break;
            }
        }
        if (doLeft) {
            xLeftTop--;
            notifyObserves();
        }
    }

    void toRightInner() {
        boolean doRight = true;
        int[][] figureState = figure.getState();
        for (int i = 0; i < figureState.length; i++) {
            if ((yLeftTop + i) < 0) {
                continue;
            }
            int[] figureRow = figureState[i];
            for (int j = figureRow.length - 1; j >= 0; j--) {
                if (figureState[i][j] > CELL_EMPTY) {
                    if ((xLeftTop + j + STEP) >= FIELD_WIDTH
                            || field[yLeftTop + i][xLeftTop + j + STEP] > CELL_EMPTY) {
                        doRight = false;
                        break;
                    }
                    break;
                }
            }
            if (!doRight) {
                break;
            }
        }

        if (doRight) {
            xLeftTop++;
            notifyObserves();
        }
    }

    void rotateInner(Function<Figure, int[][]> nextRotateFunc, Consumer<Figure> rotateFunc) {
        int[][] nextState = nextRotateFunc.apply(figure);
        boolean rotate = true;

        for (int i = 0; i < nextState.length; i++) {
            if ((yLeftTop + i) < 0) {
                continue;
            }
            int[] figureRow = nextState[i];
            for (int j = 0; j < figureRow.length; j++) {
                if (nextState[i][j] > CELL_EMPTY) {
                    if ((yLeftTop + i) >= FIELD_HEIGHT
                            || (xLeftTop + j) < 0
                            || (xLeftTop + j) >= FIELD_WIDTH
                            || field[yLeftTop + i][xLeftTop + j] > CELL_EMPTY) {
                        rotate = false;
                        break;
                    }
                }
            }
            if (!rotate) {
                break;
            }
        }
        if (rotate) {
            rotateFunc.accept(figure);
            notifyObserves();
        }
    }

    private void checkGameOver() {
        if (yLeftTop < 0) {
            stop();
        }
    }

    private boolean contact() {
        int[][] state = figure.getState();
        for (int i = 0; i < state.length; i++) {
            if ((yLeftTop + i) < 0) {
                continue;
            }
            int[] figureRow = state[i];
            for (int j = 0; j < figureRow.length; j++) {
                if (state[i][j] > CELL_EMPTY) {
                    if ((yLeftTop + i + STEP) >= FIELD_HEIGHT
                            || field[yLeftTop + i + STEP][xLeftTop + j] > CELL_EMPTY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void doScore() {
        int lineCount = 0;
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            boolean isFullLine = true;
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (field[i][j] == CELL_EMPTY) {
                    isFullLine = false;
                    break;
                }
            }
            if (isFullLine) {
                lineCount++;
                for (int c = i - 1; c >= 0; c--) {
                    for (int j = 0; j < FIELD_WIDTH; j++) {
                        field[c + 1][j] = field[c][j];
                        field[c][j] = CELL_EMPTY;
                    }
                }
            }
        }
        switch (lineCount) {
            case 1:
                score = score + 1;
                break;
            case 2:
                score = score + 3;
                break;
            case 3:
                score = score + 7;
                break;
            case 4:
                score = score + 15;
                break;
        }
    }

    private void figureToField(int[][] filedMatrix) {
        int[][] state = figure.getState();
        for (int i = 0; i < state.length; i++) {
            int[] figureRow = state[i];
            for (int j = 0; j < figureRow.length; j++) {
                if (state[i][j] > CELL_EMPTY && (yLeftTop + i) >= 0) {
                    filedMatrix[yLeftTop + i][xLeftTop + j] = state[i][j];
                }
            }
        }
    }

    int[][] getFieldState() {
        int[][] cloneFiled = new int[field.length][];
        for (int i = 0; i < field.length; i++) {
            cloneFiled[i] = field[i].clone();
        }
        figureToField(cloneFiled);
        return cloneFiled;
    }

    private TetrisState getState() {
        return new TetrisState(getFieldState(), nextFigure.getState(), score, state.name());
    }

    private void notifyObserves() {
        if (observable == null || observable.isEmpty()) {
            return;
        }
        TetrisState state = getState();
        observable.forEach(o -> o.accept(state));
    }

    private enum State {
        NEW, GAME, PAUSE, OVER
    }
}
