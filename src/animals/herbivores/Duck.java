package animals.herbivores;

import factory.Livable;

public class Duck implements Livable {
    int currentQuantity; // - текущее количество данного типа животного в данной координате
    int[][] currentCoordinate;
    int satiety = 5;

    public Duck() {}

//    public Duck(int[][] currentCoordinate) {
//        this.currentCoordinate = currentCoordinate;
//    }

    @Override
    public void eat() {
        System.out.println("Duck eating...");
        /**
         * ThreadLocalRandom. Если в currentCoordinate есть гусеница или растение, то едим.
         * Гусеницу - 90%, Растение - 100%.
         * */
    }

    @Override
    public void move() {
        System.out.println("Duck moving...");
        /**
         * ThreadLocalRandom. Генерим случайное число от 0 до maxSpeed.
         * Задаём направление движения. Выполняем проверку границ. Меняем координаты.
         */
    }

//    @Override
//    public void reproduce() {
//        System.out.println("Duck reproducing...");
        /**
         * ThreadLocalRandom. Проверяем currentQuantity. Если < maxQuantity,
         * то создаём новую утку.
         * */
//    }

    @Override
    public void die() {
        System.out.println("Duck dying...");
        /**
         * Если нас съели или satiety < 0, то мы умираем.
         * currentQuantity -= 1;
         * */
    }

}
