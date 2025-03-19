package com.ahmed.Spring_Security.dao;

import com.ahmed.Spring_Security.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token,Integer> {

    Optional<Token> findByToken(String token);

}
