package com.rlchat.server.service.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class RegisterRequestDTO {

    private String login;
    private String name;
    private String password;

}
