package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Task {
    public int[][] cost = new int[7][7];
    public double[][] pheromone = new double[7][7];
    public int[][] visitedRoads = new int[7][7];
    public char[] symbolsName = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    public final int TARGET_NUMBER = 6;
    public void run(){
        fillArray(visitedRoads,0);
        initCostMatrix();
        initPheromoneMatrix();
        findWays(0);
        displayCost(visitedRoads);
    }
    public void findWays(int position){
        //System.out.println("---");
        //System.out.println("I'm here: " + symbolsName[position]);
        //position - current index
        int[] possibleWays = cost[position];
//        for (int possibleWay : possibleWays) {
//            System.out.print(possibleWay + " ");
//        }
//        System.out.println("");
        double denominator = 0.0;
        for(int i = 0; i<7; i++){
            if (possibleWays[i] != 0) {
                //System.out.println((1/(double)possibleWays[i]));
                denominator += pheromone[position][i] * (1 /(double) possibleWays[i]);
            }
        }
        Double[] probability = new Double[7];
        for (int i = 0; i < possibleWays.length; i++) {
            if(possibleWays[i] != 0){
                double numerator = pheromone[position][i] * (1/(double) possibleWays[i]);
                probability[i] = numerator / denominator;
                //System.out.println("For " + i + " path probability = " + probability[i]);
            } else{
                probability[i] = 0.0;
            }
        }
        Double[] unsorted = Arrays.copyOf(probability,probability.length);
        Arrays.sort(probability, Collections.reverseOrder());
        //displayArray(unsorted);
        //displayArray(probability);
        double current;;
        int index = 1;
        ArrayList<Double> ranges = new ArrayList<>();
        ranges.add(1.0);
        //System.out.println(1 - probability[0]);
        do{
            ranges.add(ranges.get(index - 1) - probability[(index - 1)]);
            index++;
            current = probability[index];
        }while(current != 0);
        //System.out.println(ranges);
        int id = 0;
        do{
            Random r = new Random();
            double rand = r.nextDouble();
            //System.out.println(rand);
            for (int i = 0; i < ranges.size(); i++) {
                if (rand > ranges.get(i)){
                    //System.out.println(rand + ">" + ranges.get(i));
                    id = indexOf(unsorted,probability[i-1]);
                    //System.out.println(indexOf(unsorted,probability[i-1]));
                    //System.out.println(probability[i-1]);
                    break;
                }
            }
        }while(id == position);
        if(id != TARGET_NUMBER){
            System.out.println("(" + symbolsName[position] + " --> " + symbolsName[id] + ") ");
            addCost(visitedRoads,position,id);
            //System.out.println("I'm going to: " + symbolsName[id]);
            findWays(id);
        }else{
            System.out.println("\nFound! I'm at destination: " + symbolsName[id]);
            addCost(visitedRoads,position,id);
        }
    }
    public int indexOf(Double[] array, double value){
        for (int i = 0; i < array.length; i++) {
            if (Math.round(array[i] * 10000.0)/10000.0 == Math.round(value * 10000.0)/10000.0) return i;
        }
        return (-1);
    }
    public void displayArray(Double[] arr){
        for (double v : arr) {
            System.out.print(v + " ");
        }
        System.out.println("\n");
    }
    public void displayCost(int[][] matrix){
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println("\n");
        }
    }
    public void displayPheromone(double[][] matrix){
        for (double[] ints : matrix) {
            for (double anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println("\n");
        }
    }
    public void initCostMatrix(){
        //                  A   B   C  D  E   F  G
        cost[0] = new int[]{0, 18, 15, 14, 0, 0, 0};
        cost[1] = new int[]{18, 0, 0, 2, 0, 14, 0};
        cost[2] = new int[]{15, 0, 0, 10, 17, 0, 0};
        cost[3] = new int[]{14, 2, 10, 0, 17, 18, 0};
        cost[4] = new int[]{0, 0, 17, 17, 0, 4, 10};
        cost[5] = new int[]{0, 14, 0, 18, 4, 0, 8};
        cost[6] = new int[]{0, 0, 0, 0, 10, 8, 0};
    }
    void fillArray(int[][] array, int num){
        for (int i = 0; i < array.length; i++) {
            for (int i1 = 0; i1 < array.length; i1++) {
                array[i][i1] = num;
                System.out.print(array[i][i1] + " ");
            }
        }
    }
    void addCost(int[][] array, int start, int end){
        int price = cost[start][end];
        System.out.println("price is " + price);
        array[start][end] += price;
        array[end][start] += price;
    }
    public void initPheromoneMatrix(){
        for (int r = 0; r < 7; r++) {
            for(int c = 0; c < 7; c++){
                pheromone[r][c] = 1.0;
            }
        }
    }
}
