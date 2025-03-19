package com.ahmed.Spring_Security.handler;

import com.ahmed.Spring_Security.security.RegistrationRequest;
import com.ahmed.Spring_Security.dao.RoleRepo;
import com.ahmed.Spring_Security.dao.TokenRepo;
import com.ahmed.Spring_Security.dao.UserRepo;
import com.ahmed.Spring_Security.security.AuthenticationRequest;
import com.ahmed.Spring_Security.security.AuthenticationResponse;
import com.ahmed.Spring_Security.security.JwtService;
import com.ahmed.Spring_Security.services.EmailService;
import com.ahmed.Spring_Security.services.EmailTemplateName;
import com.ahmed.Spring_Security.entities.Role;
import com.ahmed.Spring_Security.entities.Token;
import com.ahmed.Spring_Security.entities.User;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepo rr;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo ur;
    private final TokenRepo tr;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest registrationRequest) throws MessagingException {

        Role r=rr.findByName("USER").orElseThrow();
        User u=User
                .builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false) // he will enable it when he activate the account
                .roles(List.of(r))
                .build();
        ur.save(u);
        sendValidationEmail(u);


    }

    private String generateActivationCode(int length){
        String c="0123456789";
        StringBuilder codeBuilder=new StringBuilder();
        SecureRandom sr=new SecureRandom();
        for(int i=0;i<length;i++){
            int randomIndex=sr.nextInt(c.length()); // this will take a random index from 0..9
            codeBuilder.append(c.charAt(randomIndex)); // this will store the random index and repeat the process
            // this process will repeated based on the length that i will give to this method
        }
        return codeBuilder.toString();
    }
    private String generateAndSaveActivationToken(User u){
        String generatedToken=generateActivationCode(6);
        Token t= Token
                .builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(u)
                .build();
        tr.save(t);
        return generatedToken;//return the code
    }



    private void sendValidationEmail(User u) throws MessagingException {
        String t=generateAndSaveActivationToken(u);
        emailService.sendEmail(
                u.getEmail() ,
                u.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl ,
                t,
                "Account Activation"
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest ar) {
        var authUser=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        ar.getEmail(),
                        ar.getPassword()
                )

        );
        var claims=new HashMap<String , Object>();
        User user=(User)authUser.getPrincipal();
        claims.put("fullName" , user.getName());
        String t=jwtService.generateToken(claims , user);


       return AuthenticationResponse
               .builder()
               .token(t)
               .build();
  }


  @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token t=tr.findByToken(token).orElseThrow(() -> new UsernameNotFoundException("NotFound"));
        if(LocalDateTime.now().isAfter(t.getExpiresAt())){
            sendValidationEmail(t.getUser());
        }
        User user=t.getUser();
        user.setEnabled(true);
        ur.save(user);
        t.setValidatedAt(LocalDateTime.now());
        tr.save(t);
    }


}
