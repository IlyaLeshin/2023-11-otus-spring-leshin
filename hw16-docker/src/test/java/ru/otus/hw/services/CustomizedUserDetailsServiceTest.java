package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.exceptions.UserNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с данными пользователей должен")
@SpringBootTest(classes = CustomizedUserDetailsService.class)
class CustomizedUserDetailsServiceTest {
    private static final long FIRST_USER_ID = 1L;
    private static final String FIRST_USER_USERNAME = "testUser";
    private static final String FIRST_USER_PASSWORD = "testPassword";
    private static final String FIRST_ROLE_NAME = "TEST_ROLE_1";
    private static final String SECOND_ROLE_NAME = "TEST_ROLE_2";
   private static final UserDto FIRST_USER_DTO = new UserDto(FIRST_USER_ID, FIRST_USER_USERNAME, FIRST_USER_PASSWORD,
            List.of(FIRST_ROLE_NAME, SECOND_ROLE_NAME));

    @Autowired
    private CustomizedUserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @DisplayName("загружать данные пользователя по username. текущий метод: findByUsername(String username)")
    @Test
    void loadUserByUsername() {
        when(userService.findByUsername(FIRST_USER_USERNAME)).thenReturn(FIRST_USER_DTO);
        var details = userDetailsService.loadUserByUsername(FIRST_USER_USERNAME);

        assertThat(details.getUsername())
                .isEqualTo(FIRST_USER_USERNAME);
    }

    @DisplayName("загружать данные пользователя по username. текущий метод: findByUsername(String username)")
    @Test
    void trowUsernameNotFoundExceptionWhenUserNotFoundInLoadUserByUsername() {
        when(userService.findByUsername(FIRST_USER_USERNAME)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(FIRST_USER_USERNAME));
    }
}