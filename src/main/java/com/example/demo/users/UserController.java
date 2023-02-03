package com.example.demo.users;

import com.example.demo.users.dto.ChangePasswordDto;
import com.example.demo.users.dto.UpdateRoleDto;
import com.example.demo.users.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto ,@PathVariable("id") Long userId , Authentication authentication){
        return new ResponseEntity<>(userService.updateUser(userDto , userId , authentication.getName()) , HttpStatus.OK);
    }
    //delete user role admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User is deleted successfully" , HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto , Authentication authentication){
        userService.changePassword(changePasswordDto , authentication.getName());
        return new ResponseEntity<>("Password is changed successfully" , HttpStatus.OK);
    }

    @PutMapping("/updateRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> updateRole(@RequestBody UpdateRoleDto updateRoleDto , Authentication authentication){
        return new ResponseEntity<>(userService.updateRole(updateRoleDto , authentication.getName()) , HttpStatus.OK);
    }
}

