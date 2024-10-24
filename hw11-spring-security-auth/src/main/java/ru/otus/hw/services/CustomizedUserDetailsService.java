package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.UserDto;

@RequiredArgsConstructor
@Service
public class CustomizedUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.findByUsername(username);
        if (userDto != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .build();
        }
        throw new UsernameNotFoundException("Username %s not found".formatted(username));
    }
}