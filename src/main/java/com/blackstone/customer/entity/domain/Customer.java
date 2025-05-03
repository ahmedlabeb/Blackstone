
package com.blackstone.customer.entity.domain;

import com.blackstone.customer.entity.dto.CustomerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_id", length = 7, nullable = false, unique = true)
    private String customerId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "legal_id", nullable = false)
    private String legalId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CustomerType type;
    @Column(name = "address", nullable = false)

    private String address;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountInfo> accountInfos;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalId() {
        return legalId;
    }

    public void setLegalId(String legalId) {
        this.legalId = legalId;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<AccountInfo> getAccountInfos() {
        return accountInfos;
    }

    public void setAccountInfos(Set<AccountInfo> accountInfos) {
        this.accountInfos = accountInfos;
    }
}
