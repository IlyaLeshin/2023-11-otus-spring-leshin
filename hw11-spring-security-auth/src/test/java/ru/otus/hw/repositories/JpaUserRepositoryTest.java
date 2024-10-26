package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.User;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с пользователями ")
@DataJpaTest
class JpaUserRepositoryTest {

    private static final long FIRST_USER_ID = 1L;
    private static final String FIRST_USER_USERNAME = "testUser";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @DisplayName("должен пользователя по имени")
    @Test
    void shouldReturnCorrectUserByName() {
        var optionalActualUser = userRepository.findByUsername(FIRST_USER_USERNAME);
        var expectedBook = testEntityManager.find(User.class, FIRST_USER_ID);

        assertThat(optionalActualUser).isPresent().get()
                .isEqualTo(expectedBook);
    }
}
