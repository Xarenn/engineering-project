package com.rlchat.server.persistence.repositories;

import com.rlchat.server.domain.Message;
import com.rlchat.server.domain.MessageObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> getAllByMessageObject(MessageObject messageObject, Pageable pageable);

}
