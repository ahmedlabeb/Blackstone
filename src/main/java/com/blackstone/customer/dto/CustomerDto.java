
package com.blackstone.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @Pattern(regexp = "\\d{7}", message = "Customer ID must be 7 digits")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String customerId;

    @NotBlank(message = "Customer name is required")
    private String name;
    @NotBlank(message = "Legal ID is required")
    private String legalId;
    @NotBlank(message = "Customer address is required")
    private String address;

    @NotNull(message = "Customer type is required")
    private CustomerType type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean deleted;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
