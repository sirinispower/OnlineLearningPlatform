package com.onlinelearning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.dto.RegisterRequest;
import com.onlinelearning.entity.User;

import java.util.List;

public interface UserService {
    User getByUsername(String username);

    void register(RegisterRequest request);

    User getById(Long id);

    Page<User> getUserPage(Integer page, Integer size, String keyword, String role);

    List<User> getAllUsers();

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);
}