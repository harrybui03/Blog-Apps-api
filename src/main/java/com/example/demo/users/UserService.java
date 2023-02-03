package com.example.demo.users;

import com.example.demo.users.dto.ChangePasswordDto;
import com.example.demo.users.dto.UpdateRoleDto;
import com.example.demo.users.dto.UserDto;

public interface UserService {
    UserDto getUser(Long userId);
    UserDto updateUser(UserDto userDto , Long userId, String name);
    void deleteUser(Long userId);

    void changePassword(ChangePasswordDto changePasswordDto, String name);

    UserDto updateRole(UpdateRoleDto updateRoleDto, String name);
}
