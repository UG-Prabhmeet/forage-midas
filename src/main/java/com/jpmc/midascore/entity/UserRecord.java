package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
// Tells the database: "Create a table for this class." Each object will be a row in that table.
public class UserRecord {

    @Id 
    @GeneratedValue() 
    private long id;  // id is primary key with auto increment

    @Column(nullable = false) 
    private String name; // name is not null
 
    @Column(nullable = false)
    private float balance; // balance is not null

    protected UserRecord() {
    }

    public UserRecord(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s', balance=%.2f]",
                id, name, balance
        );
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getBalance() {
        return balance;
    }

    // setter
    public void setBalance(float balance) {
        this.balance = balance;
    }
}
