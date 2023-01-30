package com.example.demo.users;

import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

@FieldNameConstants
public enum Roles implements Collection<GrantedAuthority> {
     USER , ADMIN , MEMBER;

     @Override
     public int size() {
          return 0;
     }

     @Override
     public boolean isEmpty() {
          return false;
     }

     @Override
     public boolean contains(Object o) {
          return false;
     }

     @Override
     public Iterator<GrantedAuthority> iterator() {
          return null;
     }

     @Override
     public Object[] toArray() {
          return new Object[0];
     }

     @Override
     public <T> T[] toArray(T[] a) {
          return null;
     }

     @Override
     public boolean add(GrantedAuthority grantedAuthority) {
          return false;
     }

     @Override
     public boolean remove(Object o) {
          return false;
     }

     @Override
     public boolean containsAll(Collection<?> c) {
          return false;
     }

     @Override
     public boolean addAll(Collection<? extends GrantedAuthority> c) {
          return false;
     }

     @Override
     public boolean removeAll(Collection<?> c) {
          return false;
     }

     @Override
     public boolean retainAll(Collection<?> c) {
          return false;
     }

     @Override
     public void clear() {

     }
}
