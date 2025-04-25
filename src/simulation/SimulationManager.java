package simulation;

import animals.herbivores.*;
import animals.predators.*;
import data.Data;
import factory.*;
import field.Cell;
import field.Island;
import plants.Plant;
import view.ConsoleOutputManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Класс для:
 * - инициализации острова и его компонентов
 * - управления тактами симуляции
 * - обработки условий завершения симуляции
 * - сбора и вывода статистики
 *
 */
public class SimulationManager {
    private final Island island;
    private final ScheduledExecutorService scheduler;

    public SimulationManager(int width, int height) {
        this.island = new Island(width, height);
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startSimulation() {
//        scheduler.scheduleAtFixedRate(this::startSimulation, 0, 1, TimeUnit.SECONDS);
        new ConsoleOutputManager(this.island).printIslandState();
    }

    private void runSimulationTick() {
        // логика одно такта симуляции
    }

    public void stopSimulation() {
        scheduler.shutdown();
    }

    public void initializeIsland() throws IllegalAccessException {
        distributeEntities();
    }

    private void distributeEntities() throws IllegalAccessException {
        Random random = new Random();
        for (Cell[] row : island.getCells()) {
            for (Cell cell : row) {

                for (Data animal : Data.values()) {
                    int i = random.nextInt(animal.getMaxQuantity() + 1);
                    for (int j = 0; j < i; j++) {
                        Livable newAnimal = AnimalFactory.createAnimal(animal);
                        cell.addAnimal(newAnimal);
                    }
                }

                for (int i = 0; i < random.nextInt(200 + 1); i++) {
                    Plant plant = new Plant();
                    cell.addPlant(plant);
                }
            }
        }
    }

    public Island getIsland() {
        return island;
    }
}
