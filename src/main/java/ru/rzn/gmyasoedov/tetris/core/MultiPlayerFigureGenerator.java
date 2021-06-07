package ru.rzn.gmyasoedov.tetris.core;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiPlayerFigureGenerator implements FigureGenerator {

    static final int INIT_FIGURE_COUNT = 20;
    static final int LIMIT_TO_RESIZE = 5;

    private final Map<String, BlockingQueue<Figure>> queueBySessionId = new ConcurrentHashMap<>();
    private final ArrayList<Figure> initFigures;
    private final Lock lock = new ReentrantLock();
    private volatile boolean block;

    public MultiPlayerFigureGenerator() {
        this.initFigures = getFigures();
    }

    private ArrayList<Figure> getFigures() {
        ArrayList<Figure> initFigures = new ArrayList<>(INIT_FIGURE_COUNT);
        for (int i = 0; i < INIT_FIGURE_COUNT; i++) {
            initFigures.add(generateFigure());
        }
        return initFigures;
    }

    @Override
    public String register() {
        if (block) {
            throw new IllegalStateException("game already started!");
        }
        String sessionId = UUID.randomUUID().toString();
        LinkedBlockingQueue<Figure> queue = new LinkedBlockingQueue<>(initFigures);
        queueBySessionId.put(sessionId, queue);
        return sessionId;
    }

    @Override
    public Figure getNext(String sessionId) {
        BlockingQueue<Figure> figures = getSessionFigures(sessionId);
        if (figures == null) {
            throw new IllegalStateException("no session " + sessionId);
        }
        Figure figure = figures.poll();
        if (figures.size() < LIMIT_TO_RESIZE) {
            lock.lock();
            try {
                //todo remove game over queue
                if (figures.size() < LIMIT_TO_RESIZE) {
                    ArrayList<Figure> newFigures = getFigures();
                    for (BlockingQueue<Figure> value : queueBySessionId.values()) {
                        value.addAll(newFigures);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return figure.clone();
    }

    public void block() {
        this.block = true;
    }

    BlockingQueue<Figure> getSessionFigures(String sessionId) {
        return queueBySessionId.get(sessionId);
    }
}
