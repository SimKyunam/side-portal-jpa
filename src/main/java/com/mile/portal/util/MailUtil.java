package com.mile.portal.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailUtil {

    private final TemplateEngine htmlTemplateEngine;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendTemplateMail(String toMail,
                                 String subject,
                                 String fromName,
                                 Map<String, Object> variables,
                                 String templatePath) {
        Context context = new Context();
        context.setVariables(variables);

        try {
            String htmlTemplate = htmlTemplateEngine.process(templatePath, context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

            messageHelper.setFrom(new InternetAddress(from, fromName));
            messageHelper.setTo(toMail);
            messageHelper.setSubject(subject);
            messageHelper.setText(htmlTemplate, true);

            mailSender.send(mimeMessage);
            log.info(toMail + " / 메일 발송 성공");

            Thread.sleep(300); //여러건 발송시 문제 발생
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("메일 발송에 실패했습니다.");
        }
    }
}
