package factory;

import field.Cell;

public interface Livable {
    void eat();
    void move();
    void die();

    void setCurrentCell(Cell cell);
}
