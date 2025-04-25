package field;

import factory.Livable;
import plants.Plant;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Coordinate coordinate;
    private List<Livable> animals;
    private List<Plant> plants;

    public Cell(int x, int y) {
        this.coordinate = new Coordinate(x, y);
        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void addAnimal(Livable animal) {
        if (animal != null) {
            animals.add(animal);
        }
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
     * 1. Координаты.
     * 2. Животных. Должно меняться количество животных в ячейке.
     * 3. Отображение текущего состояния игры. Должна выводиться статистика теущего состояния.
     *
     */
}
