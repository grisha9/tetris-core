package ru.rzn.gmyasoedov.tetris.core;

import org.jetbrains.annotations.TestOnly;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.String.format;


public class Tetris {
    private static final int FIELD_HEIGHT = 24;
    private static final int FIELD_WIDTH = 10;
    private static final int CELL_EMPTY = 0;


    private static final int BOOST_SPEED = 50;
    private static final int START_X_LEFT_TOP = 3;
    private static final int STEP = 1;
    private static final int SLEEP_TIME = 500;
    private final FigureGenerator figureGenerator;
    private final Lock lock = new ReentrantLock();
    private Figure figure;
    private Figure nextFigure;
    private final int[][] field;
    private int xLeftTop;
    private int yLeftTop;
    private int score;
    private int speed;
    private int speedTmp;
    private int level = 1;
    private State state = State.NEW;
    private Thread gameThread;
    private final List<Consumer<TetrisState>> observable = new CopyOnWriteArrayList<>();
    private final String id;
    private TetrisSettings tetrisSettings;

    public Tetris() {
        speed = SLEEP_TIME;
        field = new int[FIELD_HEIGHT][FIELD_WIDTH];
        figureGenerator = new FigureGenerator();
        nextFigure = figureGenerator.getNext();
        id = UUID.randomUUID().toString();
        tetrisSettings = new TetrisSettings();
    }

    public Tetris(FigureGenerator figureGenerator, String id, TetrisSettings tetrisSettings) {
        this.speed = SLEEP_TIME;
        this.field = new int[FIELD_HEIGHT][FIELD_WIDTH];
        this.figureGenerator = figureGenerator;
        this.nextFigure = figureGenerator.getNext();
        this.id = id;
        this.tetrisSettings = tetrisSettings == null ? new TetrisSettings() : tetrisSettings;
    }

    @TestOnly
    Tetris(int[][] field, Figure figure, int x, int y) {
        assert x >= 0 && x < 10;
        assert y >= 0 && y < 24;
        speed = SLEEP_TIME;
        this.field = field;
        this.figure = figure;
        this.xLeftTop = x;
        this.yLeftTop = y;
        figureGenerator = new FigureGenerator();
        nextFigure = figureGenerator.getNext();
        id = UUID.randomUUID().toString();
    }

    public void start() {
        lock.lock();
        if (state != State.NEW) {
            throw new IllegalStateException(format("state should be %s but state %s", State.NEW, state));
        }
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
        figure = nextFigure;
        nextFigure = figureGenerator.getNext();

        xLeftTop = START_X_LEFT_TOP;
        int[][] figureState = figure.getState();
        yLeftTop = 4 - figureState.length;
        for (int value : figureState[figureState.length - 1]) {
            if (value != CELL_EMPTY) {
                return;
            }
        }
        yLeftTop++;
    }

    private void game() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(speed);
                try {
                    lock.lock();
                    if (state == State.GAME) {
                        downInner();
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    void downInner() {
        int lineCount = 0;
        if (contact()) {
            figureToField(field);
            lineCount = doScore();
            checkGameOver();
            generateFigure();
            normalSpeed();
            checkDifficulty(lineCount);
        } else {
            yLeftTop++;
        }
        notifyObserves(lineCount > 3);
    }

    private void checkDifficulty(int lineCount) {
        if (lineCount == 0 || level >= 10) return;
        int newLevel = score / tetrisSettings.getScoreLevelDelta() + 1;
        if (newLevel > level) {
            level = newLevel;
            speed = Math.max(SLEEP_TIME - (tetrisSettings.getSpeedDelta() * (newLevel - 1)), BOOST_SPEED);
        }
    }

    public void pauseOrResume() {
        lock.lock();
        try {
            if (state == State.GAME) {
                state = State.PAUSE;
                notifyObserves();
            } else if (state == State.PAUSE) {
                state = State.GAME;
                notifyObserves();
            }
        } finally {
            lock.unlock();
        }

    }

    public void fastSpeed() {
        lock.lock();
        try {
            if (speed != BOOST_SPEED) {
                speedTmp = speed;
                speed = BOOST_SPEED;
            }
        } finally {
            lock.unlock();
        }
    }

    public void normalSpeed() {
        lock.lock();
        try {
            if (speedTmp > 0) {
                speed = speedTmp;
                speedTmp = 0;
            }
        } finally {
            lock.unlock();
        }
    }

    public void fastOrNormalSpeed() {
        lock.lock();
        try {
            if (speed != BOOST_SPEED) {
                speedTmp = speed;
                speed = BOOST_SPEED;
            } else if (speedTmp > 0) {
                speed = speedTmp;
                speedTmp = 0;
            }
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            if (gameThread != null) {
                gameThread.interrupt();
            }
            gameThread = null;
            state = State.OVER;
            notifyObserves();
            observable.clear();
        } finally {
            lock.unlock();
        }
    }

    public void addObserver(Consumer<TetrisState> observer) {
        observable.add(observer);
    }

    public void removeObserver(Consumer<TetrisState> observer) {
        observable.remove(observer);
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

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setLevel(int newLevel) {
        if (newLevel < 2 || newLevel > 10) return;
        lock.lock();
        if (newLevel > level) {
            level = newLevel;
            var newSpeed = Math.max(SLEEP_TIME - (tetrisSettings.getSpeedDelta() * (newLevel - 1)), BOOST_SPEED);
            if (speedTmp > 0) {
                speedTmp = newSpeed;
            } else {
                speed = newSpeed;
            }
        }
        lock.unlock();
    }

    public void addRow() {

    }

    void toLeftInner() {
        boolean doLeft = true;
        int[][] figureState = figure.getState();
        for (int i = 0; i < figureState.length; i++) {
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
            int[] figureRow = figureState[i];
            for (int j = figureRow.length - 1; j >= 0; j--) {
                if (figureState[i][j] > CELL_EMPTY) {
                    if ((xLeftTop + j + STEP) >= FIELD_WIDTH || field[yLeftTop + i][xLeftTop + j + STEP] > CELL_EMPTY) {
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
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (field[3][i] != CELL_EMPTY) {
                stop();
                break;
            }
        }
    }

    private boolean contact() {
        int[][] state = figure.getState();
        for (int i = 0; i < state.length; i++) {
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

    private int doScore() {
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
        }
        return lineCount;
    }

    private void figureToField(int[][] filedMatrix) {
        int[][] state = figure.getState();
        for (int i = 0; i < state.length; i++) {
            int[] figureRow = state[i];
            for (int j = 0; j < figureRow.length; j++) {
                if (state[i][j] > CELL_EMPTY) {
                    filedMatrix[yLeftTop + i][xLeftTop + j] = state[i][j];
                }
            }
        }
    }

    int[][] getFieldState() {
        int[][] cloneFiled = new int[20][];
        for (int i = 0; i < 20; i++) {
            cloneFiled[i] = field[i + 4].clone();
        }

        int[][] state = figure.getState();
        for (int i = 0; i < state.length; i++) {
            int[] figureRow = state[i];
            for (int j = 0; j < figureRow.length; j++) {
                if ((yLeftTop + i) > 3 && state[i][j] > CELL_EMPTY) {
                    cloneFiled[yLeftTop + i - 4][xLeftTop + j] = state[i][j];
                }
            }
        }

        return cloneFiled;
    }

    private void notifyObserves() {
        notifyObserves(false);
    }

    private void notifyObserves(boolean tetris) {
        if (observable.isEmpty()) return;
        TetrisState state = getTetrisState(tetris);
        observable.forEach(o -> o.accept(state));
    }

    private TetrisState getTetrisState(boolean tetris) {
        return new TetrisState(id, getFieldState(), nextFigure.getState(), score, level, state, tetris);
    }

    public enum State {
        NEW, GAME, PAUSE, OVER
    }
}
