package com.onlinelearning.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.onlinelearning.entity.User;
import com.onlinelearning.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            Long userId = Long.valueOf(username);
            user = userMapper.selectById(userId);
        } catch (NumberFormatException e) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, username));
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new UserDetailsImpl(user);
    }
}
