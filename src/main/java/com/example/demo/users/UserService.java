package com.example.demo.users;

public interface UserService {
    UserDto getUser(Long userId);
    UserDto updateUser(UserDto userDto , Long userId);
    void deleteUser(Long userId);
}
