
package com.blackstone.account.repository;


import com.blackstone.account.repository.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.status = 'ACTIVE'")
    Optional<Account> findActiveById(String accountId);

    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE'")
    List<Account> findAllActive();

    @Query("SELECT a FROM Account a WHERE a.customerInfo.customerId = :customerId")
    List<Account> findByCustomerId(String customerId);
}
