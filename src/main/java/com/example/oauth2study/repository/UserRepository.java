package com.example.oauth2study.repository;

import com.example.oauth2study.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Account,Long> {

   Account findByUsername(String username);
}
