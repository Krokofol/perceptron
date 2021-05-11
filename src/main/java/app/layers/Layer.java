package app.layers;

import app.layers.matrix.Matrix;

public class Layer {

    //передыдущий слой
    public Layer prevLayer;
    public Integer neuronsNumber;

    //neurons[i] = sigmoid(neurons[i])
    public Matrix neurons;
    //prevLayer.neurons.transform.multiply(weights);
    public Matrix neuronsCalcResult;
    //веса
    public Matrix weights;
    public Matrix biases;
    //ошибки
    public Matrix errors;
    public Double[] answers;

    public Layer(Integer neuronsNumber) {
        prevLayer = null;
        weights = null;
        this.neuronsNumber = neuronsNumber;
    }

    public Layer(Integer neuronsNumber, Layer prevLayer) {
        this.prevLayer = prevLayer;
        this.neuronsNumber = neuronsNumber;
        weights = Matrix.createZeroMatrix(prevLayer.getNeuronsNumber(), neuronsNumber);
        biases = Matrix.createRandomMatrix(1, neuronsNumber);
    }

    public void calculate() {
        neuronsCalcResult = prevLayer.getNeurons().mult(weights).plus(biases);
        Double[] data = new Double[neuronsCalcResult.numCols()];
        for (int i = 0; i < neuronsCalcResult.numCols(); i++) {
            data[i] = sigmoid(neuronsCalcResult.toDouble()[0][i]);
        }
        neurons = Matrix.createMatrix(toMatrix(data));
    }

    public void calculateError(Double[] answer) {
        System.out.println("Mmm, chill...");
    }

    public Matrix getNeurons() {
        return neurons;
    }

    public void setData(Double[] data) {
        neurons = Matrix.createMatrix(toMatrix(data));
    }

    protected Double[][] toMatrix(Double[] data) {
        return new Double[][]{data};
    }

    public String getData() {
        return neurons.toString();
    }

    public Integer getNeuronsNumber() {
        return neuronsNumber;
    }

    public String getErrors() {
        return errors.toString();
    }

    public Matrix calculateGradient(Double studySpeed) {
        return Matrix.createZeroMatrix(neurons.numRows(), neurons.numCols()).plus(studySpeed, neurons.elementMult(Matrix.createAllOneMatrix(neurons.numRows(), neurons.numCols()).minus(neurons)).elementMult(errors));
    }

    public Matrix calculateDelta(Matrix gradient) {
        return gradient.transpose().mult(prevLayer.neurons);
    }

    protected Double sigmoid(Double input) {
        return 1 / (1 + Math.exp(-input));
    }
}
