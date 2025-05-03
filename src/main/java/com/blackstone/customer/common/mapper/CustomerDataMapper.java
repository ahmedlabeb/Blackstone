package com.blackstone.customer.common.mapper;

import com.blackstone.customer.entity.dto.CustomerDto;
import com.blackstone.customer.entity.domain.Customer;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CustomerDataMapper {

    CustomerDto toDTO(Customer customer);

    @Mapping(target = "customerId", expression = "java(generateCustomerId())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "accountInfos", ignore = true)
    Customer toEntity(CustomerDto customerDto);

    List<CustomerDto> toDtoList(List<Customer> customers);

    List<Customer> toEntityList(List<CustomerDto> customerDtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "dto.customerId", ignore = true),
            @Mapping(target = "dto.deleted",ignore = true)
    })
    void updateCustomerFromDto(CustomerDto dto, @MappingTarget Customer entity);

     default String generateCustomerId() {
        String prefix = "C";
        String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        return prefix + substring;
    }
}
