package com.equeue.repository.impl;

import com.equeue.entity.User;
import org.springframework.stereotype.Repository;
import com.equeue.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    Map<Long, User> userMap = new HashMap<>();
    Long id = 1L;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(id++);
        }
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User findById(Long id) {
        return userMap.get(id);
    }

}
