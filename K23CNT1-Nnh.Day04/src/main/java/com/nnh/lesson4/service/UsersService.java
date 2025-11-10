package com.nnh.lesson4.service;
import com.nnh.lesson4.dto.UsersDTO;
import com.nnh.lesson4.entity.Users;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class UsersService {
    List<Users> userList = new ArrayList<Users>();

    public UsersService() {
        userList.add(new Users(1L, "user1", "pass1", "John Doe", LocalDate.parse("1990-01-01"), "john@example.com", "1234567890", 34, true));
        userList.add(new Users(2L, "user2", "pass2", "Jane Smith", LocalDate.parse("1995-05-15"), "jane@example.com", "0987654321", 32, false));
        userList.add(new Users(3L, "user3", "pass3", "Alice Johnson", LocalDate.parse("1985-11-22"), "alice@example.com", "1122334455", 39, true));
        userList.add(new Users(4L, "user4", "pass4", "Bob Brown", LocalDate.parse("1988-03-18"), "bob@example.com", "6677889900", 36, true));
        userList.add(new Users(5L, "user5", "pass5", "Charlie White", LocalDate.parse("1992-08-30"), "charlie@example.com", "4455221100", 29, false));
    }

    public List<Users> findAll() {
        return userList;
    }

    public Boolean create(UsersDTO usersDTO) {
        try {
            Users user = new Users();

            user.setId((long) (userList.size() + 1)); // Simplified ID generation
            user.setUsername(usersDTO.getUsername());
            user.setPassword(usersDTO.getPassword());
            user.setEmail(usersDTO.getEmail());
            user.setFullName(usersDTO.getFullName());
            user.setPhone(usersDTO.getPhone());
            user.setAge(usersDTO.getAge());
            user.setBirthDay(usersDTO.getBirthDay());
            user.setStatus(usersDTO.getStatus());
            userList.add(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}