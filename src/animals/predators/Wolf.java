package animals.predators;

import data.Data;
import factory.Predator;

import java.util.concurrent.atomic.AtomicReference;

public class Wolf extends Predator {
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.WOLF.getWeight());

    @Override
    public Data getData() {
        return Data.WOLF;
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
