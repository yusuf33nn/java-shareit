package ru.practicum.shareit.user.repo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final AtomicLong userCounter = new AtomicLong();
    private final Map<Long, User> users =  new HashMap<>();

    @Override
    public User getUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(id)));
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public User createUser(User user) {
        checkUniqueEmail(user.getEmail());
        var userId = userCounter.incrementAndGet();
        user.setId(userId);
        users.put(userId, user);
        log.info("User with id {} created", userId);
        return user;
    }

    @Override
    public User updateUser(Long id, User user) {
        checkUniqueEmail(user.getEmail());
        users.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        users.remove(id);
    }

    private void checkUniqueEmail(String emailToCheck) {
        var isNotUniqueEmail = users.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> Strings.CS.equals(email, emailToCheck));
        if (isNotUniqueEmail) {
            throw new DuplicateEmailException("User with email %s already exists".formatted(emailToCheck));
        }
    }
}
