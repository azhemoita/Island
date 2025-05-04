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

public class Bear extends Predator implements Livable {
    public static final int PROBABILITY_EATS_SNAKE = 80;
    public static final int PROBABILITY_EATS_HORSE = 40;
    public static final int PROBABILITY_EATS_DEER = 80;
    public static final int PROBABILITY_EATS_HARE = 80;
    public static final int PROBABILITY_EATS_HAMSTER = 90;
    public static final int PROBABILITY_EATS_GOAT = 70;
    public static final int PROBABILITY_EATS_SHEEP = 70;
    public static final int PROBABILITY_EATS_BOAR = 50;
    public static final int PROBABILITY_EATS_BUFFALO = 50;
    public static final int PROBABILITY_EATS_DUCK = 10;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.BEAR.getWeight());

    @Override
    public Data getData() {
        return Data.BEAR;
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
        System.out.println("Bear at (" + currentCell.getCoordinate().getX()
                + ", " + currentCell.getCoordinate().getY() + ") is eating...");
        List<Livable> animals = currentCell.getAnimals();

        if (animals == null) return;

        while (currentWeight.get() < Data.BEAR.getWeight()) {
            List<Livable> snakes = animals.stream().filter(Snake.class::isInstance).toList();
            List<Livable> horses = animals.stream().filter(Horse.class::isInstance).toList();
            List<Livable> deer = animals.stream().filter(Deer.class::isInstance).toList();
            List<Livable> hare = animals.stream().filter(Hare.class::isInstance).toList();
            List<Livable> hamsters = animals.stream().filter(Hamster.class::isInstance).toList();
            List<Livable> goats = animals.stream().filter(Goat.class::isInstance).toList();
            List<Livable> sheep = animals.stream().filter(Sheep.class::isInstance).toList();
            List<Livable> boar = animals.stream().filter(Boar.class::isInstance).toList();
            List<Livable> buffalo = animals.stream().filter(Buffalo.class::isInstance).toList();
            List<Livable> ducks = animals.stream().filter(Duck.class::isInstance).toList();

            if (snakes.isEmpty() && horses.isEmpty() && deer.isEmpty() && hare.isEmpty() && hamsters.isEmpty() && goats.isEmpty() && sheep.isEmpty() && boar.isEmpty() && buffalo.isEmpty() && ducks.isEmpty()) {
                break;
            }

            snakes.forEach(snake -> {
                boolean isEat = ThreadLocalRandom.current().nextInt(0, 100) < PROBABILITY_EATS_SNAKE;
                if (isEat) {
                    currentWeight.updateAndGet(w -> w + Data.SNAKE.getWeight());
                    snake.die();
                }
            });

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
        System.out.println("Медведь передвигается...");
        Cell currentCell = this.getCurrentcell();

        if (currentCell == null) {
            System.out.println("У медведя нет клетки!");
            return;
        }

        int maxSpeed = Data.BEAR.getMaxSpeed();
        int speed = ThreadLocalRandom.current().nextInt(maxSpeed + 1);
        Coordinate coord = currentCell.getCoordinate();

        if (speed == 0) return;

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
                    System.out.println("Bear moved to (" + newX + ", " + newY + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        if (currentCell != null) {
            currentCell.removeAnimal(this);
            System.out.println("Bear died at (" + currentCell.getCoordinate().getX()
                    + ", " + currentCell.getCoordinate().getY() + ")");
        }
    }

    @Override
    public Optional<Livable> getOffspring() {
        System.out.println("Медведь пытается размножиться...");
        long count = currentCell.getAnimals().stream().filter(animal -> animal.getClass().equals(this.getClass())).count();

        if (count >= 2 && count < Data.BEAR.getMaxQuantity()) {
            System.out.println("Медведь размножился...");
            return Optional.of(new Bear());
        }

        return Optional.empty();
    }
}
