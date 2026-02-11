package ua.tms.budgetmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.tms.budgetmanager.config.MapperConfiguration;
import ua.tms.budgetmanager.dto.UserResponseDto;
import ua.tms.budgetmanager.data.model.User;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface UserMapper {

    @Mapping(source = "user.id", target = "userId")
    UserResponseDto toDto(User user);
}
