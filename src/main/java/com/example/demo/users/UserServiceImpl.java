package com.example.demo.users;

import com.example.demo.exception.BlogAPIException;
import com.example.demo.users.dto.ChangePasswordDto;
import com.example.demo.users.dto.UpdateRoleDto;
import com.example.demo.users.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        private final ModelMapper mapper;

        private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.mapper = mapper;
            this.passwordEncoder = passwordEncoder;
    }


        @Override
        public UserDto getUser(Long userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
            return mapToDTO(user);
        }

        @Override
        public UserDto updateUser(UserDto userDto, Long userId, String name) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
            if(!user.getUsername().equals(name)){
                throw new BlogAPIException("You are not authorized to update this user", HttpStatus.UNAUTHORIZED);
            }
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user = userRepository.save(user);
            return mapToDTO(user);
        }

        @Override
        public void deleteUser(Long userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
            userRepository.delete(user);
        }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())){
            throw new BlogAPIException("Old password is not correct", HttpStatus.BAD_REQUEST);
        }
        if(!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())){
            throw new BlogAPIException("New password and confirm password are not the same", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDto updateRole(UpdateRoleDto updateRoleDto, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
        if(!user.getRole().equals(Roles.ADMIN)){
            throw new BlogAPIException("You are not authorized to update role", HttpStatus.UNAUTHORIZED);
        }
        User userToUpdate = userRepository.findByUsername(updateRoleDto.getUsername()).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
        userToUpdate.setRole(updateRoleDto.getRole());
        userRepository.save(userToUpdate);
        return mapToDTO(userToUpdate);
    }

    private User mapToEntity(UserDto userDto){
            return mapper.map(userDto, User.class);
        }

        private UserDto mapToDTO(User user){
            return mapper.map(user, UserDto.class);
        }
    }
