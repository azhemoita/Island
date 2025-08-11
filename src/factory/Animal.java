package factory;

import data.Data;
import field.Cell;
import field.Coordinate;
import field.Island;
import util.Console;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal implements Livable{
    protected Cell currentCell;
    protected Island island;
    Map<Class<? extends Eatable>, Integer> diet = getData().getDiet();

    @Override
    public double getWeight() {
        return getData().getWeight();
    }

    @Override
    public Cell getCurrentCell() {
        return currentCell;
    }

    @Override
    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public void move() {
        Console.log(getClass().getSimpleName() + " is trying to move...");
        Cell currentCell = this.getCurrentCell();

        if (currentCell == null) {
            Console.log(getClass().getSimpleName() + " doesn't have a cell!");
            return;
        }

        int maxSpeed = getData().getMaxSpeed();
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
                    Console.log(getClass().getSimpleName() + " moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        Console.log(getClass().getSimpleName() + " is trying to reproduce at (" + currentCell.getCoordinate().getX()
                + ", " + currentCell.getCoordinate().getY() + ")");
        long count = getCurrentCell().getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        Data animalData = getData();

        if (count >= 2 && count < animalData.getMaxQuantity()) {
            Console.log(getClass().getSimpleName() + " has reproduced at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
            return Optional.of(AnimalFactory.createAnimal(animalData));
        }

        return Optional.empty();
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);

            Console.log(getClass().getSimpleName() + " died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");

        }
    }
}
