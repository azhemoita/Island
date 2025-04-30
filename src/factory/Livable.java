package factory;

import data.Data;
import field.Cell;

import java.util.Optional;

public interface Livable {
    void eat();
    void move();
    void die();
    public Optional<Livable> getOffspring();

    Data getData(); // Для доступа к параметрам животного
    Cell getCurrentcell();
    void setCurrentCell(Cell cell);
    double getCurrentWeight();
    void setCurrentWeight(double currentWeight);
}
