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

public class Caterpillar extends Herbivore implements Livable {
    public static final int PROBABILITY_EATS_PLANT = 100;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.CATERPILLAR.getWeight());

    @Override
    public Data getData() {
        return Data.CATERPILLAR;
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
        List<Plant> plants = currentCell.getPlants();

        if (plants == null) return;

        while (currentWeight.get() < Data.CATERPILLAR.getWeight()) {
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
        System.out.println("Гусеница передвигается...");

        if (currentCell == null) {
            System.out.println("У гусеницы нет клетки!");
            return;
        }

        int maxSpeed = Data.CATERPILLAR.getMaxSpeed();
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
                    System.out.println("Caterpillar moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);
            System.out.println("Caterpillar died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.CATERPILLAR.getMaxQuantity()) {
            return Optional.of(new Caterpillar());
        }

        return Optional.empty();
    }
}
