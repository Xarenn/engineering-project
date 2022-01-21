package com.rlchat.server.service.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class LoginRequestDTO {

    private String login;
    private String password;

}
