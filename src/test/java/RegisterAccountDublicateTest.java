import accountRegistr.Account;
import accountRegistr.TokenInfo;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RegisterAccountDublicateTest extends BaseTest{
    final String email = "alina22222@yandex.ru";
    final String password = "1234";
    final String name = "alina";
    HashMap<String, String> user = new HashMap<>();

    @Step("Send POST request /api/auth/register")
    public Response sendPostRequestRegisterUser(Account account) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(account)
                .when()
                .post("/api/auth/register");
    }

    @Step("Create user")
    public Account createUser(HashMap<String, String> account) {
        return new Account(account);
    }

    @Step("Comparing the body and status of the message when creating a duplicate user")
    public void compareBodyMessageAndStatus403FromDublicateUser(Response response) {
        response.then().assertThat().body("message", equalTo("User already exists"))
                .and().body("success", equalTo(false))
                .and().statusCode(HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test
    public void registerDublicateUser() {
        user.putAll(Map.of("email", email, "password", password, "name", name));
        Account account = createUser(user);
        sendPostRequestRegisterUser(account);
        Response response = sendPostRequestRegisterUser(account);
        compareBodyMessageAndStatus403FromDublicateUser(response);
    }

    @After
    public void deleteUser(){
        //user.putAll(Map.of("email", email,"password", password)));
        //Account account = createUser(user);
        if (getAccessToken(email, password)!=null) {
            deleteUser(getAccessToken(email, password).replace("Bearer ",""));
        }
    }
}