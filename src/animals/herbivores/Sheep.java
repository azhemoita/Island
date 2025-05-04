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

public class Sheep extends Herbivore implements Livable {
    public static final int PROBABILITY_EATS_PLANT = 100;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.SHEEP.getWeight());

    @Override
    public Data getData() {
        return Data.SHEEP;
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
        System.out.println("Sheep at (" + currentCell.getCoordinate().getX()
                + ", " + currentCell.getCoordinate().getY() + ") is eating...");
        List<Plant> plants = currentCell.getPlants();

        if (plants == null) return;

        while (currentWeight.get() < Data.SHEEP.getWeight()) {
            if (plants.isEmpty()) {
                break;
            }

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
        System.out.println("Овечка передвигается...");
        Cell currentCell = this.getCurrentcell();

        if (currentCell == null) {
            System.out.println("У овцы нет клетки!");
            return;
        }

        int maxSpeed = Data.SHEEP.getMaxSpeed();
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
                    System.out.println("Sheep moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);
            System.out.println("Sheep died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        System.out.println("Овечка пытается размножиться...");
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.SHEEP.getMaxQuantity()) {
            System.out.println("Овечка размножилась...");
            return Optional.of(new Sheep());
        }

        return Optional.empty();
    }
}
