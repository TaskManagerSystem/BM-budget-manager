package ua.tms.budgetmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.tms.budgetmanager.config.MapperConfig;
import ua.tms.budgetmanager.dto.UserResponseDto;
import ua.tms.budgetmanager.model.User;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface UserMapper {

    @Mapping(source = "user.id", target = "userId")
    UserResponseDto toDto(User user);
}
