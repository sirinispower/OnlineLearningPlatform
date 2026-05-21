package com.onlinelearning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.dto.RegisterRequest;
import com.onlinelearning.entity.User;
import com.onlinelearning.mapper.UserMapper;
import com.onlinelearning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    @Override
    public void register(RegisterRequest request) {
        if (getByUsername(request.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setRole("user");
        user.setStatus(1);

        userMapper.insert(user);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public Page<User> getUserPage(Integer page, Integer size, String keyword, String role) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or()
                    .like(User::getNickname, keyword)
                    .or()
                    .like(User::getEmail, keyword));
        }
        
        if (StringUtils.hasText(role)) {
            wrapper.eq(User::getRole, role);
        }
        
        wrapper.orderByDesc(User::getCreatedAt);
        return userMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public void createUser(User user) {
        if (getByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getNickname() == null) {
            user.setNickname(user.getUsername());
        }
        if (user.getRole() == null) {
            user.setRole("user");
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        userMapper.insert(user);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
}