package com.ahmed.Spring_Security.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine; // because we send html templates

    @Async // to do not make the user wait to long
    public void sendEmail(
            String to,
            String username ,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
       String templateName;
       if(emailTemplate==null){
           templateName="confirm-email";
       }else{
           templateName=emailTemplate.getName();
       }
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(
          mimeMessage,
          MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        //how to pass some parameters to our html template
        Map<String , Object> props=new HashMap<>();
        props.put("username" , username);
        props.put("confirmationUrl" , confirmationUrl);
        props.put("activation_code" , activationCode);

        //this context from thymeleaf
        Context context=new Context();
        context.setVariables(props);
        mimeMessageHelper.setFrom("ahmdbrl811@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = templateEngine.process(templateName , context);
        mimeMessageHelper.setText(template , true);// the true is to verify the template is html or not
        javaMailSender.send(mimeMessage);








    }

}
