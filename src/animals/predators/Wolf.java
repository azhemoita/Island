package animals.predators;

import factory.Livable;

public class Wolf implements Livable {
    public Wolf() {}

    @Override
    public void eat() {
        System.out.println("Wolf eating...");
    }

    @Override
    public void move() {
        System.out.println("Wolf moving...");
    }

    @Override
    public void die() {
        System.out.println("Wolf dying...");
    }
}
