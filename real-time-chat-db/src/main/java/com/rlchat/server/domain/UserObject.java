package com.rlchat.server.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserObject {

    @Id
    @SequenceGenerator(name = "user_object_generator", sequenceName = "user_object_sequence", allocationSize = 1)
    @GeneratedValue(generator = "user_object_generator", strategy = GenerationType.SEQUENCE)
    private long id;

    private String login;
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fromUser", orphanRemoval = true)
    private Set<MessageObject> messageObjects;

}
