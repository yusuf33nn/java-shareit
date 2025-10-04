package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long id);

    User findByUserId(Long userId);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserUpdateDto userDto);

    void deleteUser(Long id);
}
