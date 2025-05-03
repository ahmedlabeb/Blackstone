package com.blackstone.customer.entity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customerId;


    @Column(name = "account_id")
    private String accountId;

    @Column(name = "type")
    private String type;
}
