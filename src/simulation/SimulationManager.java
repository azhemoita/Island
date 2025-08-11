package simulation;

import data.Data;
import factory.*;
import field.Cell;
import field.Island;
import plants.Plant;
import view.ConsoleOutputManager;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager {
    static volatile boolean isRunning = false;
    private final Island island;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2); // 2 потока
    private ConsoleOutputManager print;
    int countOfThreads = Runtime.getRuntime().availableProcessors(); // Количество реально доступных потоков для программы
    public static AtomicInteger dayCounter = new AtomicInteger(0);

    public SimulationManager(int width, int height) {
        this.island = new Island(width, height);
        this.print = new ConsoleOutputManager(this.island);
    }

    public void startSimulation() {
        if (isRunning) return;
        isRunning = true;

        scheduler.scheduleAtFixedRate(this::runSimulationTick, 1, 2, TimeUnit.SECONDS);
        scheduler.schedule(() -> {
            System.out.println("5 минут прошло - завершаю симуляцию.");
            stopSimulation();
            print.printIslandState();
        }, 5, TimeUnit.MINUTES);
        print.printIslandState();
    }

    private void runSimulationTick() {
        if (!isRunning) return;
        // Обрабатываем все ячейки
        for (Cell[] row : island.getCells()) {
            for (Cell cell : row) {
                // Для каждого животного в ячейке
                cell.getAnimals().forEach(animal -> {
                    double metabolismLoss = animal.getData().getWeight() * 0.05;
                    animal.setCurrentWeight(animal.getCurrentWeight() - metabolismLoss);

                    double maxWeight = animal.getData().getWeight();
                    if (animal.getCurrentWeight() > maxWeight) {
                        animal.setCurrentWeight(maxWeight);
                    }

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

                int growth = ThreadLocalRandom.current().nextInt(100, 150);
                for (int i = 0; i < growth; i++) {
                    cell.addPlant(new Plant());
                }
            }
        }
        // Условный вывод + проверка на deadlock
        int day = SimulationManager.dayCounter.incrementAndGet();
        if (day % 5 == 0 || (day % 10 == 0 && checkForDeadlock())) {
//            printIslandStateWithLocks();
            print.printIslandState();
        }
    }

    public long getTotalAnimals() {
        return Arrays.stream(island.getCells())
                .parallel()  // Параллельная обработка для больших островов
                .flatMap(Arrays::stream)
                .mapToLong(cell -> {
                    synchronized (cell) {
                        return cell.getAnimals().size();
                    }
                })
                .sum();
    }

    private boolean checkForDeadlock() {
        // Простая проверка - если количество животных не меняется
        long prevCount = getTotalAnimals();
        try {
            Thread.sleep(100); // Даем время для изменений
        } catch (InterruptedException ignored) {}
        return prevCount == getTotalAnimals();
    }

    public void stopSimulation() {
        isRunning = false;
        scheduler.shutdown();
    }

    public void initializeIsland() throws IllegalAccessException {
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
}
