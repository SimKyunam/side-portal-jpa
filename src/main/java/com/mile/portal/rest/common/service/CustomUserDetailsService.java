package com.mile.portal.rest.common.service;

import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.user.repository.UserRepository;
import com.mile.portal.rest.user.model.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.");
                });
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserType().getAuthority());

        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(),
                user.getLoginPwd(),
                Collections.singleton(grantedAuthority)
        );
    }
}