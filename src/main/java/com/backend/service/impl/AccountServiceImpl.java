package com.backend.service.impl;

import com.backend.dto.request.RegisterRequest;
import com.backend.entity.Role;
import com.backend.entity.Account;
import com.backend.repository.AccountRepository;
import com.backend.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Account register(RegisterRequest registerRequest) {
        Account account = new Account();
        account.setName(registerRequest.getName());
        account.setEmail(registerRequest.getEmail());
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setRole(Role.builder().id(registerRequest.getIdRole()).build());
        return accountRepository.save(account);
    }
}
