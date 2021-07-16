package com.equeue.repository;

import com.equeue.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {

    User save(User user);

    List<User> findAll();

    User findById(Long id);

    User findByName(String name);
}
