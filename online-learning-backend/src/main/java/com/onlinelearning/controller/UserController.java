package com.onlinelearning.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.dto.Result;
import com.onlinelearning.entity.User;
import com.onlinelearning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role) {
        return Result.success(userService.getUserPage(page, size, keyword, role));
    }

    @GetMapping("/all")
    public Result<List<User>> getAll() {
        return Result.success(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PostMapping
    public Result<?> create(@RequestBody User user) {
        userService.createUser(user);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}