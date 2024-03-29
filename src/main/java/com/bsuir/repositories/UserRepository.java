package com.bsuir.repositories;

import com.bsuir.models.User;
import com.bsuir.models.UserActive;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findFirstByUsername(String username);

    boolean deleteByUsername(String username);

    boolean existsUserByUsername(String username);

}
