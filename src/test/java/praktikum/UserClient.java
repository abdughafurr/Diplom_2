package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient {

    private RequestSpecification getBaseSpec() {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .baseUri(EndPoints.BASE_URL);
    }

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return getBaseSpec()
                .body(user)
                .when()
                .post(EndPoints.REGISTER)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(User user) {
        return getBaseSpec()
                .body(user)
                .when()
                .post(EndPoints.LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .when()
                .delete(EndPoints.USER)
                .then();
    }
}