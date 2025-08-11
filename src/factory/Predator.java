package factory;

import plants.Plant;
import util.Console;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Predator extends Animal {

    @Override
    public void eat() {
        Console.log(getClass().getSimpleName() + " at (" + getCurrentCell().getCoordinate().getX()
                + ", " + getCurrentCell().getCoordinate().getY() + ") begins to eat...");

        Map<Class<? extends Eatable>, Integer> diet = getData().getDiet();

        if (diet == null || diet.isEmpty()) return;

        currentCell.getAnimals().stream().forEach(animal->{
            for (var entry : diet.entrySet()) {
                if (animal.getClass().equals(entry.getKey())) {
                    tryEat(animal, entry.getValue());
                }
            }
        });

        currentCell.getPlants().stream().forEach(plant->{
            for (var entry : diet.entrySet()) {
                if (plant.getClass().equals(entry.getKey())) {
                    tryEat(plant, entry.getValue());
                }
            }
        });
    }

    private void tryEat(Eatable food, int probability) {
        boolean isEat = ThreadLocalRandom.current().nextInt(100) < probability;

        if (isEat) {
            setCurrentWeight(getCurrentWeight() + food.getWeight());

            if (food instanceof Livable animal) {
                animal.die();
            } else if (food instanceof Plant plant) {
                plant.die();
            }
        }
    }
}
