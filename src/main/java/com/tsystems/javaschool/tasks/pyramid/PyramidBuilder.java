package com.tsystems.javaschool.tasks.pyramid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PyramidBuilder {
    public static void main(String[] args) {
        //new PyramidBuilder().buildPyramid(new ArrayList<Integer>(Arrays.asList(4, 1, 2, 5, 6, 3, 7)));

  }

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers)  {
        double pyramidHeight = 0.5 + Math.sqrt(1 + 8 * inputNumbers.size()) / 2 - 1;
        if (pyramidHeight - Math.floor(pyramidHeight) != 0) throw new CannotBuildPyramidException();
        try {
            inputNumbers.sort(Comparator.naturalOrder());
        } catch (NullPointerException e) {
            throw new CannotBuildPyramidException();
        }

        int[][] pyramid = new int[(int) pyramidHeight][(int) (2 * pyramidHeight - 1)];
        int indexColumnTopOfThePyramid = (int) pyramidHeight - 1;
        int countInputNambers = 0;
        for (int i = 0; i < pyramidHeight; i++) {
            for (int j = indexColumnTopOfThePyramid - i; j <= indexColumnTopOfThePyramid + i; j = j + 2) {
                pyramid[i][j] = inputNumbers.get(countInputNambers);
                countInputNambers++;
            }
        }
        return pyramid;
    }


}
