package plants;

import factory.Eatable;
import field.Cell;

public class Plant implements Eatable {
    public static final double WEIGHT = 1.0;
    private Cell currentCell;

    @Override
    public double getWeight() {
        return WEIGHT;
    }

    public void die() {
        currentCell.getPlants().remove(this);
    }
}
