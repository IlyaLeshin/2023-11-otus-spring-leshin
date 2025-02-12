package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;

@DisplayName("Сервис для работы с пользователями должен")
@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {
    private static final long FIRST_USER_ID = 1L;
    private static final String FIRST_USER_USERNAME = "testUser";
    private static final String FIRST_USER_PASSWORD = "testPassword";
    private static final long FIRST_ROLE_ID = 1L;
    private static final long SECOND_ROLE_ID = 2L;
    private static final String FIRST_ROLE_NAME = "TEST_ROLE_1";
    private static final String SECOND_ROLE_NAME = "TEST_ROLE_2";
    private static final Role FIRST_ROLE = new Role(FIRST_ROLE_ID, "TEST_ROLE_1");
    private static final Role SECOND_ROLE = new Role(SECOND_ROLE_ID, "TEST_ROLE_2");
    private static final User FIRST_USER = new User(FIRST_USER_ID, FIRST_USER_USERNAME, FIRST_USER_PASSWORD,
            Set.of(FIRST_ROLE, SECOND_ROLE));
    private static final UserDto FIRST_USER_DTO = new UserDto(FIRST_USER_ID, FIRST_USER_USERNAME, FIRST_USER_PASSWORD,
            List.of(FIRST_ROLE_NAME, SECOND_ROLE_NAME));

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
