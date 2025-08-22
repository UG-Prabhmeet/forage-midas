package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRepo;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component

// database conduit - pipeline / bridge b/w our code (transaction processor and database)
// it's a class carrying user & transaction data to/from the database

public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepo transactionRepo;

    public DatabaseConduit(UserRepository userRepository, TransactionRepo transactionRepo) {
        this.userRepository = userRepository;
        this.transactionRepo = transactionRepo;
    }

    public void save(UserRecord user) {
        userRepository.save(user);
    }

    public void saveTransaction (TransactionRecord txRecord) {
        transactionRepo.save(txRecord);
    }

    public UserRecord getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserRecord> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }
}
