package animals.predators;

import animals.herbivores.*;
import data.Data;
import factory.Livable;
import factory.Predator;
import field.Cell;
import field.Coordinate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Snake extends Predator implements Livable {
    public static final int PROBABILITY_EATS_FOX = 15;
    public static final int PROBABILITY_EATS_HARE = 20;
    public static final int PROBABILITY_EATS_HAMSTER = 40;
    public static final int PROBABILITY_EATS_DUCK = 10;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.SNAKE.getWeight());

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

        while (currentWeight.get() < Data.SNAKE.getWeight()) {
            List<Livable> fox = animals.stream().filter(Fox.class::isInstance).toList();
            List<Livable> hare = animals.stream().filter(Hare.class::isInstance).toList();
            List<Livable> hamsters = animals.stream().filter(Hamster.class::isInstance).toList();
            List<Livable> ducks = animals.stream().filter(Duck.class::isInstance).toList();

            if (fox.isEmpty() && hare.isEmpty() && hamsters.isEmpty() && ducks.isEmpty()) {
                break;
            }

            fox.forEach(f -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_FOX;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.FOX.getWeight());
                    f.die();
                }
            });

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
        }
    }

    @Override
    public void move() {
        int maxSpeed = Data.SNAKE.getMaxSpeed();
        int speed = ThreadLocalRandom.current().nextInt(0, maxSpeed + 1);
        Coordinate currentCellCoordinate = this.currentCell.getCoordinate();

        if (speed == 0) return;

        int x = currentCellCoordinate.getX();
        int y = currentCellCoordinate.getY();

        int dx = ThreadLocalRandom.current().nextInt(-speed, speed + 1);
        int randomSign = ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
        int dy = (speed - Math.abs(dx)) * randomSign;

        int newX = x + dx;
        int newY = y + dy;

        if (newX == x && newY == y) return;

        if (currentCell.getIsland().isValidCoordinate(newX, newY)) {
            Cell newCell = currentCell.getIsland().getCell(newX, newY);
            currentCell.getAnimals().remove(this);
            newCell.addAnimal(this);
            currentCell = newCell;
        }
    }

    @Override
    public void die() {
        currentCell.getAnimals().remove(this);
    }

    public Optional<Livable> getOffspring() {
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.SNAKE.getMaxQuantity()) {
            return Optional.of(new Snake());
        }

        return Optional.empty();
    }
}
