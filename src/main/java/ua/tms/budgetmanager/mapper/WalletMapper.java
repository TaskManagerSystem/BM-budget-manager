package ua.tms.budgetmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.tms.budgetmanager.config.MapperConfiguration;
import ua.tms.budgetmanager.data.dto.wallet.WalletCreateDto;
import ua.tms.budgetmanager.data.dto.wallet.WalletDetailsDto;
import ua.tms.budgetmanager.data.dto.wallet.WalletListDto;
import ua.tms.budgetmanager.data.model.User;
import ua.tms.budgetmanager.data.model.Wallet;

@Mapper(componentModel = "spring", config = MapperConfiguration.class, uses = {TransactionMapper.class})
public interface WalletMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "transactions", ignore = true)
  @Mapping(target = "user", source = "user")
  Wallet toModel(WalletCreateDto dto, User user);

  WalletListDto toListDto(Wallet wallet);

  WalletDetailsDto toDetailsDto(Wallet wallet);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "transactions", ignore = true)
  void updateWalletFromDto(WalletCreateDto dto, @MappingTarget Wallet wallet);
}
