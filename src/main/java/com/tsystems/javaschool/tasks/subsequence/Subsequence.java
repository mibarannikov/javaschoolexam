package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;

public class Subsequence {
    public static void main(String[] args) {

    }

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) throws IllegalArgumentException {
        if (x == null | y == null) throw new IllegalArgumentException();
        if (x.size() == 0) {
            return true;
        }
        if (x.size() == 0 & y.size() == 0) {
            return true;
        }
        int i = 0;
        for (int j = 0; j < y.size(); j++) {
            if (x.get(i).equals(y.get(j))) {
                i++;
            }
            if (i == x.size() - 1) {
                return true;
            }

        }

        return false;
    }
}
