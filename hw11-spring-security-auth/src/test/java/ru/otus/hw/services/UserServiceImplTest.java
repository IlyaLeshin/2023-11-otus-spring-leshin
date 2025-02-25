package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;

@DisplayName("Сервис для работы с книгами должен")
@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {
    private static final long FIRST_USER_ID = 1L;
    private static final String FIRST_USER_USERNAME = "testUser";
    private static final String FIRST_USER_PASSWORD = "testPassword";
    private static final User FIRST_USER = new User(FIRST_USER_ID, FIRST_USER_USERNAME, FIRST_USER_PASSWORD);
    private static final UserDto FIRST_USER_DTO = new UserDto(FIRST_USER_ID, FIRST_USER_USERNAME, FIRST_USER_PASSWORD);

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserConverter userConverter;

    @DisplayName("загружать пользователя по username. текущий метод: findByUsername(String username)")
    @Test
    void findByUsernameTest() {
        when(userRepository.findByUsername(FIRST_USER_USERNAME)).thenReturn(Optional.of(FIRST_USER));
        when(userConverter.modelToDto(FIRST_USER)).thenReturn(FIRST_USER_DTO);
        UserDto actualUserDto = userService.findByUsername(FIRST_USER_USERNAME);

        assertThat(actualUserDto)
                .isEqualTo(FIRST_USER_DTO);
    }
}
