package ua.tms.budgetmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.tms.budgetmanager.config.MapperConfiguration;
import ua.tms.budgetmanager.data.dto.WalletDto;
import ua.tms.budgetmanager.data.model.User;
import ua.tms.budgetmanager.data.model.Wallet;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface WalletMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "transactions", ignore = true)
  @Mapping(target = "user", source = "user")
  Wallet toModel(WalletDto walletDto, User user);

  WalletDto toDto(Wallet wallet);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "transactions", ignore = true)
  void updateWalletFromDto(WalletDto dto, @MappingTarget Wallet wallet);
}
