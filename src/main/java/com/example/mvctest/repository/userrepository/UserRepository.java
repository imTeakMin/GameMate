package com.example.mvctest.repository.userrepository;

import com.example.mvctest.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository {

    void save(User user);
    User findById(Long id);
    List<User> findAll();
}
