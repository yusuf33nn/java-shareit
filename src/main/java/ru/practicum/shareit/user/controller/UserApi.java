package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Validated
@RequestMapping(path = "/users")
public interface UserApi {

    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUserById(@NotNull @PathVariable("id") Long id);

    @GetMapping
    ResponseEntity<List<UserDto>> getAllUsers();

    @PostMapping
    ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto);

    @PatchMapping("/{id}")
    ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDto userDto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable("id") Long id);
}
