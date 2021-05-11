package app.layers;

public class FirstLayer extends Layer {

    public FirstLayer (Integer neuronsNumber) {
        super(neuronsNumber);
    }

    @Override
    public void calculate() {
        System.out.println("Mmm, chill...");
    }
}
