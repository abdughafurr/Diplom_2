package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private RequestSpecification getBaseSpec() {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .baseUri(EndPoints.BASE_URL);
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return getBaseSpec()
                .when()
                .get(EndPoints.INGREDIENTS)
                .then();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(String accessToken, List<String> ingredients) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .body(Map.of("ingredients", ingredients))
                .when()
                .post(EndPoints.ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(List<String> ingredients) {
        return getBaseSpec()
                .body(Map.of("ingredients", ingredients))
                .when()
                .post(EndPoints.ORDERS)
                .then();
    }
}