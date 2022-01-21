package com.rlchat.server.persistence.repositories;

import com.rlchat.server.domain.UserObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserObjectRepository extends JpaRepository<UserObject, Long> {

    Optional<UserObject> getUserObjectByLogin(String login);

}
