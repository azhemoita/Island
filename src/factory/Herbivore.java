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
             case DUCK -> new Duck(currentCell);
             case CATERPILLAR -> new Caterpillar(currentCell);
             case HORSE -> new Horse(currentCell);
             case DEER -> new Deer(currentCell);
             case BUFFALO -> new Buffalo(currentCell);
             case HARE -> new Hare(currentCell);
             case BOAR -> new Boar(currentCell);
             case HAMSTER -> new Hamster(currentCell);
             case SHEEP -> new Sheep(currentCell);
             case GOAT -> new Goat(currentCell);
            default -> {
                System.out.println("There is no such herbivore");
                yield null; // Optional<Livable>
            }
        };
    }
}
