import simulation.SimulationManager;
import view.ConsoleOutputManager;

public class IslandApp {
    private static final int width = 5;
    private static final int height = 5;

    public static void main(String[] args) throws IllegalAccessException {

        SimulationManager simulationManager = new SimulationManager(width, height);
        simulationManager.initializeIsland();
        simulationManager.startSimulation();

    }
}
