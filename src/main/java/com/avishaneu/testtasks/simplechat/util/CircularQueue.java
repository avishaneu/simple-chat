package com.avishaneu.testtasks.simplechat.util;

import java.util.ArrayList;

/**
 * Created by avishaneu on 8/28/17.
 */
public class CircularQueue<T> extends ArrayList<T> {
    private int maxSize;

    public CircularQueue(int size) {
        this.maxSize = size;
    }

    public boolean add(T element) {
        boolean success = super.add(element);
        if (size() > maxSize) {
            removeRange(0, size() - maxSize - 1);
        }
        return success;
    }
}
