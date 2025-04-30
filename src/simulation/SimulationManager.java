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
import java.util.Optional;
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
    private final ScheduledExecutorService scheduler; //= Executors.newScheduledThreadPool(2); // 2 потока
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
        // Обрабатываем все ячейки
        for (Cell[] row : island.getCells()) {
            for (Cell cell : row) {
                // Для каждого животного в ячейке
                cell.getAnimals().forEach(animal -> {
                    animal.eat();
                    animal.move();
                    // Размножение
                    Optional<Livable> offspring = animal.getOffspring();
                    offspring.ifPresent(child -> {
                        child.setCurrentCell(cell);
                        cell.addAnimal(child);
                    });
                    // Проверка на смерть от голода
                    if (animal.getCurrentWeight() < animal.getData().getWeight() * 0.1) {
                        animal.die();
                    }
                });
            }
        }
    }

    public void stopSimulation() {
        isRunning = false;
        scheduler.shutdown();
    }

    public void initializeIsland() throws IllegalAccessException {
        // Создаем ячейки и связываем их с островом
        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                Cell cell = new Cell(x, y, island);
                island.setCell(x, y, cell);
            }
        }
        distributeEntities();
    }

    private void distributeEntities() {
        Random random = new Random();
        for (Cell[] row : island.getCells()) {
            for (Cell cell : row) {
                // Добавляем животных
                for (Data animalType : Data.values()) {
                    int maxPerCell = animalType.getMaxQuantity();
                    int count = random.nextInt(maxPerCell + 1); // Случайное количество
                    for (int i = 0; i < count; i++) {
                        try {
                            Livable animal = AnimalFactory.createAnimal(animalType);
                            animal.setCurrentCell(cell); // Важно!
                            cell.addAnimal(animal);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Добавляем растения
                int plantsCount = random.nextInt(200 + 1);
                for (int i = 0; i < plantsCount; i++) {
                    cell.addPlant(new Plant());
                }
            }
        }
    }

    public Island getIsland() {
        return island;
    }
}
