package com.example.mvctest.repository.userrepository;

import com.example.mvctest.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

<<<<<<< HEAD
@Component
public class UserMemoryRepository implements UserRepository{
=======
@Repository
public class UserMemoryRepository implements UserRepositoryPort{
>>>>>>> feature/login_register_sangbeom

    private static Map<Long, User> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    @Getter
    private static final UserMemoryRepository instance = new UserMemoryRepository();

    private UserMemoryRepository(){} // 이거 왜 하는거지?

    @Override
    public void save(User user) {
        user.setId(++sequence);
        store.put(user.getId(), user);
    }

    @Override
    public User findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return store.values().stream()
                .filter(user -> user.getLoginId().equals(loginId))
                .findFirst();
    }

    public void clearStore(){
        store.clear();
    }

}