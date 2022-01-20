package com.mile.portal.config.security;

import com.mile.portal.rest.client.model.domain.Client;
import com.mile.portal.rest.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final ClientRepository clientRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // registrationId
        // 햔재 로그인 진행 중인 서비스를 구분하는 코드
        // 네이버 로그인인지, 구글 로그인인지 구분하기위해 사용한다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // userNameAttributeName
        // OAuth2 로그인 진행 시 키가 되는 필드값을 말한다. PK와 같은 의미이다.
        // 구글의 경우 기본적으로 코드를 지원한다. 구글의 기본 코드는 "sub"이다. (네이버, 카카오 등은 기본 지원하지 않는다.)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes
        // OAuth2UserService를 통해 가져온 OAuth2User의 attributes를 담을 클래스이다.
        // 다른 소셜 로그인도 이 클래스를 사용한다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // SessionUser
        // 세션에 사용자 정보를 저장하기 위한 따로 만든 Dto 클래스이다.
        Client client = saveOrUpdate(attributes);

        log.info("로그인 시도");
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(client.getPermission().getAuthority())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Client saveOrUpdate(OAuthAttributes attributes) {
        Client client = clientRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.oauth2Update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return clientRepository.save(client);
    }
}