package factory;

import animals.predators.*;
import data.Data;
import field.Cell;

public class Predator extends Animal {
    private Cell currentCell;

    @Override
    public Livable reproduce(Data animalName) {
        return switch (animalName) {
            case WOLF -> new Wolf(currentCell);
            case SNAKE -> new Snake(currentCell);
            case EAGLE -> new Eagle(currentCell);
            case FOX -> new Fox(currentCell);
            case BEAR -> new Bear(currentCell);
            default -> {
                System.out.println("There is no such predator");
                yield null;
            }
        };
    }
}
