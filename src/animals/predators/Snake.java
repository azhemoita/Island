package animals.predators;

import data.Data;
import factory.Predator;

import java.util.concurrent.atomic.AtomicReference;

public class Snake extends Predator {
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.SNAKE.getWeight());

    @Override
    public Data getData() {
        return Data.SNAKE;
    }

    @Override
    public double getCurrentWeight() {
        return currentWeight.get();
    }

    @Override
    public void setCurrentWeight(double currentWeight) {
        this.currentWeight.set(currentWeight);
    }
}
