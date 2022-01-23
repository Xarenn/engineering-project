package com.rlchat.server.service.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class LoginResponseDTO {

    private String token;
    private Long id;

}
