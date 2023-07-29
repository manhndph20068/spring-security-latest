package com.javamanh.controller;

import com.javamanh.config.UserInfoUserDetailsService;
import com.javamanh.dto.AuthRequest;
import com.javamanh.dto.JwtResponse;
import com.javamanh.dto.Product;
import com.javamanh.entity.RefreshToken;
import com.javamanh.entity.UserInfo;
import com.javamanh.repository.UserInfoRepository;
import com.javamanh.service.JwtService;
import com.javamanh.service.ProductService;
import com.javamanh.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @Autowired
    private UserInfoRepository userInfoRepository;



    @PostMapping("/signUp")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getAllTheProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return service.getProduct(id);
    }


    @PostMapping("/login")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getEmail(), response);

            String accessToken = jwtService.generateToken(authRequest.getEmail());

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
            UserInfo userInfo = new UserInfo(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken())
                    .userInfo(userInfo)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(HttpServletRequest request) {
        String refreshTokenFromCookie = refreshTokenService.getRefreshTokenFromCookie(request);

        if (refreshTokenFromCookie == null) {
            throw new RuntimeException("Refresh token is not in cookie!");
        }

        if (jwtService.isRefreshTokenExpired(refreshTokenFromCookie)) {
            throw new RuntimeException("Refresh token has expired. Please make a new signin request");
        }

        String email = jwtService.extractUsernameFromRefreshToken(refreshTokenFromCookie);
        String accessToken = jwtService.generateToken(email);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .token(refreshTokenFromCookie)
                .build();
    }


    @GetMapping("/fetchAccount")
    public UserInfo fetchAccountInfo(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Access token is missing or invalid!");
        }
        String accessToken = authorizationHeader.substring(7);


        String email = jwtService.extractEmail(accessToken);
        System.out.println("email "+email);

        Optional<UserInfo> userInfoOptional = userInfoRepository.findByEmail(email);

        if (userInfoOptional.isPresent()) {
            UserInfo userInfo = userInfoOptional.get();
            return userInfo;
        } else {
            throw new RuntimeException("User not found for email: " + email);
        }
    }


}
