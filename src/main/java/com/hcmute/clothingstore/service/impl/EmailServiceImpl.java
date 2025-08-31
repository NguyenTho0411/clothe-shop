package com.hcmute.clothingstore.service.impl;

import com.hcmute.clothingstore.appconstant.AppConstant;
import com.hcmute.clothingstore.entity.User;
import com.hcmute.clothingstore.service.interfaces.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${sendgrid.from-email}")
    private String fromEmail;
    @Value("${cors.allowed-origins}")
    private String baseUrl;
    private final SpringTemplateEngine springTemplateEngine;

    private final SendGrid sendGrid;
    @Override
    @Async
    public void sendActivationCode(User savedUser) {
        this.sendEmailFromTemplateSync(savedUser.getEmail(), AppConstant.ACTIVATION_CODE_EMAIL_SUBJECT,
                AppConstant.ACTIVATION_CODE_EMAIL_TEMPLATE, savedUser.getProfile().getFullName(),
                savedUser.getActivationCode());
    }

    private void sendEmailFromTemplateSync(String to, String subject,
                                           String templateName, String username,
                                           String key) {
        Context context = new Context();
        context.setVariable("name", username);
        context.setVariable("key",key);
        context.setVariable("baseUrl",baseUrl);
        String content = springTemplateEngine.process(templateName,context);
        this.sendEmailSync(to,subject,content,false,true);
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        try {
            Email emailFrom = new Email(fromEmail);
            Email emailTo = new Email(to);
            Content emailContent = new Content(isHtml ? "text/html" : "text/plain", content);
            Mail mail = new Mail(emailFrom, subject, emailTo, emailContent);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            // 👇 thêm log để chắc chắn
            log.info("SendGrid response: status={}, body={}, headers={}",
                    response.getStatusCode(), response.getBody(), response.getHeaders());

            if (response.getStatusCode() >= 400) {
                log.error("Fail to send email. Status code: {}, Response: {}",
                        response.getStatusCode(), response.getBody());
            }
        } catch (IOException e) {
            log.error("Email could not be sent to user: {}", to, e);
        }
    }

}
