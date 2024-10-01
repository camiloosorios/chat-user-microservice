package com.bosorio.app.user_microservice.repository;

import com.bosorio.app.user_microservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
}
