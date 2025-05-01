import simulation.SimulationManager;
import view.ConsoleOutputManager;

public class IslandApp {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;

    public static void main(String[] args) throws IllegalAccessException {

        SimulationManager simulationManager = new SimulationManager(WIDTH, HEIGHT); // Создаём симуляцию.
        simulationManager.initializeIsland(); // Инициализируем остров заданных размеров, заселяя его случайным количеством животных от 0 до максимально допустимого.
        simulationManager.startSimulation(); // Запускаем симуляцию жизни на острове.

    }
}
