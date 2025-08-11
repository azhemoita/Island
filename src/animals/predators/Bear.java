package animals.predators;

import data.Data;
import factory.Predator;
import java.util.concurrent.atomic.AtomicReference;

public class Bear extends Predator {
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.BEAR.getWeight());

    @Override
    public Data getData() {
        return Data.BEAR;
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
