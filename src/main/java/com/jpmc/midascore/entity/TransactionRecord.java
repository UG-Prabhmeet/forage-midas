package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne (optional = false)
    @JoinColumn (name = "sender_id")
    private UserRecord sender;

    @ManyToOne (optional = false)
    @JoinColumn (name = "recipient_id")
    private UserRecord recipient;

    @Column (nullable = false)
    private float amount;

    @Column (nullable = false)
    private Instant createdAt = Instant.now();

    protected TransactionRecord() {}

    public TransactionRecord (UserRecord sender , UserRecord recipient , float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction[id=%d, sender='%s', recipient='%s', amount=%.2f, createdAt=%s]",
                id,
                sender != null ? sender.getName() : "null",
                recipient != null ? recipient.getName() : "null",
                amount,
                createdAt
        );
    }

    // getters
    public Long getId() {
        return id;
    }

    public UserRecord getSender () {
        return sender;
    }

    public UserRecord getRecipient () {
        return recipient;
    }

    public float getAmount () {
        return amount;
    }

    public Instant getCreatedAt () {
        return createdAt;
    }
}
