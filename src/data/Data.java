package data;

public enum Data {
    DUCK("🦆", 1.0, 200, 4, 0.15),
    CATERPILLAR("🐛", 0.01, 1000, 0, 0),
    HORSE("🐎", 400, 20, 4, 60),
    DEER("🦌", 300, 20, 4, 50),
    BUFFALO("🐃", 700, 10, 3, 100),
    HARE("🐇", 2, 150, 2, 0.45),
    BOAR("🐗", 400, 50, 2, 50),
    HAMSTER("🐁", 0.05, 500, 1, 0.01),
    SHEEP("🐑", 70, 140, 3, 15),
    GOAT("🐐", 60, 140, 3, 10),
    WOLF("🐺", 50.0, 30, 3, 8.0),
    SNAKE("🐍", 15, 30, 1, 3),
    EAGLE("🦅", 6, 20, 3, 1),
    FOX("🦊", 8, 30, 2, 2),
    BEAR("🐻", 500, 5, 2, 80);

    private final String icon;
    private final double weight;
    private final int maxQuantity;
    private final int maxSpeed;
    private final double countOfFood;

    Data(String icon, double weight, int maxQuantity, int maxSpeed, double countOfFood) {
        this.icon = icon;
        this.weight = weight;
        this.maxQuantity = maxQuantity;
        this.maxSpeed = maxSpeed;
        this.countOfFood = countOfFood;
    }

    public String getIcon() {
        return icon;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public double getCountOfFood() {
        return countOfFood;
    }
}
