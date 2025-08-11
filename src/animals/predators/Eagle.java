package animals.predators;

import data.Data;
import factory.Predator;

import java.util.concurrent.atomic.AtomicReference;

public class Eagle extends Predator {
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.EAGLE.getWeight());

    @Override
    public Data getData() {
        return Data.EAGLE;
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
