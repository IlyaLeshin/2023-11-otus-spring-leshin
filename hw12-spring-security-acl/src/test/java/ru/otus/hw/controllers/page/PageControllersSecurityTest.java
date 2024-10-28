package ru.otus.hw.controllers.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.MethodSecurityConfig;
import ru.otus.hw.config.SecurityConfig;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллеры страниц должен")
@WebMvcTest(controllers = {BookPageController.class, CommentPageController.class})
@Import({SecurityConfig.class, MethodSecurityConfig.class})
class PageControllersSecurityTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final long FIRST_COMMENT_ID = 1L;
    private static final String USERNAME = "testUser";
    private static final String[] ROLES = {"ADMIN"};

    @Autowired
    private MockMvc mvc;

    @DisplayName("Должен возвращать ожидаемый статус")
    @ParameterizedTest(name = "{0} {1} for user {2} with roles {3} should return {4} status")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(String method, String url, String userName, String[] roles, int status,
                                    boolean checkLoginRedirection) throws Exception {
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
                Map.of("get", MockMvcRequestBuilders::get);
        return methodMap.get(method).apply(url);
    }


    public static Stream<Arguments> getTestData() {

        return Stream.of(
                Arguments.of("get", "/books", USERNAME, ROLES, 200, false)
                , Arguments.of("get", "/books", null, null, 302, true)
                , Arguments.of("get", String.format("/books/%s", FIRST_BOOK_ID), USERNAME, ROLES, 200, false)
                , Arguments.of("get", String.format("/books/%s", FIRST_BOOK_ID), null, null, 302, true)
                , Arguments.of("get", "/books/creation-form", USERNAME, ROLES, 200, false)
                , Arguments.of("get", "/books/creation-form", null, null, 302, true)
                , Arguments.of("get", String.format("/books/%s/editing-form", FIRST_BOOK_ID), USERNAME, ROLES, 200, false)
                , Arguments.of("get", String.format("/books/%s/editing-form", FIRST_BOOK_ID), null, null, 302, true)

                , Arguments.of("get", String.format("/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID), USERNAME, ROLES, 200, false)
                , Arguments.of("get", String.format("/books/%s/comments/%s", FIRST_BOOK_ID, FIRST_COMMENT_ID), null, null, 302, true)
                , Arguments.of("get", String.format("/books/%s/comments/creation-form", FIRST_BOOK_ID), USERNAME, ROLES, 200, false)
                , Arguments.of("get", String.format("/books/%s/comments/creation-form", FIRST_BOOK_ID), null, null, 302, true)
                , Arguments.of("get", String.format("/books/%s/comments/%s/editing-form", FIRST_BOOK_ID, FIRST_COMMENT_ID), USERNAME, ROLES, 200, false)
                , Arguments.of("get", String.format("/books/%s/comments/%s/editing-form", FIRST_BOOK_ID, FIRST_COMMENT_ID), null, null, 302, true)
        );
    }
}