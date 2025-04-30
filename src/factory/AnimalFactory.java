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
    private static final Map<Data, Supplier<Livable>> animalSuppliers = new HashMap<>();
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

    public static Livable createAnimal(Data animalType) {
        Livable animal = switch (animalType) {
            case BOAR -> new Boar();
            case BUFFALO -> new Buffalo();
            case CATERPILLAR -> new Caterpillar();
            case DEER -> new Deer();
            case DUCK -> new Duck();
            case GOAT -> new Goat();
            case HAMSTER -> new Hamster();
            case HARE -> new Hare();
            case HORSE -> new Horse();
            case SHEEP -> new Sheep();
            case BEAR -> new Bear();
            case EAGLE -> new Eagle();
            case FOX -> new Fox();
            case SNAKE -> new Snake();
            case WOLF -> new Wolf();
            default -> throw new IllegalArgumentException("Unknown animal: " + animalType);
        };
        // Инициализация текущего веса
//        animal.getCurrentWeight().set(animalType.getWeight());
        animal.setCurrentWeight(animal.getCurrentWeight());
        return animal;
    }
}
