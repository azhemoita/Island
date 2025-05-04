package animals.herbivores;

import data.Data;
import factory.Herbivore;
import factory.Livable;
import field.Cell;
import field.Coordinate;
import plants.Plant;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Duck extends Herbivore implements Livable {
    public static final int PROBABILITY_EATS_CATERPILLAR = 90;
    public static final int PROBABILITY_EATS_PLANT = 100;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.DUCK.getWeight());

    @Override
    public Data getData() {
        return Data.DUCK;
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
        System.out.println("Duck at (" + currentCell.getCoordinate().getX()
                + ", " + currentCell.getCoordinate().getY() + ") is eating...");
        List<Livable> animals = currentCell.getAnimals();
        List<Plant> plants = currentCell.getPlants();

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
                    currentCell.getPlants().remove(plant);
                }
            });
        }
    }

    @Override
    public void move() {
        System.out.println("Утка передвигается...");
        Cell currentCell = this.getCurrentcell();

        if (currentCell == null) {
            System.out.println("У утки нет клетки!");
            return;
        }

        int maxSpeed = Data.DUCK.getMaxSpeed();
        int speed = ThreadLocalRandom.current().nextInt( maxSpeed + 1);

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
                    System.out.println("Duck moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);
            System.out.println("Duck died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        System.out.println("Утка пытается размножиться...");
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.DUCK.getMaxQuantity()) {
            System.out.println("Утка размножилась...");
            return Optional.of(new Duck());
        }

        return Optional.empty();
    }

}
