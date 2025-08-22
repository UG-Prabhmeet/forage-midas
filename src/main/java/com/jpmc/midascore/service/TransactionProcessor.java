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

    private static final Logger log = LoggerFactory.getLogger(TransactionProcessor.class);

    private final DatabaseConduit databaseConduit;
    private final IncentiveService incentiveService;

    public TransactionProcessor (DatabaseConduit databaseConduit, IncentiveService incentiveService) {
        this.databaseConduit = databaseConduit;
        this.incentiveService = incentiveService;
    }

    @Transactional // ensures atomicity
    public void process (Transaction tx) {
        UserRecord sender = databaseConduit.getUserById(tx.getSenderId());
        UserRecord recipient = databaseConduit.getUserById(tx.getRecipientId());

        if(sender == null || recipient == null) return;
        if(sender.getBalance() < tx.getAmount()) return;

        // otherwise we've valid records (so, updating balances)
        sender.setBalance(sender.getBalance() - tx.getAmount());

        // calling incentive api to find if any bonus (incentive) is there for this transaction
        float incentiveAmt = incentiveService.fetchIncentive(tx);

        recipient.setBalance(recipient.getBalance() + tx.getAmount() + incentiveAmt);

        // saving transaction records in new Class and TransactionRepo
        TransactionRecord newTx = new TransactionRecord(sender, recipient, tx.getAmount(), incentiveAmt);
        databaseConduit.saveTransaction(newTx);

        // storing users with updated balances in userRepo
        databaseConduit.save(sender);
        databaseConduit.save(recipient);

        // printing balances of all users in console log
        log.info("=== Balances after transaction ({} → {}, amount={}, incentive={}) ===",
                sender.getName(), recipient.getName(), tx.getAmount(), incentiveAmt);

        databaseConduit.getAllUsers().forEach(user ->
                log.info("   {} → {}", user.getName(), user.getBalance())
        );
    }
}
