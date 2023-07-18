package kr.co.sionms.demo.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sionms.demo.hello.model.UserEntity;
import kr.co.sionms.demo.hello.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        final String username = userEntity.getUsername();

        if (userRepository.existsByUsername(username)) {
            String msg = "User name already exisy";
            log.warn(msg + "{}", username);
            throw new RuntimeException(msg);
        }
        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUer = userRepository.findByUsername(username);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if (originalUer != null && encoder.matches(password, originalUer.getPassword())) {
            return originalUer;
        }
        return null;
        // return userRepository.findByUsernameAndPassword(username, password);
    }
}
