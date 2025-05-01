package simulation;

import animals.herbivores.*;
import animals.predators.*;
import data.Data;
import factory.*;
import field.Cell;
import field.Island;
import plants.Plant;
import view.ConsoleOutputManager;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
        print.printIslandState();
    }

    private void runSimulationTick() {
        if (!isRunning) return;
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
        // Условный вывод + проверка на deadlock
        int day = SimulationManager.dayCounter.incrementAndGet();
        if (day % 5 == 0 || (day % 10 == 0 && checkForDeadlock())) {
            printIslandStateWithLocks();
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
//        // Создаем ячейки и связываем их с островом
//        for (int x = 0; x < island.getWidth(); x++) {
//            for (int y = 0; y < island.getHeight(); y++) {
//                Cell cell = new Cell(x, y, island);
//                island.setCell(x, y, cell);
//            }
//        }
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

    public void printIslandStateWithLocks() {
        System.out.println("\n=== Детальное состояние острова (потокобезопасное) ===");
        System.out.printf("День: %d | Размер: %dx%d\n",
                SimulationManager.dayCounter.get(),
                island.getWidth(),
                island.getHeight());

        // Собираем и сортируем ячейки для предотвращения deadlock
        List<Cell> allCells = new ArrayList<>();
        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                allCells.add(island.getCell(x, y));
            }
        }

        // Сортируем по координатам (x+y) для упорядоченной блокировки
        allCells.sort(Comparator.comparingInt(c -> c.getCoordinate().getX() + c.getCoordinate().getY()));

        // Статистика по всему острову
        Map<Data, Integer> globalStats = new EnumMap<>(Data.class);
        int totalPlants = 0;

        for (Cell cell : allCells) {
            synchronized (cell) {
                // Локальная статистика для ячейки
                Map<Data, Integer> cellStats = new EnumMap<>(Data.class);

                // Подсчет животных по типам
                for (Livable animal : cell.getAnimals()) {
                    Data type = animal.getData();
                    cellStats.merge(type, 1, Integer::sum);
                    globalStats.merge(type, 1, Integer::sum);
                }

                // Подсчет растений
                int plantsInCell = cell.getPlants().size();
                totalPlants += plantsInCell;

                // Вывод информации о ячейке
                if (!cellStats.isEmpty() || plantsInCell > 0) {
                    System.out.printf("\nЯчейка [%2d][%2d]: ",
                            cell.getCoordinate().getX(),
                            cell.getCoordinate().getY());

                    // Вывод животных (максимум 5 самых многочисленных)
                    cellStats.entrySet().stream()
                            .sorted(Map.Entry.<Data, Integer>comparingByValue().reversed())
                            .limit(5)
                            .forEach(e -> System.out.printf("%s=%d ", e.getKey().getIcon(), e.getValue()));

                    // Вывод растений
                    if (plantsInCell > 0) {
                        System.out.printf("| Растений: %d", plantsInCell);
                    }
                }
            }
        }

        // Вывод глобальной статистики
        System.out.println("\n\n=== Общая статистика ===");
        globalStats.entrySet().stream()
                .sorted(Map.Entry.<Data, Integer>comparingByValue().reversed())
                .forEach(e -> System.out.printf("%s: %d | ", e.getKey().getIcon(), e.getValue()));

        System.out.printf("\nВсего растений: %d\n", totalPlants);
        System.out.println("=== Конец отчета ===\n");
    }
}
