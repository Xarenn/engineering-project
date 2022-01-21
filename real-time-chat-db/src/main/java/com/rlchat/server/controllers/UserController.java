package com.rlchat.server.controllers;

import com.rlchat.server.persistence.UserManagement;
import com.rlchat.server.security.SpringSecurityConfig;
import com.rlchat.server.service.dto.LoginRequestDTO;
import com.rlchat.server.service.dto.LoginResponseDTO;
import com.rlchat.server.service.dto.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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

    @PostMapping("/user/register")
    public Mono<String> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return userManagement.registerUser(registerRequestDTO)
                .onErrorMap(SpringSecurityConfig::mapError);
    }

}
