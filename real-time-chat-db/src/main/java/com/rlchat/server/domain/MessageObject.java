package com.rlchat.server.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageObject {

    @Id
    @SequenceGenerator(name = "message_object_generator", sequenceName = "message_object_sequence", allocationSize = 1)
    @GeneratedValue(generator = "message_object_generator", strategy = GenerationType.SEQUENCE)
    private long id;

    private String lastMessage;
    private LocalDateTime lastMessageDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "messageObject", orphanRemoval = true)
    private Set<Message> messages;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserObject fromUser;

    private long toUser;

}
