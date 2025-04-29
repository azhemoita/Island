import simulation.SimulationManager;
import view.ConsoleOutputManager;

public class IslandApp {
    private static final int width = 5;
    private static final int height = 5;

    public static void main(String[] args) throws IllegalAccessException {

        SimulationManager simulationManager = new SimulationManager(width, height); // Создаём симуляцию.
        simulationManager.initializeIsland(); // Инициализируем остров заданных размеров, заселяя его случайным количеством животных от 0 до максимально допустимого.
        simulationManager.startSimulation(); // Запускаем симуляцию жизни на острове.

    }
}
