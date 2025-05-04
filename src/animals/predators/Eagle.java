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

public class Eagle extends Predator implements Livable {
    public static final int PROBABILITY_EATS_FOX = 10;
    public static final int PROBABILITY_EATS_HARE = 90;
    public static final int PROBABILITY_EATS_HAMSTER = 90;
    public static final int PROBABILITY_EATS_DUCK = 80;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.EAGLE.getWeight());

    @Override
    public Data getData() {
        return Data.EAGLE;
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
        System.out.println("Eagle at (" + currentCell.getCoordinate().getX()
                + ", " + currentCell.getCoordinate().getY() + ") is eating...");
        List<Livable> animals = currentCell.getAnimals();

        if (animals == null) return;

        while (currentWeight.get() < Data.EAGLE.getWeight()) {
            List<Livable> fox = animals.stream().filter(Snake.class::isInstance).toList();
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
        System.out.println("Орел передвигается...");
        Cell currentCell = this.getCurrentcell();

        if (currentCell == null) {
            System.out.println("У орла нет клетки!");
            return;
        }

        int maxSpeed = Data.EAGLE.getMaxSpeed();
        int speed = ThreadLocalRandom.current().nextInt(maxSpeed + 1);

        if (speed == 0) return;

        Coordinate coord = currentCell.getCoordinate();
        int x = coord.getX();
        int y = coord.getY();

        int dx = ThreadLocalRandom.current().nextInt(-speed, speed + 1);
        int dy = (speed - Math.abs(dx)) * (ThreadLocalRandom.current().nextBoolean() ? 1 : -1);

        int newX = x + dx;
        int newY = y + dy;

        if (newX == x && newY == y) return;

        Cell newCell = null;
        if (currentCell.getIsland().isValidCoordinate(newX, newY)) {
            newCell = currentCell.getIsland().getCell(newX, newY);
        } else return;

        // Синхронизация для атомарного перемещения
        synchronized (currentCell) {
            synchronized (newCell) {
                if (currentCell.getAnimals().contains(this)) {
                    currentCell.removeAnimal(this);
                    newCell.addAnimal(this);
                    this.setCurrentCell(newCell);
                    System.out.println("Eagle moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);
            System.out.println("Eagle died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        System.out.println("Орёл пытается размножиться...");
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.EAGLE.getMaxQuantity()) {
            System.out.println("Орёл размножился...");
            return Optional.of(new Eagle());
        }

        return Optional.empty();
    }
}
