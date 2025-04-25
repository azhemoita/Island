package view;

import animals.herbivores.*;
import animals.predators.*;
import data.Data;
import field.Cell;
import field.Island;

import java.util.ArrayList;
import java.util.List;

public class ConsoleOutputManager {
    private final Island island;

    public ConsoleOutputManager(Island island) {
        this.island = island;
    }

    public void printIslandState() {
        Cell[][] cells = island.getCells();
        int width = island.getWidth();
        int height = island.getHeight();
        long countOfDucks = 0;
        long countOfWolves = 0;
        long countOfBears = 0;
        long countOfSnakes = 0;
        long countOfCaterpillars = 0;
        long countOfHorses = 0;
        long countOfDeers = 0;
        long countOfBuffalo = 0;
        long countOfHares = 0;
        long countOfBoars = 0;
        long countOfHamsters = 0;
        long countOfSheep = 0;
        long countOfGoats = 0;
        long countOfEagles = 0;
        long countOfFoxes = 0;
        int countOfPlants = 0;

        for (int x = 0; x < width; x++) {

            List<StringBuilder> rowLines = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                rowLines.add(new StringBuilder());
            }

            for (int y = 0; y < height; y++) {
                Cell cell = cells[x][y];

                long ducks = cell.getAnimals().stream().filter(a -> a instanceof Duck).count();
                countOfDucks += ducks;
                long wolves = cell.getAnimals().stream().filter(a -> a instanceof Wolf).count();
                countOfWolves += wolves;
                int plants = cell.getPlants().size();
                countOfPlants += plants;
                long caterpillar = cell.getAnimals().stream().filter(a -> a instanceof Caterpillar).count();
                countOfCaterpillars += caterpillar;
                long horse = cell.getAnimals().stream().filter(a -> a instanceof Horse).count();
                countOfHorses += horse;
                long deer = cell.getAnimals().stream().filter(a -> a instanceof Deer).count();
                countOfDeers += deer;
                long buffalo = cell.getAnimals().stream().filter(livable -> livable instanceof Buffalo).count();
                countOfBuffalo += buffalo;
                long hare = cell.getAnimals().stream().filter(livable -> livable instanceof Hare).count();
                countOfHares += hare;
                long boar = cell.getAnimals().stream().filter(livable -> livable instanceof Boar).count();
                countOfBoars += boar;
                long hamster = cell.getAnimals().stream().filter(livable -> livable instanceof Hamster).count();
                countOfHamsters += hamster;
                long sheep = cell.getAnimals().stream().filter(livable -> livable instanceof Sheep).count();
                countOfSheep += sheep;
                long goat = cell.getAnimals().stream().filter(livable -> livable instanceof Goat).count();
                countOfGoats += goat;
                long snake = cell.getAnimals().stream().filter(livable -> livable instanceof Snake).count();
                countOfSnakes += snake;
                long eagle = cell.getAnimals().stream().filter(livable -> livable instanceof Eagle).count();
                countOfEagles += eagle;
                long fox = cell.getAnimals().stream().filter(livable -> livable instanceof Fox).count();
                countOfFoxes += fox;
                long bear = cell.getAnimals().stream().filter(livable -> livable instanceof Bear).count();
                countOfBears += bear;

                rowLines.get(0).append(String.format("(%2d,%2d)+---------------------------------+ ", x, y));
                rowLines.get(1).append(String.format("       | %s:%03d  %s:%03d  %s:%03d  %s:%03d | ",
                        Data.DUCK.getIcon(), ducks,
                        Data.WOLF.getIcon(), wolves,
                        Data.BEAR.getIcon(), bear,
                        Data.SNAKE.getIcon(), snake));
                rowLines.get(2).append(String.format("       | 🌱:%03d  %s:%03d  %s:%03d  %s:%03d | ",
                        plants,
                        Data.CATERPILLAR.getIcon(), caterpillar,
                        Data.HORSE.getIcon(), horse,
                        Data.DEER.getIcon(), deer));
                rowLines.get(3).append(String.format("       | %s:%03d  %s:%03d  %s:%03d  %s:%03d | ",
                        Data.BUFFALO.getIcon(), buffalo,
                        Data.HARE.getIcon(), hare,
                        Data.BOAR.getIcon(), boar,
                        Data.HAMSTER.getIcon(), hamster));
                rowLines.get(4).append(String.format("       | %s:%03d  %s:%03d  %s:%03d  %s:%03d | ",
                        Data.SHEEP.getIcon(), sheep,
                        Data.GOAT.getIcon(), goat,
                        Data.EAGLE.getIcon(), eagle,
                        Data.FOX.getIcon(), fox));
                rowLines.get(5).append("      +----------------------------------+ ");


            }
            for (StringBuilder line : rowLines) {
                System.out.println(line);
            }

            System.out.println();
        }

        System.out.printf("Всего на острове: %n%s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | %s: %d | 🌱: " + countOfPlants + "\n\n\n",
                Data.DUCK.getIcon(), countOfDucks,
                Data.WOLF.getIcon(), countOfWolves,
                Data.BEAR.getIcon(), countOfBears,
                Data.SNAKE.getIcon(), countOfSnakes,
                Data.CATERPILLAR.getIcon(), countOfCaterpillars,
                Data.HORSE.getIcon(), countOfHorses,
                Data.DEER.getIcon(), countOfDeers,
                Data.BUFFALO.getIcon(), countOfBuffalo,
                Data.HARE.getIcon(), countOfHares,
                Data.BOAR.getIcon(), countOfBoars,
                Data.HAMSTER.getIcon(), countOfHamsters,
                Data.SHEEP.getIcon(), countOfSheep,
                Data.GOAT.getIcon(), countOfGoats,
                Data.EAGLE.getIcon(), countOfEagles,
                Data.FOX.getIcon(), countOfFoxes);

    }
}
