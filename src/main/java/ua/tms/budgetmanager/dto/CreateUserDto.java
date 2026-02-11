package ua.tms.budgetmanager.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String password;
    private String repeatedPassword;
    private String firstName;
}
