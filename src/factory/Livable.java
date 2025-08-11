package factory;

import data.Data;
import field.Cell;

import java.util.Optional;

public interface Livable extends  Eatable {
    void eat();
    void move();
    void die();
    Optional<Livable> getOffspring();

    Data getData(); // Для доступа к параметрам животного
    Cell getCurrentCell();
    void setCurrentCell(Cell cell);
    double getCurrentWeight();
    void setCurrentWeight(double currentWeight);
}
