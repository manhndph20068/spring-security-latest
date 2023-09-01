package com.javamanh.dto;

import com.javamanh.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoRequest {
    private String name;
    private String email;
    private String password;
    private Integer idRole;
}
