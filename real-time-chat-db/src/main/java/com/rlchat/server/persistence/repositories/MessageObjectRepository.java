package com.rlchat.server.persistence.repositories;

import com.rlchat.server.domain.MessageObject;
import com.rlchat.server.domain.UserObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MessageObjectRepository extends JpaRepository<MessageObject, Long> {

    Page<MessageObject> getAllByFromUserAndToUser(UserObject fromUser, Long toUser, Pageable pageable);

}
