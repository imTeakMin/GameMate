package com.example.mvctest.repository.userrepository;
//1
import com.example.mvctest.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    void save(User user);
    User findById(Long id);
    List<User> findAll();
    Optional<User> findByLoginId(String loginId);
}
