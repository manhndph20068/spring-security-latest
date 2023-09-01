package com.javamanh.service.impl;

import com.javamanh.dto.UserInfoRequest;
import com.javamanh.entity.Role;
import com.javamanh.entity.Account;
import com.javamanh.repository.UserInfoRepository;
import com.javamanh.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInforServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Account register(UserInfoRequest userInfoRequest) {
        Account userInfo = new Account();
        userInfo.setName(userInfoRequest.getName());
        userInfo.setEmail(userInfoRequest.getEmail());
        userInfo.setPassword(passwordEncoder.encode(userInfoRequest.getPassword()));
        userInfo.setRole(Role.builder().id(userInfoRequest.getIdRole()).build());
        return userInfoRepository.save(userInfo);
    }
}
