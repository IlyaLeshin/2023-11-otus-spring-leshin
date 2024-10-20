package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.UserDto;

@RequiredArgsConstructor
@Service
public class CusomizedUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.findByUsername(username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
    }
}
