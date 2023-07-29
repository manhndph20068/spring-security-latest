package com.javamanh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userinfo")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
//    private String roles;
//
//    public UserInfo(String name, String password, Collection<? extends GrantedAuthority> authorities) {
//        this.name = name;
//        this.password = password;
//        this.roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
//    }

    @ManyToOne
    @JoinColumn(name = "id_roles")
    private Role role;

    public UserInfo(String name, String password, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.password = password;

        // Tạo danh sách các role từ các đối tượng GrantedAuthority
        List<String> roleNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Tạo đối tượng Role từ tên của role (trong trường hợp này chỉ lưu một role cho mỗi người dùng)
        if (!roleNames.isEmpty()) {
            String roleName = roleNames.get(0); // Giả sử chỉ lưu một role
            this.role = new Role(roleName);
        }
    }
}
