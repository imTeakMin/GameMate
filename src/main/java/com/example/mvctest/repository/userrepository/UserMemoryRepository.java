package com.example.mvctest.repository.userrepository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserMemoryRepository implements UserRepository{

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

    public void clearStore(){
        store.clear();
    }

}
