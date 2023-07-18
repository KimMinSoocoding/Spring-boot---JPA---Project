package kr.co.sionms.demo.hello.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.sionms.demo.hello.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);

    UserEntity findByUsernameAndPassword(String username, String password);

}
