package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userinfo")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;


    @ManyToOne
    @JoinColumn(name = "id_roles")
    private Role role;

    public Account(Integer id, String name, String email, String password, Role role, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;

        List<String> roleNames = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roleNames.add(authority.getAuthority());
        }

        if (!roleNames.isEmpty()) {
            String roleName = roleNames.get(0);
            this.role = new Role(roleName);
        }
    }

    public Account(String name, String password, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.password = password;

        List<String> roleNames = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roleNames.add(authority.getAuthority());
        }

        if (!roleNames.isEmpty()) {
            String roleName = roleNames.get(0);
            this.role = new Role(roleName);
        }


    }

}
