package com.candyshop.islodycze.registration;

import com.candyshop.islodycze.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByVerificationCode(String code);
}