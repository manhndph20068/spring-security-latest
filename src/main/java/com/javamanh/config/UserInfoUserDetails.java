package com.javamanh.config;

import com.javamanh.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoUserDetails implements UserDetails {
    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails(Account userInfo) {
    name = userInfo.getEmail();
    password = userInfo.getPassword();
    authorities = Arrays.stream(userInfo.getRole().getName().split(","))  // Giả sử getName() trả về danh sách các roles tách bằng dấu ","
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // ko dùng stream
//    authorities = new ArrayList<>();
//    for (String role : userInfo.getRole().getName().split(",")) {
//        authorities.add(new SimpleGrantedAuthority(role));
//    }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
