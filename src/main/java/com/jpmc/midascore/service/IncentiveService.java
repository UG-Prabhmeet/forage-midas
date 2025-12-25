package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
// This service interacts with the external Incentive API
// It sends transaction data and receives incentive (bonus) amounts in return
public class IncentiveService {

    // RestTemplate is a Spring-provided HTTP client used to make REST API calls
    private final RestTemplate restTemplate;

    private static final String INCENTIVE_URL = "http://localhost:8080/incentive";

    public IncentiveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float fetchIncentive(Transaction tx) {
        // Sending a POST request with the Transaction object
        // The API responds with an Incentive object containing the incentive amount
        Incentive incentive = restTemplate.postForObject(INCENTIVE_URL, tx, Incentive.class);

        // If the API didn’t return a valid response, print a warning and return 0 as default incentive
        if (incentive == null) {
            System.out.println("Warning: Incentive API returned null for transaction " + tx);
            return 0f;
        }

        // Returning the incentive amount from the response
        return incentive.getAmount();
    }
}
