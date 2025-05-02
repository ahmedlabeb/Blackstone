package com.blackstone.customer.repository;

import com.blackstone.customer.repository.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo,Long> {


    @Query("SELECT a FROM AccountInfo a WHERE a.accountId = :accountId")
    AccountInfo findByAccountId(String accountId);
}
