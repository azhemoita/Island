package animals.predators;

import animals.herbivores.*;
import data.Data;
import factory.Livable;
import factory.Predator;
import field.Cell;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Fox extends Predator implements Livable {
    public static final int PROBABILITY_EATS_HARE = 70;
    public static final int PROBABILITY_EATS_HAMSTER = 90;
    public static final int PROBABILITY_EATS_DUCK = 60;
    public static final int PROBABILITY_EATS_CATERPILLAR = 40;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.FOX.getWeight());

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell newCell) {
        this.currentCell = newCell;
    }

    @Override
    public void eat() {
        List<Livable> animals = currentCell.getAnimals();

        if (animals == null) return;

        while (currentWeight.get() < Data.FOX.getWeight()) {
            List<Livable> hare = animals.stream().filter(Hare.class::isInstance).toList();
            List<Livable> hamsters = animals.stream().filter(Hamster.class::isInstance).toList();
            List<Livable> ducks = animals.stream().filter(Duck.class::isInstance).toList();
            List<Livable> caterpillars = animals.stream().filter(Caterpillar.class::isInstance).toList();

            if (hare.isEmpty() && hamsters.isEmpty() && caterpillars.isEmpty() && ducks.isEmpty()) {
                break;
            }

            hare.forEach(h -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_HARE;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.HARE.getWeight());
                    h.die();
                }
            });

            hamsters.forEach(hamster -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_HAMSTER;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.HAMSTER.getWeight());
                    hamster.die();
                }
            });

            ducks.forEach(duck -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_DUCK;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.DUCK.getWeight());
                    duck.die();
                }
            });

            caterpillars.forEach(caterpillar -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_CATERPILLAR;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.CATERPILLAR.getWeight());
                    caterpillar.die();
                }
            });
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void die() {
        currentCell.getAnimals().remove(this);
    }
}
