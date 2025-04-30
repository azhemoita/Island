package factory;

import animals.herbivores.*;
import data.Data;
import field.Cell;

import java.util.Optional;

public class Herbivore extends Animal {
    Cell currentCell;

    @Override
    public Livable reproduce(Data animalName) {

        return switch (animalName) {
             case DUCK -> new Duck();
             case CATERPILLAR -> new Caterpillar();
             case HORSE -> new Horse();
             case DEER -> new Deer();
             case BUFFALO -> new Buffalo();
             case HARE -> new Hare();
             case BOAR -> new Boar();
             case HAMSTER -> new Hamster();
             case SHEEP -> new Sheep();
             case GOAT -> new Goat();
            default -> {
                System.out.println("There is no such herbivore");
                yield null; // Optional<Livable>
            }
        };
    }
}
