package com.mile.portal.config;

import com.mile.portal.rest.common.model.domain.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider(){
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<Long>{
        @Override
        public Optional<Long> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication != null) {
                if(authentication.getPrincipal() instanceof Account) {
                    Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    return Optional.of(account.getId());
                }
            }

            return Optional.empty();
        }
    }
}
