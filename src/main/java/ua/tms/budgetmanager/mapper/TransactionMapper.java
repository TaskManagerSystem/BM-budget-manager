package ua.tms.budgetmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.tms.budgetmanager.config.MapperConfiguration;
import ua.tms.budgetmanager.data.dto.transaction.TransactionCreateDto;
import ua.tms.budgetmanager.data.dto.transaction.TransactionResponseDto;
import ua.tms.budgetmanager.data.model.Transaction;
import ua.tms.budgetmanager.data.model.Wallet;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wallet", source = "wallet")
    Transaction toModel(TransactionCreateDto dto, Wallet wallet);

    TransactionResponseDto toResponseDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    void updateTransactionFromDto(TransactionCreateDto dto, @MappingTarget Transaction transaction);
}
