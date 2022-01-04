package com.mile.portal.study;

import com.mile.portal.util.CommonUtil;
import com.mile.portal.util.MailUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringTest {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public MailUtil mailUtil;

    @Test
    @DisplayName("1. 테스트")
    void test1() {
        String stringByLowerAlphabet = CommonUtil.createStringByLowerAlphabet(20);

        Base64.Encoder encoder = Base64.getEncoder();
        String string = new String(encoder.encode(stringByLowerAlphabet.getBytes(StandardCharsets.UTF_8)));

        System.out.println(passwordEncoder.encode(stringByLowerAlphabet));
        System.out.println(stringByLowerAlphabet);
        System.out.println(string);
    }

    @Test
    @DisplayName("2. 메일 발송")
    void test2() {
        Map<String, Object> mailProperty = new HashMap<>();
        mailProperty.put("emailCode", CommonUtil.createStringByLowerAlphabet(10));

        mailUtil.sendTemplateMail("ppp9026@naver.com", "이메일 인증을 해주세요.", "ppp9026@naver.com", mailProperty, "mail/login");
    }

}