package com.mile.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<Integer> auditorProvider(){
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<Integer>{
        @Override
        public Optional<Integer> getCurrentAuditor() {
//            if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Integer) {
//                return Optional.of(1);
//            }else{
//                return Optional.empty();
//            }
            return Optional.of(1);
        }
    }
}
