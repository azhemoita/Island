package animals.herbivores;

import util.Console;
import data.Data;
import factory.Herbivore;
import factory.Livable;
import plants.Plant;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Boar extends Herbivore {
    public static final int PROBABILITY_EATS_HAMSTER =  50;
    public static final int PROBABILITY_EATS_CATERPILLAR = 90;
    public static final int PROBABILITY_EATS_PLANT = 100;
    private AtomicReference<Double> currentWeight = new AtomicReference<>(Data.BOAR.getWeight());

    @Override
    public Data getData() {
        return Data.BOAR;
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
        Console.log("Кабан на клетке (" + getCurrentCell().getCoordinate().getX()
                + ", " + getCurrentCell().getCoordinate().getY() + ") ест");

        List<Livable> animals = getCurrentCell().getAnimals();
        List<Plant> plants = getCurrentCell().getPlants();

        if (animals == null || plants == null) {
            return;
        }

        while (currentWeight.get() < Data.BOAR.getWeight()) {
            List<Livable> hamsters = animals.stream().filter(Hamster.class::isInstance).toList();
            List<Livable> caterpillars = animals.stream().filter(Caterpillar.class::isInstance).toList();

            if (hamsters.isEmpty() && caterpillars.isEmpty() && plants.isEmpty()) {
                break;
            }

            hamsters.forEach(hamster -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_HAMSTER;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.HAMSTER.getWeight());
                    hamster.die();
                }
            });

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
