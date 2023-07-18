package kr.co.sionms.demo.hello.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.sionms.demo.hello.model.UserEntity;
import kr.co.sionms.demo.hello.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    public OAuthUserServiceImpl() {
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // DefaultOAuth2USerService의 기존 loadUser를 호출한다 이 메서드가 user-info-uri를 이용해 사용자 정보를
        // 가져오는 부분이다
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            log.info("OAuth2User attributes{}", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // login 필드를 가져온다
        // final String username = (String) oAuth2User.getAttributes().get("login");
        final String authProvider = userRequest.getClientRegistration().getClientName();

        String username = null;
        if (authProvider.equalsIgnoreCase("github")) {
            username = (String) oAuth2User.getAttribute("login");
        } else if (authProvider.equalsIgnoreCase("google")) {
            username = (String) oAuth2User.getAttribute("email");
        }

        UserEntity userEntity = null;

        // 유저가 존재하지 않으면 새로 생성한다.
        if (!userRepository.existsByUsername(username)) {
            userEntity = UserEntity.builder().username(username).auth_provider(authProvider).build();
            userEntity = userRepository.save(userEntity);
        } else {
            userEntity = userRepository.findByUsername(username);
        }

        log.info("successfully pulled user info username {} authProvider {}", username, authProvider);
        return new ApplicationOAuth2User(userEntity.getId(), oAuth2User.getAttributes());
    }
}
