package ua.tms.budgetmanager.service.userservice;

import jakarta.persistence.EntityExistsException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tms.budgetmanager.data.dto.CreateUserDto;
import ua.tms.budgetmanager.data.dto.UserResponseDto;
import ua.tms.budgetmanager.data.enumariton.RoleName;
import ua.tms.budgetmanager.mapper.UserMapper;
import ua.tms.budgetmanager.data.model.Role;
import ua.tms.budgetmanager.data.model.User;
import ua.tms.budgetmanager.repository.RoleRepository;
import ua.tms.budgetmanager.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto createUser(final CreateUserDto userDto) {
        if (isUserExist(userDto.getUsername())) {
            throw new EntityExistsException("user with userName %s does exist".formatted(userDto.getUsername()));
        }

        final Role role = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new IllegalArgumentException("Default role USER not found in database"));

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .userRoles(List.of(role))
                .build();

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto updateUser(final CreateUserDto userDto, final Long userId) {
        if (!isUserExist(userDto.getUsername())) {
            throw new EntityExistsException("user with userName %s does not exist".formatted(userDto.getUsername()));
        }

        User user = getUserById(userId);

        user.setUsername(userDto.getUsername());
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public String deleteUser(final Long userId) {
        User userOptional = getUserById(userId);

        userRepository.delete(userOptional);
        return "User with id %s deleted".formatted(userId);
    }


    private boolean isUserExist(final String username) {
        return userRepository.existsByUsername(username);
    }

    private User getUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found by id %s".formatted(id)));
    }

}
