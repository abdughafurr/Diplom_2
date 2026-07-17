package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private String accessToken;
    private List<String> ingredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getRandom();
        accessToken = userClient.create(user).extract().path("accessToken");

        List<String> allIngredients = orderClient.getIngredients().extract().path("data._id");
        ingredients = List.of(allIngredients.get(0), allIngredients.get(1));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuthReturnsOk() {
        orderClient.createOrder(accessToken, ingredients)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthReturnsOk() {
        orderClient.createOrderWithoutAuth(ingredients)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void createOrderWithIngredientsReturnsOk() {
        orderClient.createOrder(accessToken, ingredients)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsReturnsBadRequest() {
        orderClient.createOrder(accessToken, List.of())
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHashReturnsInternalServerError() {
        orderClient.createOrder(accessToken, List.of("invalid_hash_12345"))
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}