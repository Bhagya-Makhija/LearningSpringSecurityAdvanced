package com.bhagya.spring_security.myApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhagya.spring_security.myApp.entity.AuthProviderType;
import com.bhagya.spring_security.myApp.entity.OAuthAccount;

@Repository
public interface OAuthAccountsRepository extends JpaRepository<OAuthAccount, Long> {
    Optional<OAuthAccount> findByProviderTypeAndProviderId(AuthProviderType providerType, String providerId);    
}
