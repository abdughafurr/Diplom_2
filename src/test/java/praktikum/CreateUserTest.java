package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {

    private static final String REQUIRED_FIELDS_MESSAGE = "Email, password and name are required fields";

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserReturnsOk() {
        ValidatableResponse response = userClient.create(user);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void createExistingUserReturnsForbidden() {
        ValidatableResponse firstResponse = userClient.create(user);
        accessToken = firstResponse.extract().path("accessToken");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailReturnsForbidden() {
        user.setEmail(null);

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordReturnsForbidden() {
        user.setPassword(null);

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameReturnsForbidden() {
        user.setName(null);

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }
}