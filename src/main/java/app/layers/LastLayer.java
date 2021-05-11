package app.layers;

import app.layers.matrix.Matrix;

public class LastLayer extends Layer {

    public LastLayer(Integer neuronsNumber, Layer prevLayer) {
        super(neuronsNumber, prevLayer);
    }

    @Override
    public void calculateError(Double[] answer) {
        answers = answer;
        this.errors = Matrix.createMatrix(toMatrix(answer)).minus(neurons);
    }
}
