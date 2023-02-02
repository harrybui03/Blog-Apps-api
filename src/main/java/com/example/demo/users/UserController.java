package com.example.demo.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId){
        return ResponseEntity.ok(userService.getUser(userId)) ;
    }
    //update user yourself
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto ,@PathVariable("id") Long userId){
        return new ResponseEntity<>(userService.updateUser(userDto , userId) , HttpStatus.OK);
    }
    //delete user role admin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User is deleted successfully" , HttpStatus.OK);
    }
}

