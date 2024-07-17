package com.micro.authentication.repository;

import com.micro.authentication.entity.Role;
import com.micro.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional findByEmail(String username);

    User findByRole(Role admin);
}
