package com.rlchat.server.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rlchat.server.domain.UserObject;
import com.rlchat.server.exceptions.BadRequest;
import com.rlchat.server.persistence.repositories.UserObjectRepository;
import com.rlchat.server.security.util.JwtUtil;
import com.rlchat.server.service.dto.RegisterRequestDTO;

import reactor.core.publisher.Mono;

class UserManagementTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserObjectRepository userObjectRepository;

    @InjectMocks
    private UserManagement objectUnderTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void registerUser() {
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .name("name")
                .password("passw")
                .login("login")
                .build();
        when(userObjectRepository.getUserObjectByLogin(registerRequestDTO.getLogin()))
                .thenReturn(Optional.empty());
        Mono<String> response = objectUnderTest.registerUser(registerRequestDTO);

        verify(userObjectRepository).save(any());
        assertEquals(response.block(), "Register success");
    }

    @Test
    void registerUser_failure() {
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .name("name")
                .password("passw")
                .login("login")
                .build();
        when(userObjectRepository.getUserObjectByLogin(registerRequestDTO.getLogin()))
                .thenReturn(Optional.of(UserObject.builder().build()));

        BadRequest badRequest = assertThrows(BadRequest.class, () -> objectUnderTest.registerUser(registerRequestDTO).block());

        verify(userObjectRepository).getUserObjectByLogin(any());
        verifyNoMoreInteractions(userObjectRepository);
        assertEquals(badRequest.getMessage(), "User found with login: login");
    }

    @Test
    void getAllUsers() {
    }
}