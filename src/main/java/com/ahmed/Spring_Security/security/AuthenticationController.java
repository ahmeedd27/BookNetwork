package com.ahmed.Spring_Security.security;

import com.ahmed.Spring_Security.handler.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name="Authentication") // because the swagger
public class AuthenticationController {

    private final AuthenticationService authService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED) // by default the response is accepted
    public ResponseEntity<?> register(
        @RequestBody @Valid RegistrationRequest registrationRequest
    ) throws MessagingException {
        authService.register(registrationRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest ar
    ){
        return ResponseEntity.ok(authService.authenticate(ar));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        authService.activateAccount(token);
    }

}
