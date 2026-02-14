package ua.tms.budgetmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.tms.budgetmanager.data.dto.CreateUserDto;
import ua.tms.budgetmanager.service.userservice.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/update/{userId}")
    @Secured(value = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> updateUser(@RequestBody final CreateUserDto userDto, @PathVariable final Long userId) {
        return ResponseEntity.ok(userService.updateUser(userDto, userId));
    }

    @DeleteMapping("/delete/{userId}")
    @Secured(value = {"ROLE_ADMIN"})
    public ResponseEntity<?> deleteUser(@PathVariable final Long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
}
