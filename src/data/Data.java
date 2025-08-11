package data;

import animals.herbivores.*;
import animals.predators.*;
import factory.Eatable;
import plants.Plant;

import java.util.Map;

public enum Data {
    DUCK("🦆", 1.0, 200, 4, 0.15, Map.of(Caterpillar.class, 90, Plant.class, 100)),
    CATERPILLAR("🐛", 0.01, 1000, 0, 0, Map.of(Plant.class, 100)),
    HORSE("🐎", 400, 20, 4, 60, Map.of(Plant.class, 100)),
    DEER("🦌", 300, 20, 4, 50, Map.of(Plant.class, 100)),
    BUFFALO("🐃", 700, 10, 3, 100, Map.of(Plant.class, 100)),
    HARE("🐇", 2, 150, 2, 0.45, Map.of(Plant.class, 100)),
    BOAR("🐗", 400, 50, 2, 50, Map.of(Hamster.class, 50, Caterpillar.class, 90, Plant.class, 100)),
    HAMSTER("🐁", 0.05, 500, 1, 0.01, Map.of(Caterpillar.class, 90, Plant.class, 100)),
    SHEEP("🐑", 70, 140, 3, 15, Map.of(Plant.class, 100)),
    GOAT("🐐", 60, 140, 3, 10, Map.of(Plant.class, 100)),
    WOLF("🐺", 50.0, 30, 3, 8.0,  Map.of(Horse.class, 10, Deer.class, 15, Hare.class, 60, Hamster.class, 80, Goat.class, 60, Sheep.class, 70, Boar.class, 15, Buffalo.class, 15, Duck.class, 40)),
    SNAKE("🐍", 15, 30, 1, 3, Map.of(Fox.class, 15, Hare.class, 20, Hamster.class, 40, Duck.class, 10)),
    EAGLE("🦅", 6, 20, 3, 1, Map.of(Fox.class, 10, Hare.class, 90, Hamster.class, 90, Duck.class, 80)),
    FOX("🦊", 8, 30, 2, 2, Map.of(Hare.class, 70, Hamster.class, 90, Duck.class, 60, Caterpillar.class, 40)),
    BEAR("🐻", 500, 5, 2, 80, Map.of(Snake.class, 80, Horse.class, 40, Deer.class, 80, Hare.class, 80, Hamster.class, 90, Goat.class, 70, Sheep.class, 70, Boar.class, 50, Buffalo.class, 50, Duck.class, 10)),;

    private final String icon;
    private final double weight;
    private final int maxQuantity;
    private final int maxSpeed;
    private final Map<Class<? extends Eatable>, Integer> diet;

    Data(String icon, double weight, int maxQuantity, int maxSpeed, double countOfFood, Map<Class<? extends Eatable>, Integer> diet) {
        this.icon = icon;
        this.weight = weight;
        this.maxQuantity = maxQuantity;
        this.maxSpeed = maxSpeed;
        this.diet = diet;
    }

    public String getIcon() {
        return icon;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public Map<Class<? extends Eatable>, Integer> getDiet() {
        return diet;
    }
}
