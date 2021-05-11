package app;

public class App {

    public static void main (String[] args) {
        new MainJob().mainJob();
//        sinAndCosTest();
    }

    public static void sinAndCosTest() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(4, new Integer[]{1, 70, 70, 2});
        Double[][] studyData = new Double[101][1];
        Double[][] answers = new Double[101][2];

        double startXValue = 0;
        for (int i = 0; i < 101; i++) {
            studyData[i][0] = startXValue / 2d / Math.PI;
            answers[i][0] = (Math.sin(startXValue) + 1d) / 2d;
            answers[i][1] = (Math.cos(startXValue) + 1d) / 2d;
            startXValue += Math.PI / 50;
        }

        neuralNetwork.study(studyData, answers, 0.1d, 1000);

        for (int i = 0; i < 101; i += 25) {
            neuralNetwork.setData(studyData[i]);
            neuralNetwork.calculate();
            neuralNetwork.calculateErrors(answers[i]);
            System.out.print("result : " + neuralNetwork.getResult());
            System.out.println("error : " + neuralNetwork.getErrors());
        }
    }
}
