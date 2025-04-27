package factory;

import data.Data;
import field.Cell;
import field.Island;

public abstract class Animal {
    protected Cell currentCell;
    protected Island island;

    public abstract Livable reproduce(Data animalName);
}
