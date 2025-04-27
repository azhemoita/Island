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

public class Horse extends Herbivore implements Livable {
    public static final int PROBABILITY_EATS_PLANT = 100;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.HORSE.getWeight());

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public void eat() {
        List<Plant> plants = currentCell.getPlants();

        if (plants == null) return;

        while (currentWeight.get() < Data.HORSE.getWeight()) {
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
        int maxSpeed = Data.HORSE.getMaxSpeed();
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

        if (count >= 2 && count < Data.HORSE.getMaxQuantity()) {
            return Optional.of(new Horse());
        }

        return Optional.empty();
    }
}
