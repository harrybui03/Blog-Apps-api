package com.example.demo.users.dto;

import com.example.demo.users.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {
    private String username;
    private Roles role;
}

