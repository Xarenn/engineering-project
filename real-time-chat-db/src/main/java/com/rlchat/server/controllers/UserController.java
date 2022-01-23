package com.rlchat.server.controllers;

import com.rlchat.server.persistence.UserManagement;
import com.rlchat.server.security.SpringSecurityConfig;
import com.rlchat.server.service.dto.LoginRequestDTO;
import com.rlchat.server.service.dto.LoginResponseDTO;
import com.rlchat.server.service.dto.RegisterRequestDTO;
import com.rlchat.server.service.dto.UserObjectDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {

    private final UserManagement userManagement;

    @PostMapping("/user/login")
    public Mono<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userManagement.loginUser(loginRequestDTO)
                .onErrorMap(SpringSecurityConfig::mapError);

    }

    @GetMapping("/user/list")
    public Mono<List<UserObjectDTO>> getAllUsers() {
        return userManagement.getAllUsers().onErrorMap(SpringSecurityConfig::mapError);

    }

    @PostMapping("/user/register")
    public Mono<String> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return userManagement.registerUser(registerRequestDTO)
                .onErrorMap(SpringSecurityConfig::mapError);
    }

}
