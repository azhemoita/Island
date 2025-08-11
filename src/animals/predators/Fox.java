package animals.predators;

import data.Data;
import factory.Predator;

import java.util.concurrent.atomic.AtomicReference;

public class Fox extends Predator {
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.FOX.getWeight());

    @Override
    public Data getData() {
        return Data.FOX;
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
