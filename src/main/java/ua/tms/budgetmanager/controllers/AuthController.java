package ua.tms.budgetmanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.tms.budgetmanager.data.dto.CreateUserDto;
import ua.tms.budgetmanager.data.dto.UserLoginRequestDto;
import ua.tms.budgetmanager.security.AuthenticationService;
import ua.tms.budgetmanager.service.userservice.UserService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody final CreateUserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid final UserLoginRequestDto requestDto) {
        return ResponseEntity.ok(authenticationService.login(requestDto));
    }
}