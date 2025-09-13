package com.example.mvctest.repository.userrepository;

import java.util.List;

public interface UserRepository {

    void save(User user);
    User findById(Long id);
    List<User> findAll();
}
