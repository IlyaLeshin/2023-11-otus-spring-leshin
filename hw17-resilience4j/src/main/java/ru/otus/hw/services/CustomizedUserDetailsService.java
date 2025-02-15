package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.exceptions.UserNotFoundException;

@RequiredArgsConstructor
@Service
public class CustomizedUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDto userDto = userService.findByUsername(username);
            String[] roles = userDto.getRoles().toArray(String[]::new);

            return org.springframework.security.core.userdetails.User.builder()
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .roles(roles)
                    .build();
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("Username %s not found".formatted(username), e);
        }
    }
}