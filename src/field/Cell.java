package field;

import factory.Livable;
import plants.Plant;

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

    public void addAnimal(Livable animal) {
        long countOfSameType = animals.stream().filter(a -> a.getData() == animal.getData()).count();

        if (countOfSameType < animal.getData().getMaxQuantity()) {
            animals.add(animal);
            animal.setCurrentCell(this); // Устанавливаем ссылку на ячейку у животного
        } else {
            System.out.println("Cannot add " + animal.getData() + ": max quantity reached");
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

    /**
     * TODO:
     * Ячейка содержит:
     * 1. Отображение текущего состояния игры. Должна выводиться статистика теущего состояния.
     *
     */
}
