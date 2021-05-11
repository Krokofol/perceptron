package app.layers;

import app.NeuralNetwork;
import org.apache.commons.math3.util.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class MainJob {

    private static final Integer[] neuronsForLayers = new Integer[]{15, 70, 70, 2};
    private static final Integer epochsNumber = 1000;
    private static final Double studySpeed = 0.1;

    private HashMap<Integer, Pair<Double, Double>> minAndMax;

    private Double[][] trainStartData;
    private Double[][] trainResultData;

    private Double[][] realJobStartData;
    private Double[][] realJobResultData;

    public void mainJob() {
        try {
            preloadTrainData();
            preloadRealJobData();
            normalizeData();
            NeuralNetwork readyForJobNeuralNetwork = createReadyForJobNeuralNetwork();
            calculateRealJob(readyForJobNeuralNetwork);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NeuralNetwork createReadyForJobNeuralNetwork() {
        NeuralNetwork readyForJobNeuralNetwork = new NeuralNetwork(neuronsForLayers.length, neuronsForLayers);
        readyForJobNeuralNetwork.study(trainStartData, trainResultData, studySpeed, epochsNumber);
        return  readyForJobNeuralNetwork;
    }

    public void preloadTrainData() throws IOException {
        File input = new File("src/main/resources/testSet.csv");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(input), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        int colCount;
        int rowCount = 1;
        colCount = reader.readLine().split(",").length;
        while (reader.readLine() != null) {
            rowCount++;
        }
        isr = new InputStreamReader(new FileInputStream(input), StandardCharsets.UTF_8);
        reader = new BufferedReader(isr);

        trainStartData = new Double[rowCount][colCount - 2];
        trainResultData = new Double[rowCount][2];
        minAndMax = new HashMap<>();
        for (int i = 0; i < colCount; i++) {
            minAndMax.put(i, new Pair<>(Double.MAX_VALUE, -Double.MAX_VALUE));
        }

        for (int i = 0; i < rowCount; i++) {
            String[] data = reader.readLine().split(",");
            int j;
            for (j = 0; j < colCount - 2; j++) {
                if (data[j].equals("nan")) {
                    trainStartData[i][j] = null;
                    continue;
                }
                trainStartData[i][j] = Double.parseDouble(data[j]);
                if (trainStartData[i][j] < minAndMax.get(j).getFirst()) {
                    minAndMax.put(j, new Pair<>(trainStartData[i][j], minAndMax.get(j).getSecond()));
                }
                if (trainStartData[i][j] > minAndMax.get(j).getSecond()) {
                    minAndMax.put(j, new Pair<>(minAndMax.get(j).getFirst(), trainStartData[i][j]));
                }
            }
            for (;j < colCount; j++) {
                if (data[j].equals("nan")) {
                    trainResultData[i][j - 15] = null;
                    continue;
                }
                trainResultData[i][j - 15] = Double.parseDouble(data[j]);
                if (trainResultData[i][j - 15] < minAndMax.get(j).getFirst()) {
                    minAndMax.put(j, new Pair<>(trainResultData[i][j - 15], minAndMax.get(j).getSecond()));
                }
                if (trainResultData[i][j - 15] > minAndMax.get(j).getSecond()) {
                    minAndMax.put(j, new Pair<>(minAndMax.get(j).getFirst(), trainResultData[i][j - 15]));
                }
            }
        }
    }

    public void preloadRealJobData() throws IOException {
        File input = new File("src/main/resources/trainSet.csv");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(input), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        int colCount;
        int rowCount = 1;
        colCount = reader.readLine().split(",").length;
        while (reader.readLine() != null) {
            rowCount++;
        }
        isr = new InputStreamReader(new FileInputStream(input), StandardCharsets.UTF_8);
        reader = new BufferedReader(isr);

        realJobStartData = new Double[rowCount][colCount - 2];
        realJobResultData = new Double[rowCount][2];
        for (int i = 0; i < colCount; i++) {
            minAndMax.put(i, new Pair<>(Double.MAX_VALUE, -Double.MAX_VALUE));
        }

        for (int i = 0; i < rowCount; i++) {
            String[] data = reader.readLine().split(",");
            int j;
            for (j = 0; j < colCount - 2; j++) {
                if (data[j].equals("nan")) {
                    realJobStartData[i][j] = null;
                    continue;
                }
                realJobStartData[i][j] = Double.parseDouble(data[j]);
                if (realJobStartData[i][j] < minAndMax.get(j).getFirst()) {
                    minAndMax.put(j, new Pair<>(realJobStartData[i][j], minAndMax.get(j).getSecond()));
                }
                if (realJobStartData[i][j] > minAndMax.get(j).getSecond()) {
                    minAndMax.put(j, new Pair<>(minAndMax.get(j).getFirst(), realJobStartData[i][j]));
                }
            }
            for (;j < colCount; j++) {
                if (data[j].equals("nan")) {
                    realJobResultData[i][j - 15] = null;
                    continue;
                }
                realJobResultData[i][j - 15] = Double.parseDouble(data[j]);
                if (realJobResultData[i][j - 15] < minAndMax.get(j).getFirst()) {
                    minAndMax.put(j, new Pair<>(realJobResultData[i][j - 15], minAndMax.get(j).getSecond()));
                }
                if (realJobResultData[i][j - 15] > minAndMax.get(j).getSecond()) {
                    minAndMax.put(j, new Pair<>(minAndMax.get(j).getFirst(), realJobResultData[i][j - 15]));
                }
            }
        }

    }

    public void normalizeData() {
        for (int i = 0; i < trainStartData.length; i++) {
            for (int j = 0; j < trainStartData[i].length; j++) {
                if (trainStartData[i][j] == null) {
                    trainStartData[i][j] = 0.5d;
                    continue;
                }
                trainStartData[i][j] = (trainStartData[i][j] - minAndMax.get(j).getFirst()) / (minAndMax.get(j).getSecond() - minAndMax.get(j).getFirst());
            }
        }
        for (int i = 0; i < trainResultData.length; i++) {
            for (int j = 0; j < trainResultData[i].length; j++) {
                if (trainResultData[i][j] == null) {
                    trainResultData[i][j] = 0.5d;
                    continue;
                }
                trainResultData[i][j] = (trainResultData[i][j] - minAndMax.get(j + 15).getFirst()) / (minAndMax.get(j + 15).getSecond() - minAndMax.get(j + 15).getFirst());
            }
        }
        for (int i = 0; i < realJobStartData.length; i++) {
            for (int j = 0; j < realJobStartData[i].length; j++) {
                if (realJobStartData[i][j] == null) {
                    realJobStartData[i][j] = 0.5d;
                    continue;
                }
                realJobStartData[i][j] = (realJobStartData[i][j] - minAndMax.get(j).getFirst()) / (minAndMax.get(j).getSecond() - minAndMax.get(j).getFirst());
            }
        }
        for (int i = 0; i < realJobResultData.length; i++) {
            for (int j = 0; j < realJobResultData[i].length; j++) {
                if (realJobResultData[i][j] == null) {
                    realJobResultData[i][j] = 0.5d;
                    continue;
                }
                realJobResultData[i][j] = (realJobResultData[i][j] - minAndMax.get(j + 15).getFirst()) / (minAndMax.get(j + 15).getSecond() - minAndMax.get(j + 15).getFirst());
            }
        }
    }

    public void calculateRealJob(NeuralNetwork readyForJobNeuralNetwork) {
        for (int i = 0; i < realJobStartData.length; i++) {
            System.out.println("input : " + Arrays.toString(realJobStartData[i]));
            readyForJobNeuralNetwork.setData(realJobStartData[i]);
            readyForJobNeuralNetwork.calculate();
            readyForJobNeuralNetwork.calculateErrors(realJobResultData[i]);
            System.out.print("result : " + readyForJobNeuralNetwork.getResult());
            System.out.println("expected : " + Arrays.toString(realJobResultData[i]));
            System.out.println("error : " + readyForJobNeuralNetwork.getErrors());
        }
    }
}
