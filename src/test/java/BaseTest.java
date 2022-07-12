import account.Account;
import account.TokenInfo;
import ingredients.Ingredients;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class BaseTest {
    static ArrayList<String> loginPass = new ArrayList<>();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    public void deleteUser(String token) {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .delete("/api/auth/user");
    }

    public String getAccessToken(String email, String password){
        HashMap<String, String> user = new HashMap<>();
        if(email!=null)
            user.put("email", email);
        if(password!=null)
            user.put("password", password);
        Account account = new Account(user);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(account)
                .when()
                .post("/api/auth/login")
                .as(TokenInfo.class).getAccessToken();
    }

    // метод для шага "Отправить запрос":
    @Step("Send POST request /api/auth/register")
    public Response sendPostRequestRegisterUser(Account account) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(account)
                .when()
                .post("/api/auth/register");
    }

    @Step("Send POST request /api/auth/login")
    public Response sendPostRequestAuthUser(Account account) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(account)
                        .when()
                        .post("/api/auth/login");
        return response;
    }

    @Step("Send Get request not auth /api/orders")
    public Response sendGetRequestOrders() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/orders");
    }

    @Step("Send Get request auth /api/orders")
    public Response sendGetRequestOrders(String token) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
                .when()
                .get("/api/orders");
    }

    @Step("Send POST request with auth /api/auth/register")
    public Response sendPatchRequestEditUser(Account account, String token) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
                .body(account)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Send POST request not auth /api/auth/register")
    public Response sendPatchRequestEditUser(Account account) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(account)
                .when()
                .patch("/api/auth/user");
    }

    // метод для шага "Отправить запрос":
    @Step("Send POST request /api/orders")
    public Response sendPostRequestCreateOrder(Ingredients ingredients) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }

    // метод для шага "Отправить запрос":
    @Step("Send POST request /api/orders")
    public Response sendPostRequestCreateOrder(Ingredients ingredients, String token) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }

    // метод для шага "Отправить запрос":
    @Step("Send POST request /api/orders")
    public Response sendPostRequestCreateOrder(String token) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .when()
                .post("/api/orders");
    }
}
