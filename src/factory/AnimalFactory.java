package factory;

import animals.herbivores.*;
import animals.predators.*;
import data.Data;
import field.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class AnimalFactory {
    private static final Map<Data, Function<Cell, Livable>> animalSuppliers = new HashMap<>();
    private static Cell cell;

    static {
        animalSuppliers.put(Data.DUCK, Duck::new);
        animalSuppliers.put(Data.CATERPILLAR, Caterpillar::new);
        animalSuppliers.put(Data.HORSE, Horse::new);
        animalSuppliers.put(Data.DEER, Deer::new);
        animalSuppliers.put(Data.BUFFALO, Buffalo::new);
        animalSuppliers.put(Data.HARE, Hare::new);
        animalSuppliers.put(Data.BOAR, Boar::new);
        animalSuppliers.put(Data.HAMSTER, Hamster::new);
        animalSuppliers.put(Data.SHEEP, Sheep::new);
        animalSuppliers.put(Data.GOAT, Goat::new);
        animalSuppliers.put(Data.WOLF, Wolf::new);
        animalSuppliers.put(Data.SNAKE, Snake::new);
        animalSuppliers.put(Data.EAGLE, Eagle::new);
        animalSuppliers.put(Data.FOX, Fox::new);
        animalSuppliers.put(Data.BEAR, Bear::new);
    }

    public static Livable createAnimal(Data animalName, Cell cell) throws IllegalAccessException {
        Function<Cell, Livable> creator = animalSuppliers.get(animalName);
        if (creator != null) {
            Livable animal = creator.apply(cell);
            cell.addAnimal(animal);
            return animal;
        }
        throw new IllegalAccessException("Unknown animal: " + animalName);
    }
}
