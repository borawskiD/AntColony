package com.company;

import java.util.*;

public class Task {
    public final int TARGET_NUMBER = 6;
    public final int START_NUMBER = 0;
    public final int POPULATION_SIZE = 1;
    public final int AMOUNT_OF_CYCLE = 100;
    public int[][] cost = new int[7][7];
    public double[][] pheromone = new double[7][7];
    public boolean[][] visitedRoads = new boolean[7][7];
    public char[] symbolsName = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    public double[][] newPheromone;

    public void run() {
        fillArray(visitedRoads);
        initCostMatrix();
        pheromone = initPheromoneMatrix();
        newPheromone = initPheromoneMatrix();
        Ant[] population = new Ant[POPULATION_SIZE];

        for (int iterations = 0; iterations < AMOUNT_OF_CYCLE; iterations++) {
            System.out.println("\nIteration number: " + (iterations + 1));
            for (int i = 0; i < POPULATION_SIZE; i++) {
                System.out.println("\nAnt number: " + (i + 1));
                population[i] = new Ant(cost, pheromone, TARGET_NUMBER, START_NUMBER);
                population[i].goAhead();
                newPheromone = addPheromones(newPheromone, population[i].getNewPheromone());
            }
            pheromone = newPheromone;
        }
        System.out.println("Final pheromone:");
        displayPheromone(newPheromone);
        findTheBestWay();
    }

    public void findTheBestWay() {
        System.out.println("---");
        System.out.println("Found possible ways:");
        List<String> possibleRoads = new ArrayList<>();
        List<Character> road = new ArrayList<>();
        boolean exist = false;
        int sumOfCost = 0;
        road.add(symbolsName[START_NUMBER]);
        for (int r = 0; r < 7; r++) {
            double max = 0;
            int maxIndex = 0;
            for (int c = 0; c < 7; c++) {
                if (c > r) {
                    if (newPheromone[r][c] > max) {
                        max = newPheromone[r][c];
                        maxIndex = c;
                    }
                }
            }
            if (!road.contains(symbolsName[maxIndex])) {
                sumOfCost += cost[r][maxIndex];
                road.add(symbolsName[maxIndex]);
            }
        }
        for (Character character : road) {
            String output = character != symbolsName[TARGET_NUMBER] ? character + " -> " : "" + character + "\n";
            System.out.print(output);
        }
        System.out.println("Cost: " + sumOfCost);
    }

    public void displayPheromone(double[][] matrix) {
        for (double[] ints : matrix) {
            for (double anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println("\n");
        }
    }

    public void initCostMatrix() {
        //                  A   B   C  D  E   F  G
        cost[0] = new int[]{0, 18, 15, 14, 0, 0, 0};
        cost[1] = new int[]{18, 0, 0, 2, 0, 14, 0};
        cost[2] = new int[]{15, 0, 0, 10, 17, 0, 0};
        cost[3] = new int[]{14, 2, 10, 0, 17, 18, 0};
        cost[4] = new int[]{0, 0, 17, 17, 0, 4, 10};
        cost[5] = new int[]{0, 14, 0, 18, 4, 0, 8};
        cost[6] = new int[]{0, 0, 0, 0, 10, 8, 0};
    }

    void fillArray(boolean[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int i1 = 0; i1 < array.length; i1++) {
                array[i][i1] = false;
            }
        }
    }

    public double[][] addPheromones(double[][] one, double[][] two) {
        double[][] finalPheromone = new double[7][7];
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                finalPheromone[r][c] = one[r][c] + two[r][c];
            }
        }
        return finalPheromone;
    }

    public double[][] initPheromoneMatrix() {
        double[][] initPheromone = new double[7][7];
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (cost[r][c] > 0) {
                    initPheromone[r][c] = 1.0;
                }
            }
        }
        return initPheromone;
    }
}
