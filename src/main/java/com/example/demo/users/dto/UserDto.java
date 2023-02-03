package com.example.demo.users.dto;

import com.example.demo.users.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;

    private Roles role;
}
