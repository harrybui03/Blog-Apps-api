package com.example.demo.users;

import com.example.demo.exception.BlogAPIException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        private final ModelMapper mapper;

        public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
            this.userRepository = userRepository;
            this.mapper = mapper;
        }


        @Override
        public UserDto getUser(Long userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
            return mapToDTO(user);
        }

        @Override
        public UserDto updateUser(UserDto userDto, Long userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BlogAPIException("User not found", HttpStatus.NOT_FOUND));
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

        private User mapToEntity(UserDto userDto){
            return mapper.map(userDto, User.class);
        }

        private UserDto mapToDTO(User user){
            return mapper.map(user, UserDto.class);
        }
    }
