package ru.otus.hw.controllers.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер api должен")
@WebMvcTest(controllers = {AuthorController.class, BookController.class,
        CommentController.class, GenreController.class})
@Import(SecurityConfig.class)
class ControllersSecurityTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final long FIRST_COMMENT_ID = 1L;

    private static final String USERNAME = "testUser";
    private static final String[] ROLES = {"ADMIN", "CAN_READ_AUTHORS","CAN_READ_GENRES", "CAN_READ_BOOKS",
            "CAN_EDIT_BOOKS", "CAN_EDIT_COMMENTS"};

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private GenreService genreService;

    @DisplayName("возвращать ожидаемый статус")
    @ParameterizedTest(name = "{0} {1} for user {2} with roles {3} should return {4} status")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(String method, String url, String userName, String[] roles, int status, boolean checkLoginRedirection) throws Exception {
        var request = method2RequestBuilder(method, url);
        if (Objects.nonNull(userName)) {
            request = request.with(user(userName).roles(roles));
        }
        ResultActions resultActions = mvc.perform(request).andExpect(status().is(status));
        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post,
                        "put", MockMvcRequestBuilders::put,
                        "delete", MockMvcRequestBuilders::delete);
        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {

        return Stream.of(
                Arguments.of("get", "/api/v1/authors", USERNAME, ROLES, 200, false)
                , Arguments.of("get", "/api/v1/authors", null, null, 302, true)

                , Arguments.of("get", "/api/v1/books", USERNAME, ROLES, 200, false)
                , Arguments.of("get", "/api/v1/books", null, null, 302, true)
                , Arguments.of("get", String.format("/api/v1/books/%s", FIRST_BOOK_ID), USERNAME, ROLES, 200, false)
                , Arguments.of("get", String.format("/api/v1/books/%s", FIRST_BOOK_ID), null, null, 302, true)
                , Arguments.of("post", "/api/v1/books", USERNAME, ROLES, 400, false)
                , Arguments.of("post", "/api/v1/books", null, null, 302, true)
                , Arguments.of("put", String.format("/api/v1/books/%s", FIRST_BOOK_ID), USERNAME, ROLES, 400, false)
                , Arguments.of("put", String.format("/api/v1/books/%s", FIRST_BOOK_ID), null, null, 302, true)
                , Arguments.of("delete", String.format("/api/v1/books/%s", FIRST_BOOK_ID), USERNAME, ROLES, 204, false)
                , Arguments.of("delete", String.format("/api/v1/books/%s", FIRST_BOOK_ID), null, null, 302, true)

                , Arguments.of("get", String.format("/api/v1/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID),
                        USERNAME, ROLES, 200, false)
                , Arguments.of("get", String.format("/api/v1/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID), null, null, 302, true)
                , Arguments.of("post", String.format("/api/v1/books/%s/comments", FIRST_BOOK_ID), USERNAME, ROLES, 400, false)
                , Arguments.of("post", String.format("/api/v1/books/%s/comments", FIRST_BOOK_ID), null, null, 302, true)
                , Arguments.of("put", String.format("/api/v1/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID), USERNAME, ROLES, 400, false)
                , Arguments.of("put", String.format("/api/v1/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID), null, null, 302, true)
                , Arguments.of("delete", String.format("/api/v1/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID),
                        USERNAME, ROLES, 204, false)
                , Arguments.of("delete", String.format("/api/v1/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID), null, null, 302, true)

                , Arguments.of("get", "/api/v1/genres", USERNAME, ROLES, 200, false)
                , Arguments.of("get", "/api/v1/genres", null, null, 302, true)
        );
    }
}