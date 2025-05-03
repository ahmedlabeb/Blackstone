
package com.blackstone.customer.entity.repository;


import com.blackstone.customer.entity.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId AND c.deleted = false")
    Optional<Customer> findActiveById(@Param("customerId") String id);

    @Query("SELECT c FROM Customer c WHERE c.deleted = false")
    List<Customer> findAllActive();

}
