package com.jpmc.midascore;

import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRepo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepo transactionRepo;

    @Test
    void task_three_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // wait for processing
        Thread.sleep(2000);

        // log all users
        logger.info("========== USERS ==========");
        userRepository.findAll().forEach(user -> logger.info(user.toString()));

        // log all transactions
        logger.info("========== TRANSACTIONS ==========");
        transactionRepo.findAll().forEach(tx -> logger.info(tx.toString()));

        logger.info("==================================");
    }
}
