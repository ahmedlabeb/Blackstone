package com.blackstone.account.entity.repository;

import com.blackstone.account.entity.domain.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {

    @Query("SELECT c from CustomerInfo c where c.customerId = :customerId")
    CustomerInfo findCustomerById(String customerId);
}
