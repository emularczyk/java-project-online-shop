package com.candyshop.islodycze.registration;

import com.candyshop.islodycze.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}