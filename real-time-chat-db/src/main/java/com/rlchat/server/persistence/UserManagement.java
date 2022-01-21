package com.rlchat.server.persistence;

import com.rlchat.server.domain.Role;
import com.rlchat.server.domain.UserObject;
import com.rlchat.server.exceptions.BadRequest;
import com.rlchat.server.persistence.repositories.UserObjectRepository;
import com.rlchat.server.security.util.JwtUtil;
import com.rlchat.server.service.dto.LoginRequestDTO;
import com.rlchat.server.service.dto.LoginResponseDTO;
import com.rlchat.server.service.dto.RegisterRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserManagement {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserObjectRepository userObjectRepository;

    public Mono<LoginResponseDTO> loginUser(LoginRequestDTO loginRequestDTO) {
        Optional<UserObject> userObject = userObjectRepository.getUserObjectByLogin(loginRequestDTO.getLogin());

        if(userObject.isEmpty()) {
            return Mono.error(new BadRequest("User not found with login: " + loginRequestDTO.getLogin()));
        }

        if(!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), userObject.get().getPassword())) {
            return Mono.error(new BadRequest("User login or password are invalid"));
        }

        return Mono.justOrEmpty(LoginResponseDTO.builder()
                        .token(jwtUtil.generateToken(String.valueOf(userObject.get().getId()),
                                Collections.singletonList(userObject.get().getRole())))
                .build());
    }

    public Mono<String> registerUser(RegisterRequestDTO registerRequestDTO) {
        Optional<UserObject> userObject = userObjectRepository.getUserObjectByLogin(registerRequestDTO.getLogin());

        if(userObject.isPresent()) {
            return Mono.error(new BadRequest("User found with login: " + registerRequestDTO.getLogin()));
        }

        final UserObject userObject1 = UserObject.builder()
                .name(registerRequestDTO.getName())
                .login(registerRequestDTO.getLogin())
                .password(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()))
                .role(Role.USER)
                .build();

        userObjectRepository.save(userObject1);
        return Mono.justOrEmpty("Register success");
    }

}
