package animals.herbivores;

import data.Data;
import factory.Herbivore;
import factory.Livable;
import field.Cell;
import plants.Plant;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Duck extends Herbivore implements Livable {
    public static final int PROBABILITY_EATS_CATERPILLAR = 90;
    public static final int PROBABILITY_EATS_PLANT = 100;
    private Cell currentCell;
    private final AtomicReference<Double> currentWeight = new AtomicReference<>(Data.DUCK.getWeight());

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public void eat() {
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
        System.out.println("Duck moving...");
        /**
         * ThreadLocalRandom. Генерим случайное число от 0 до maxSpeed.
         * Задаём направление движения. Выполняем проверку границ. Меняем координаты.
         */
    }

        /**
         * ThreadLocalRandom. Проверяем currentQuantity. Если < maxQuantity,
         * то создаём новую утку.
         * */

    @Override
    public void die() {
        currentCell.getAnimals().remove(this);
    }

}
