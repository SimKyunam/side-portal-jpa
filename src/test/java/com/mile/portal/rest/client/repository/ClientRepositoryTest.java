package com.mile.portal.rest.client.repository;

import com.mile.portal.rest.client.model.domain.Client;
import com.mile.portal.rest.common.model.enums.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("1. 사용자 저장")
    @Test
    void test_1() {
        Client client = createClient();

        Client newClient = clientRepository.save(client);

        assertAll(
                () -> assertEquals(client.getLoginId(), newClient.getLoginId())
                , () -> assertEquals(client.getLoginPwd(), newClient.getLoginPwd())
        );
    }

    @DisplayName("2. 사용자 Empty 값 저장")
    @Test
    void test_2() {
        Client client = createClient();
        client.setLoginId(null);
        client.setLoginPwd(null);

        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(client);
        });
    }

    public Client createClient() {
        Client client = new Client();
        client.setLoginId("test");
        client.setLoginPwd(passwordEncoder.encode("1111"));
        client.setName("홍길동");
        client.setPermission(Authority.ROLE_USER);
        client.setIcisNo("A12345678");
        client.setStatus("ACT");

        return client;
    }
}