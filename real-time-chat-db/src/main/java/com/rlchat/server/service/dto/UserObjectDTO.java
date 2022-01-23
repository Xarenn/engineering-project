package com.rlchat.server.service.dto;

import com.rlchat.server.domain.UserObject;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserObjectDTO {

    private String label;
    private long id;
    private String login;
    private String name;

    public static UserObjectDTO map(UserObject userObject) {
        return UserObjectDTO.builder()
                .name(userObject.getName())
                .login(userObject.getLogin())
                .label(userObject.getName()+"@"+userObject.getLogin())
                .id(userObject.getId())
                .build();
    }

}
