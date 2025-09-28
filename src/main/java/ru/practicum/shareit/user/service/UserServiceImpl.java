package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toDto(userRepository.getUserById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toDtoList(userRepository.getAllUsers());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        return UserMapper.toDto(userRepository.createUser(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setId(id);
        return UserMapper.toDto(userRepository.updateUser(id, user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }
}
