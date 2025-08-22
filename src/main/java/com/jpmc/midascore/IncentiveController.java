package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import org.springframework.web.bind.annotation.*;

@RestController
public class IncentiveController {

    @PostMapping("/incentive")
    public Incentive calculateIncentive(@RequestBody Transaction tx) {
        // Log the received transaction
        System.out.println("Received transaction: " + tx);

        // Example incentive calculation: 10% of transaction amount
        return new Incentive(tx.getAmount() * 0.1f);
    }
}
