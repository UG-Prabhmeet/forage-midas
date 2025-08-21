package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepo;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionProcessor {

    private final UserRepository userRepository;
    private final TransactionRepo transactionRepo;

    public TransactionProcessor (UserRepository userRepository , TransactionRepo transactionRepo) {
        this.userRepository = userRepository;
        this.transactionRepo = transactionRepo;
    }

    @Transactional
    public void process (Transaction tx) {
        UserRecord sender = userRepository.findById(tx.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(tx.getRecipientId()).orElse(null);

        if(sender == null || recipient == null) return;
        if(sender.getBalance() < tx.getAmount()) return;

        // otherwise we've valid records (so, updating balances)
        sender.setBalance(sender.getBalance() - tx.getAmount());

        recipient.setBalance(recipient.getBalance() + tx.getAmount());

        // saving transaction records in new Class and TransactionRepo
        TransactionRecord newTx = new TransactionRecord(sender, recipient, tx.getAmount());
        transactionRepo.save(newTx);

        // storing users with updated balances in userRepo
        userRepository.save(sender);
        userRepository.save(recipient);
    }
}
