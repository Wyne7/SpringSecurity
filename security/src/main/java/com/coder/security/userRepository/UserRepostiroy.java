package com.coder.security.userRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coder.security.model.User;

@Repository
public interface UserRepostiroy extends JpaRepository<User,Integer>{

    Optional<User> findByEmail(String email);
    
}
