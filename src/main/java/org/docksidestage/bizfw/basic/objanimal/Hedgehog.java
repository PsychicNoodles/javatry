package org.docksidestage.bizfw.basic.objanimal;

public class Hedgehog extends Animal {
    public Hedgehog() {}

    public Hedgehog(String name) { super(name); }

    @Override
    protected String getBarkWord() {
        return "squeak";
    }
}
