package ru.otus.hw.converters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Конвертер для работы с пользователями")
@SpringBootTest(classes = {UserConverter.class})
public class UserConverterTest {
    private static final long FIRST_USER_ID = 1L;
    private static final String FIRST_USER_USERNAME = "testUser";
    private static final String FIRST_USER_PASSWORD = "testPassword";
    private static final long FIRST_ROLE_ID = 1L;
    private static final long SECOND_ROLE_ID = 2L;
    private static final String FIRST_ROLE_NAME = "TEST_ROLE_1";
    private static final String SECOND_ROLE_NAME = "TEST_ROLE_2";
    private static final Role FIRST_ROLE = new Role(FIRST_ROLE_ID, FIRST_ROLE_NAME);
    private static final Role SECOND_ROLE = new Role(SECOND_ROLE_ID, SECOND_ROLE_NAME);
    private static final User FIRST_USER = new User(FIRST_USER_ID, FIRST_USER_USERNAME, FIRST_USER_PASSWORD,
            Set.of(FIRST_ROLE, SECOND_ROLE));
    private static final UserDto FIRST_USER_DTO = new UserDto(FIRST_USER.getId(), FIRST_USER.getUsername(), FIRST_USER.getPassword(),
            FIRST_USER.getRoles().stream().map(Role::getName).toList());

    @Autowired
    private UserConverter userConverter;

    @DisplayName("корректно преобразовывать модель в DTO. текущий метод modelToDto(User user)")
    @Test
    void modelToDtoTest() {
        UserDto actualUserDto = userConverter.modelToDto(FIRST_USER);

        assertThat(actualUserDto).isEqualTo(FIRST_USER_DTO);
    }
}
