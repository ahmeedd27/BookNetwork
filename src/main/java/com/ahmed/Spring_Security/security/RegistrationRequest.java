package com.ahmed.Spring_Security.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "should not being empty")
    @NotBlank(message = "not mandatory")
    private String name;
    @NotEmpty(message = "should not being empty")
    @NotBlank(message = "not mandatory")
    @Email(message="Email is not formatted")
    private String email;
    @NotEmpty(message = "should not being empty")
    @NotBlank(message = "not mandatory")
    @Size(min=8 , message="Password should b characters long minimum")
    private String password;

}
