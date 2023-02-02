package com.example.demo.users;

import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

@FieldNameConstants
public enum Roles {
     USER, ADMIN, MEMBER;

     public static Roles getRole(String role) {
          for (Roles r : Roles.values()) {
               if (r.name().equals(role)) {
                    return r;
               }
          }
          return null;
     }
}
