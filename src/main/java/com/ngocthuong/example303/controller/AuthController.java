package com.ngocthuong.example303.controller;

import com.ngocthuong.example303.repository.AppUserRepository;
import com.ngocthuong.example303.repository.RoleRepository;
import com.ngocthuong.example303.security.JwtGenerator;
import com.ngocthuong.example303.dto.AuthResponseDto;
import com.ngocthuong.example303.dto.LoginDto;
import com.ngocthuong.example303.dto.RegisterDto;
import com.ngocthuong.example303.model.AppUser;
import com.ngocthuong.example303.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(AppUserRepository appUserRepository,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator,
            AuthenticationManager authenticationManager) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

        if (appUserRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles = roleRepository.findByName(registerDto.getRole()).get();
        appUser.setRoles(Collections.singletonList(roles));

        appUserRepository.save(appUser);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    @PostMapping("login")
public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtGenerator.generateToken(authentication);
    System.out.println("Token JWT: " + token);

    AuthResponseDto response = new AuthResponseDto(token);
    System.out.println("AuthResponseDto: " + response);

    return ResponseEntity.ok(response);
}
}
