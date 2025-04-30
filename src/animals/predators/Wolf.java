package animals.predators;

import animals.herbivores.*;
import data.Data;
import factory.Livable;
import field.Cell;
import field.Coordinate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Wolf implements Livable {
    public static final int PROBABILITY_EATS_HORSE = 10;
    public static final int PROBABILITY_EATS_DEER = 15;
    public static final int PROBABILITY_EATS_HARE = 60;
    public static final int PROBABILITY_EATS_HAMSTER = 80;
    public static final int PROBABILITY_EATS_GOAT = 60;
    public static final int PROBABILITY_EATS_SHEEP = 70;
    public static final int PROBABILITY_EATS_BOAR = 15;
    public static final int PROBABILITY_EATS_BUFFALO = 15;
    public static final int PROBABILITY_EATS_DUCK = 40;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.WOLF.getWeight());

    @Override
    public Data getData() {
        return Data.WOLF;
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

        while (currentWeight.get() < Data.WOLF.getWeight()) {
            List<Livable> horses = animals.stream().filter(Horse.class::isInstance).toList();
            List<Livable> deer = animals.stream().filter(Deer.class::isInstance).toList();
            List<Livable> hare = animals.stream().filter(Hare.class::isInstance).toList();
            List<Livable> hamsters = animals.stream().filter(Hamster.class::isInstance).toList();
            List<Livable> goats = animals.stream().filter(Goat.class::isInstance).toList();
            List<Livable> sheep = animals.stream().filter(Sheep.class::isInstance).toList();
            List<Livable> boar = animals.stream().filter(Boar.class::isInstance).toList();
            List<Livable> buffalo = animals.stream().filter(Buffalo.class::isInstance).toList();
            List<Livable> ducks = animals.stream().filter(Duck.class::isInstance).toList();

            if (horses.isEmpty() && deer.isEmpty() && hare.isEmpty() && hamsters.isEmpty() && goats.isEmpty() && sheep.isEmpty() && boar.isEmpty() && buffalo.isEmpty() && ducks.isEmpty()) {
                break;
            }

            horses.forEach(horse -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_HORSE;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.HORSE.getWeight());
                    horse.die();
                }
            });

            deer.forEach(d -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_DEER;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.DEER.getWeight());
                    d.die();
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

            goats.forEach(goat -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_GOAT;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.GOAT.getWeight());
                    goat.die();
                }
            });

            sheep.forEach(sh -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_SHEEP;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.SHEEP.getWeight());
                    sh.die();
                }
            });

            boar.forEach(b -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_BOAR;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.BOAR.getWeight());
                    b.die();
                }
            });

            buffalo.forEach(buf -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_BUFFALO;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.BUFFALO.getWeight());
                    buf.die();
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
        System.out.println("Кабан передвигается...");

        if (currentCell == null) {
            System.out.println("У кабана нет клетки!");
            return;
        }

        int maxSpeed = Data.WOLF.getMaxSpeed();
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
                    System.out.println("Wolf moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);
            System.out.println("Wolf died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.WOLF.getMaxQuantity()) {
            return Optional.of(new Wolf());
        }

        return Optional.empty();
    }
}
