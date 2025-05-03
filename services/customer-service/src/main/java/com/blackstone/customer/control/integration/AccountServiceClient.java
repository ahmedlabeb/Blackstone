package com.blackstone.customer.control.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "account-service", url = "${services.account.base-url}")

public interface AccountServiceClient {

    @GetMapping("/api/accounts/check-balance/{customerId}")
    Boolean hasNonZeroBalance(@PathVariable("customerId") String customerId);

}
