package field;

import factory.Livable;
import plants.Plant;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cell {
    private Island island;
    private Coordinate coordinate;
    private List<Livable> animals;
    private List<Plant> plants;

    public Cell(int x, int y) {
        this.coordinate = new Coordinate(x, y);
        this.animals = new CopyOnWriteArrayList<>();
        this.plants = new CopyOnWriteArrayList<>();
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
            animal.setCurrentCell(this);
            animals.add(animal);
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
