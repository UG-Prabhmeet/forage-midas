package com.jpmc.midascore.service;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
public class TransactionProcessor {

    // Logger to print messages and keep track of what’s happening during processing
    private static final Logger log = LoggerFactory.getLogger(TransactionProcessor.class);

    // DatabaseConduit is our helper class to interact with the database (fetching & saving data)
    private final DatabaseConduit databaseConduit;

    // IncentiveService handles calls to the external Incentive API
    private final IncentiveService incentiveService;

    // Constructor – Spring automatically injects the required dependencies
    public TransactionProcessor(DatabaseConduit databaseConduit, IncentiveService incentiveService) {
        this.databaseConduit = databaseConduit;
        this.incentiveService = incentiveService;
    }

    // @Transactional makes sure all DB operations inside this method are done as a single unit
    // (if something fails midway, everything is rolled back)
    @Transactional
    public void process(Transaction tx) {
        // Fetching sender and recipient details using their IDs
        UserRecord sender = databaseConduit.getUserById(tx.getSenderId());
        UserRecord recipient = databaseConduit.getUserById(tx.getRecipientId());

        // Checking if sender or recipient doesn’t exist – if so, we skip the transaction
        if (sender == null || recipient == null) return;

        // Checking if the sender has enough balance to perform the transaction
        if (sender.getBalance() < tx.getAmount()) return;

        // Deducting the transaction amount from the sender’s balance
        sender.setBalance(sender.getBalance() - tx.getAmount());

        // Calling the Incentive API to see if any reward (incentive) applies to this transaction
        float incentiveAmt = incentiveService.fetchIncentive(tx);

        // Adding both the transaction amount and any incentive to the recipient’s balance
        recipient.setBalance(recipient.getBalance() + tx.getAmount() + incentiveAmt);

        // Creating a new TransactionRecord object to store transaction details in the database
        TransactionRecord newTx = new TransactionRecord(sender, recipient, tx.getAmount(), incentiveAmt);
        databaseConduit.saveTransaction(newTx);  // saving the transaction record

        // Updating both user records in the database with their new balances
        databaseConduit.save(sender);
        databaseConduit.save(recipient);

        // Printing transaction details and updated balances to the console for debugging/logging
        log.info("=== Balances after transaction ({} → {}, amount={}, incentive={}) ===",
                sender.getName(), recipient.getName(), tx.getAmount(), incentiveAmt);

        // Looping through all users to display their updated balances
        databaseConduit.getAllUsers().forEach(user ->
                log.info("   {} → {}", user.getName(), user.getBalance())
        );
    }
}
