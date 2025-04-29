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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс для:
 * - инициализации острова и его компонентов
 * - управления тактами симуляции
 * - обработки условий завершения симуляции
 * - сбора и вывода статистики
 *
 */
public class SimulationManager {
    static volatile boolean isRunning = false;
    private final Island island;
    private final ScheduledExecutorService scheduler;
    private ConsoleOutputManager print;
    int countOfThreads = Runtime.getRuntime().availableProcessors();
    public static AtomicInteger dayCounter = new AtomicInteger(0);

    public SimulationManager(int width, int height) {
        this.island = new Island(width, height);
        this.scheduler = Executors.newScheduledThreadPool(countOfThreads);
        this.print = new ConsoleOutputManager(this.island);
    }

    public void startSimulation() {
        if (isRunning) return;

        isRunning = true;

        scheduler.scheduleAtFixedRate(this::runSimulationTick, 1, 2, TimeUnit.SECONDS);
        print.printIslandState();
    }

    private void runSimulationTick() {
        for (Cell[] row : island.getCells()) {
            for (Cell cell : row) {
                cell.getAnimals().forEach(Livable::move);
            }
        }
        print.printIslandState();
    }

    public void stopSimulation() {
        isRunning = false;
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
                        Livable newAnimal = AnimalFactory.createAnimal(animal, cell);
                        cell.addAnimal(newAnimal);
                    }
                }

                for (int i = 0; i < random.nextInt( 200 + 1); i++) {
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
