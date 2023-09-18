package com.backend.service;

import com.backend.dto.request.RegisterRequest;
import com.backend.entity.Account;

public interface IAccountService {
    Account register(RegisterRequest registerRequest);
}
