package com.ahmed.Spring_Security.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "should not being empty")
    @NotBlank(message = "not mandatory")
    @Email(message="Email is not formatted")
    private String email;
    @NotEmpty(message = "should not being empty")
    @NotBlank(message = "not mandatory")
    @Size(min=8 , message="Password should b characters long minimum")
    private String password;
}
