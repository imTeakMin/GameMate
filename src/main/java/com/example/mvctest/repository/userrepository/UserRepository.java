package com.example.mvctest.repository.userrepository;

import com.example.mvctest.domain.User;

import java.util.List;

public interface UserRepository {

    void save(User user);
    User findById(Long id);
    List<User> findAll();
}
