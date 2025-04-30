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

public class Fox extends Predator implements Livable {
    public static final int PROBABILITY_EATS_HARE = 70;
    public static final int PROBABILITY_EATS_HAMSTER = 90;
    public static final int PROBABILITY_EATS_DUCK = 60;
    public static final int PROBABILITY_EATS_CATERPILLAR = 40;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.FOX.getWeight());

    @Override
    public Data getData() {
        return Data.FOX;
    }

    @Override
    public Cell getCurrentcell() {
        return currentCell;
    }

    @Override
    public void setCurrentCell(Cell cell) {
        this.currentCell = cell;
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
        System.out.println("Кабан передвигается...");

        if (currentCell == null) {
            System.out.println("У кабана нет клетки!");
            return;
        }

        int maxSpeed = Data.FOX.getMaxSpeed();
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

        Cell newCell = currentCell.getIsland().getCell(newX, newY);

        // Синхронизация для атомарного перемещения
        synchronized (currentCell) {
            synchronized (newCell) {
                if (currentCell.getAnimals().contains(this)) {
                    currentCell.removeAnimal(this);
                    newCell.addAnimal(this);
                    this.currentCell = newCell;
                    System.out.println("Fox moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);
            System.out.println("Fox died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.FOX.getMaxQuantity()) {
            return Optional.of(new Fox());
        }

        return Optional.empty();
    }
}
