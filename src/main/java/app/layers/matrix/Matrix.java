package app.layers.matrix;

import org.ejml.simple.SimpleMatrix;

import java.util.Random;

public class Matrix extends SimpleMatrix {

    public static Matrix createRandomMatrix(Integer rowNum, Integer colNum) {
        Random random = new Random();
        double[][] data = new double[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                data[i][j] = ((double) (random.nextInt() % 1800)) / 1000d;
            }
        }
        return new Matrix(data);
    }

    public static Matrix createAllOneMatrix(Integer rowNum, Integer colNum) {
        double[][] data = new double[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                data[i][j] = 1d;
            }
        }
        return new Matrix(data);
    }

    public static Matrix createZeroMatrix(Integer rowNum, Integer colNum) {
        return new Matrix(rowNum, colNum);
    }

    public static Matrix createMatrix(Double[][] value) {
        int rows = value.length;
        int columns = value[0].length;
        double[][] primitiveValue = new double[rows][columns];
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[i].length; j++) {
                primitiveValue[i][j] = value[i][j];
            }
        }
        return new Matrix(primitiveValue);
    }

    public Matrix(Integer rowNum, Integer colNum) {
        super(rowNum, colNum);
    }

    public Matrix(double[][] value) {
        super(value);
    }

    public Matrix(SimpleMatrix value) {
        super(value);
    }

    public Double[][] toDouble() {
        Double[][] value = new Double[numRows()][numCols()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numCols(); j++) {
                value[i][j] = get(i, j);
            }
        }
        return value;
    }

    public Matrix plus(double beta, SimpleMatrix B) {
        SimpleMatrix result = super.plus(beta, B);
        return new Matrix(result);
    }

    public Matrix elementMult(SimpleMatrix b) {
        return new Matrix(super.elementMult(b));
    }

    public Matrix mult(Matrix B) {
        return new Matrix(super.mult(B));
    }

    public Matrix plus(SimpleMatrix B) {
        return new Matrix(super.plus(B));
    }

    public Matrix minus(SimpleMatrix B) {
        return new Matrix(super.minus(B));
    }

    public Matrix transpose() {
        return new Matrix(super.transpose());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numRows(); i++) {
            result.append("[");
            for (int j = 0; j < numCols(); j++) {
                result.append(" ").append(get(i, j)).append(",");
            }
            result.append(" ]\n");
        }
        return result.toString().replace(", ]", " ]");
    }
}
