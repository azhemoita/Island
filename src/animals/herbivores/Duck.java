package animals.herbivores;

import data.Data;
import factory.Herbivore;
import factory.Livable;
import plants.Plant;
import util.Console;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Duck extends Herbivore {
    public static final int PROBABILITY_EATS_CATERPILLAR = 90;
    public static final int PROBABILITY_EATS_PLANT = 100;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.DUCK.getWeight());

    @Override
    public Data getData() {
        return Data.DUCK;
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
        Console.log("Duck at (" + getCurrentCell().getCoordinate().getX()
                + ", " + getCurrentCell().getCoordinate().getY() + ") is eating...");
        List<Livable> animals = getCurrentCell().getAnimals();
        List<Plant> plants = getCurrentCell().getPlants();

        if (animals == null || plants == null) {
            return;
        }

        while (currentWeight.get() < Data.DUCK.getWeight()) {
            List<Livable> caterpillars = animals.stream().filter(Caterpillar.class::isInstance).toList();

            if (caterpillars.isEmpty() && plants.isEmpty()) {
                break;
            }

            caterpillars.forEach(caterpillar -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_CATERPILLAR;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.CATERPILLAR.getWeight());
                    caterpillar.die();
                }
            });

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
