package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Ant {
    private static final double VAPORIZATION_CONSTANT = 0.5;
    public int[][] cost;
    public double[][] pheromone;
    public boolean[][] visitedRoads = new boolean[7][7];
    public char[] symbolsName = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    public final int TARGET_NUMBER;
    private double[][] newPheromone;
    private int START_POSITION;
    int sumOfCost = 0;

    public Ant(int[][] cost, double[][] pheromone, int TARGET_NUMBER, int START_POSITION) {
        this.cost = cost;
        this.pheromone = pheromone;
        this.TARGET_NUMBER = TARGET_NUMBER;
        this.START_POSITION = START_POSITION;
        fillBooleanArray(visitedRoads);
    }

    public void goAhead() {
        findWays(START_POSITION);
    }

    void fillBooleanArray(boolean[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int i1 = 0; i1 < array.length; i1++) {
                array[i][i1] = false;
            }
        }
    }

    public void findWays(int position) {
        int[] possibleWays = cost[position];
        double denominator = 0.0;

        for (int i = 0; i < 7; i++) {
            if (possibleWays[i] != 0) {
                denominator += pheromone[position][i] * (1 / (double) possibleWays[i]);
            }
        }
        //cannot merge this 2 loops into 1 loop - we need denominator to be calculated.
        Double[] probability = new Double[7];
        for (int i = 0; i < possibleWays.length; i++) {
            if (possibleWays[i] != 0) {
                double numerator = pheromone[position][i] * (1 / (double) possibleWays[i]);
                probability[i] = numerator / denominator;
            } else {
                probability[i] = 0.0;
            }
        }

        Double[] unsorted = Arrays.copyOf(probability, probability.length);
        Arrays.sort(probability, Collections.reverseOrder());
        double current;
        int index = 1;
        ArrayList<Double> ranges = new ArrayList<>();
        ranges.add(1.0);
        do {
            ranges.add(ranges.get(index - 1) - probability[(index - 1)]);
            index++;
            current = probability[index];
        } while (current != 0);
        //System.out.println(ranges);
        int id = 0;
        do {
            Random r = new Random();
            double rand = r.nextDouble();
            //System.out.println(rand);
            for (int i = 0; i < ranges.size(); i++) {
                if (rand > ranges.get(i)) {
                    id = indexOf(unsorted, probability[i - 1]);
                    break;
                }
            }
        } while (id == position);
        displayStatus(id, position);

    }

    public void displayStatus(int id, int position) {
        if (id != TARGET_NUMBER) {
            System.out.print("(" + symbolsName[position] + " --> " + symbolsName[id] + ") ");
            addCost(visitedRoads, position, id);
            findWays(id);
        } else {
            System.out.print("(" + symbolsName[position] + " --> " + symbolsName[id] + ") ");
            System.out.println("\nFound! I'm at destination: " + symbolsName[id]);
            addCost(visitedRoads, position, id);
            System.out.println("Total price: " + sumOfCost);
            generateNewPheromone();
        }
    }

    public double[][] getNewPheromone() {
        return newPheromone;
    }

    public void generateNewPheromone() {
        newPheromone = new double[7][7];
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (visitedRoads[r][c]) {
                    newPheromone[r][c] = (VAPORIZATION_CONSTANT * pheromone[r][c] + (1 / ((double) sumOfCost)));
                }
            }
        }
    }

    void addCost(boolean[][] array, int start, int end) {
        int price = cost[start][end];
        sumOfCost += price;
        array[start][end] = true;
        array[end][start] = true;
    }

    public int indexOf(Double[] array, double value) {
        for (int i = 0; i < array.length; i++) {
            if (Math.round(array[i] * 10000.0) / 10000.0 == Math.round(value * 10000.0) / 10000.0) return i;
        }
        return (-1);
    }

}
