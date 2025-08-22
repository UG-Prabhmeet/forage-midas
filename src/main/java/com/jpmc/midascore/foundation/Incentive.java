package com.jpmc.midascore.foundation;

public class Incentive {
    private float amount;

    public Incentive () {}

    public Incentive (float amount) {
        this.amount = amount;
    }

    // getter
    public float getAmount () {
        return amount;
    }

    // setter
    public void setAmount (float amount) {
        this.amount = amount;
    }
}
