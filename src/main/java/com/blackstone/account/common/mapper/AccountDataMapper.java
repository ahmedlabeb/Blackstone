package com.blackstone.account.common.mapper;

import java.util.List;
import java.util.UUID;

import com.blackstone.account.entity.dto.AccountDto;
import com.blackstone.account.entity.domain.Account;
import org.mapstruct.*;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AccountDataMapper {

    AccountDto toDTO(Account account);

    @Mapping(target = "accountId", expression = "java(generateAccountId(customerDto.getCustomerId()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(AccountDto customerDto);

    List<AccountDto> toDtoList(List<Account> accounts);

    List<Account> toEntityList(List<AccountDto> accountDtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountFromDto(AccountDto dto, @MappingTarget Account entity);

     default String generateAccountId(String customerId) {
        String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 3);
        return customerId.concat(substring);
    }
}
