package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        accessToken = userClient.create(user).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    public void loginExistingUserReturnsOk() {
        userClient.login(user)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Вход с неверным логином и паролем")
    public void loginWithWrongCredentialsReturnsUnauthorized() {
        User wrongUser = new User("wrong-" + user.getEmail(), "wrong-password", user.getName());

        userClient.login(wrongUser)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}