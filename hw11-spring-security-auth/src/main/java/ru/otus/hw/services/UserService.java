package ru.otus.hw.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.dto.UserDto;

public interface UserService extends UserDetailsService {

    UserDto findByUsername(String username);
}
