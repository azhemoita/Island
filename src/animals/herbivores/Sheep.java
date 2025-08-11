package animals.herbivores;

import data.Data;
import factory.Herbivore;
import factory.Livable;
import plants.Plant;
import util.Console;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Sheep extends Herbivore {
    public static final int PROBABILITY_EATS_PLANT = 100;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.SHEEP.getWeight());

    @Override
    public Data getData() {
        return Data.SHEEP;
    }

    @Override
    public double getCurrentWeight() {
        return currentWeight.get();
    }

    @Override
    public void setCurrentWeight(double currentWeight) {
        this.currentWeight.set(currentWeight);
    }

    @Override
    public void eat() {
        Console.log("Sheep at (" + getCurrentCell().getCoordinate().getX()
                + ", " + getCurrentCell().getCoordinate().getY() + ") is eating...");
        List<Plant> plants = getCurrentCell().getPlants();

        if (plants == null) return;

        while (currentWeight.get() < Data.SHEEP.getWeight()) {
            if (plants.isEmpty()) {
                break;
            }

            plants.forEach(plant -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_PLANT;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Plant.WEIGHT);
                    getCurrentCell().getPlants().remove(plant);
                }
            });
        }
    }
}
