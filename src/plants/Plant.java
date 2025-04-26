package plants;

import field.Cell;

public class Plant {
    public static final double WEIGHT = 1.0;
    private Cell currentCell;

    public void grow() {

    }

    public void die() {
        currentCell.getPlants().remove(this);
    }
}
