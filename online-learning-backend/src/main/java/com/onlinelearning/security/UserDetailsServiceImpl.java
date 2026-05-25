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
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new UserDetailsImpl(user);
    }

    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userId);
        }

        return new UserDetailsImpl(user);
    }
}
