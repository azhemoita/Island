package field;

import factory.Livable;
import plants.Plant;
import util.Console;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cell {
    private Island island;
    private Coordinate coordinate;
    private final List<Livable> animals = new CopyOnWriteArrayList<>();
    private final List<Plant> plants = new CopyOnWriteArrayList<>();

    public Cell(int x, int y, Island island) {
        this.coordinate = new Coordinate(x, y);
        this.island = island;
    }

    public Island getIsland() {
        return island;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public synchronized void addAnimal(Livable animal) {
        if (animals.stream().filter(a -> a.getClass() == animal.getClass()).count() < animal.getData().getMaxQuantity()) {
            animals.add(animal);
            animal.setCurrentCell(this);
        } else {
            Console.log("Невозможно добавить животное " + animal.getData() + ". В клетке их уже максимальноле количество.");
        }
    }

    public void removeAnimal(Livable animal) {
        animals.remove(animal);
    }

    public void addPlant(Plant plant) {
        if (plant != null) {
            plants.add(plant);
        }
    }

    public List<Livable> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }
}
