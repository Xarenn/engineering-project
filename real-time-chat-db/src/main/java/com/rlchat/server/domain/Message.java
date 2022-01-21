package com.rlchat.server.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    @Id
    @SequenceGenerator(name = "message_generator", sequenceName = "message_sequence", allocationSize = 1)
    @GeneratedValue(generator = "message_generator", strategy = GenerationType.SEQUENCE)
    private long id;

    private String message;

    @Column
    private LocalDateTime time;

    private long userFrom;
    private long userTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private MessageObject messageObject;


}
