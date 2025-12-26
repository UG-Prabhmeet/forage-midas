package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRepo;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component 

// database conduit - acts as a bridge between our business logic and the database repositories

public class DatabaseConduit {

    private final UserRepository userRepository;
    private final TransactionRepo transactionRepo;

    public DatabaseConduit(UserRepository userRepository, TransactionRepo transactionRepo) {
        this.userRepository = userRepository;
        this.transactionRepo = transactionRepo;
    }

    // Saves or updates a user record in the database
    public void save(UserRecord user) {
        userRepository.save(user);
    }

    // Saves a new transaction record in the database
    public void saveTransaction(TransactionRecord txRecord) {
        transactionRepo.save(txRecord);
    }

    // Retrieves a user by their unique ID
    // If the user doesn’t exist, returns null instead of throwing an exception
    public UserRecord getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Fetches all users from the database and returns them as a list
    // StreamSupport is used here to easily convert Iterable to a List
    public List<UserRecord> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }
}
