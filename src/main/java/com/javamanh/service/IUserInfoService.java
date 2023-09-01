package com.javamanh.service;

import com.javamanh.dto.UserInfoRequest;
import com.javamanh.entity.Account;

public interface IUserInfoService {
    Account register(UserInfoRequest userInfoRequest);
}
