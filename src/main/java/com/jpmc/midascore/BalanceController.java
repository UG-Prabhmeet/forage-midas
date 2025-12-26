package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    private final DatabaseConduit databaseConduit;

    public BalanceController (DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @GetMapping ("/balance")
    /* Listens for GET requests at "http://localhost:8080/balance".
       @RequestParam long userId: Looks for "?userId=123" in the URL.
    */
    public Balance getBalance (@RequestParam long userId) {
        UserRecord user = databaseConduit.getUserById(userId);

        if(user == null) {
            return new Balance(0f);
        }

        return new Balance(user.getBalance());
    }
}
