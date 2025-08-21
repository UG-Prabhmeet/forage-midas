package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
// registers this class as spring bean (java object managed by spring) automatically

public class TransactionListener {

    private final TransactionProcessor processor;

    public TransactionListener (TransactionProcessor processor) {
        this.processor = processor;
    }
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "${spring.kafka.consumer.group-id}")
    // marks this function as a Kafka Consumer.
    // spring automatically subscribes it to the given topic.
    // msgs published to "tx" are routed here.

    public void fetchTransaction(Transaction tx) {
        // This method is triggered every time a new Transaction
        // message is consumed from Kafka.

        processor.process(tx);
        // System.out.println(">>> RECEIVED Transaction: " + tx);
        // System.out.println(">>> AMOUNT: " + tx.getAmount());
    }
}
