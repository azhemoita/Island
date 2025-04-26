package factory;

import animals.herbivores.*;
import data.Data;

public class Herbivore extends Animal {

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
