package app;

import app.layers.FirstLayer;
import app.layers.LastLayer;
import app.layers.Layer;
import app.layers.matrix.Matrix;

public class NeuralNetwork {

    public Matrix inputs;

    public Integer layersNumber;
    public Layer[] layers;

    public NeuralNetwork(Integer layersNumber, Integer[] neuronsInLayerNumber) {
        this.layersNumber = layersNumber;

        layers = new Layer[layersNumber];
        int i;
        layers[0] = new FirstLayer(neuronsInLayerNumber[0]);
        for (i = 1; i < layersNumber - 1; i++) {
            layers[i] = new Layer(neuronsInLayerNumber[i], layers[i - 1]);
        }
        layers[i] = new LastLayer(neuronsInLayerNumber[i], layers[i - 1]);
    }

    public void setData(Double[] data) {
        inputs = Matrix.createMatrix(toMatrix(data));
        layers[0].setData(data);
    }

    public void calculateErrors(Double[] answers) {
        layers[layersNumber - 1].calculateError(answers);
    }

    public void calculate() {
        for (int i = 1; i < layersNumber; i++) {
            layers[i].calculate();
        }
    }

    public void backProp(Double studySpeed) {
        Matrix nextErrors = layers[layersNumber - 1].errors;
        Matrix deltas;
        Matrix gradient;
        for (int i = layersNumber - 1; i > 0; i--) {
            layers[i].errors = nextErrors;
            gradient = layers[i].calculateGradient(studySpeed);
            deltas = layers[i].calculateDelta(gradient);
            nextErrors = layers[i].weights.mult(layers[i].errors.transpose()).transpose();
            layers[i].weights = layers[i].weights.plus(deltas.transpose());
            layers[i].biases = layers[i].biases.plus(gradient);
        }
    }

    public void study(Double[][] startData, Double[][] answers, Double studySpeed, Integer epochs) {
        for (int epochIterator = 0; epochIterator < epochs; epochIterator++) {
            if (epochIterator % (epochs / 100) == 0) {
                System.out.println(epochIterator * 100 / epochs + "%");
            }
            for (int inputIterator = 0; inputIterator < answers.length; inputIterator++) {
                setData(startData[inputIterator]);
                calculate();
                calculateErrors(answers[inputIterator]);
                backProp(studySpeed);
            }
        }
    }

    public String getResult() {
        return layers[layersNumber - 1].getData();
    }

    public String getErrors() {
        return layers[layersNumber - 1].getErrors();
    }

    protected Double[][] toMatrix(Double[] data) {
        return new Double[][]{data};
    }
}
