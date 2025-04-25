package factory;

import animals.herbivores.*;
import animals.predators.*;
import data.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AnimalFactory {
    private static final Map<Data, Supplier<Livable>> animalSuppliers = new HashMap<>();


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

    public static Livable createAnimal(Data animalName) throws IllegalAccessException {
        Supplier<Livable> supplier = animalSuppliers.get(animalName);
        if (supplier != null) {
            return supplier.get();
        }
        throw new IllegalAccessException("Unknown animal: " + animalName);
    }
}
