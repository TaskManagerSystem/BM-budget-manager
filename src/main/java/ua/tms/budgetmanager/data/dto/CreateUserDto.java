package ua.tms.budgetmanager.data.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String password;
    private String repeatedPassword;
    private String firstName;
}
