package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
// interact with INCENTIVE API and
// fetch incentive (bonus) amounts for transactions
public class IncentiveService {

    // REST TEMPLATE - Spring HTTP client to make API calls
    private final RestTemplate restTemplate;
    private static final String INCENTIVE_URL = "http://localhost:8080/incentive";

    public IncentiveService (RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float fetchIncentive (Transaction tx) {

        // sending POST request to api
        Incentive incentive = restTemplate.postForObject(INCENTIVE_URL, tx, Incentive.class);

        if (incentive == null) {
            System.out.println("Warning: Incentive API returned null for transaction " + tx);
            return 0f;
        }

        return incentive.getAmount();
    }
}
