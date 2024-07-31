package com.claudioscagliotti.thesis.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest extends LoginRequest{
    @NotBlank(message = "Age cannot be blank")
    private int age;
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @NotBlank(message = "Role cannot be blank")
    private String role;
    @NotBlank(message = "Mail cannot be blank")
    private String email;
}
