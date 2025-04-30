package factory;

import animals.predators.*;
import data.Data;
import field.Cell;

public class Predator extends Animal {
    private Cell currentCell;

    @Override
    public Livable reproduce(Data animalName) {
        return switch (animalName) {
            case WOLF -> new Wolf();
            case SNAKE -> new Snake();
            case EAGLE -> new Eagle();
            case FOX -> new Fox();
            case BEAR -> new Bear();
            default -> {
                System.out.println("There is no such predator");
                yield null;
            }
        };
    }
}
