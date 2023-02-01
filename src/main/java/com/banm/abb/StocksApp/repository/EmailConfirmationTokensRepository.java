package com.banm.abb.StocksApp.repository;

import com.banm.abb.StocksApp.entity.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmationTokensRepository extends JpaRepository<EmailConfirmationToken, Long> {

    Optional<EmailConfirmationToken> findEmailConfirmationTokenByToken(String token);
}
