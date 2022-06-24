import accountRegistr.Account;
import accountRegistr.TokenInfo;
import accountRegistr.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RegisterAccountDublicateTest extends BaseTest{

    private final String description;
    private Account user;
    private Account userDuplicates;
    private final boolean isSuccess;
    private final String message;
    private final int statusCode;

    public RegisterAccountDublicateTest(String description, Account user, Account userDuplicates, boolean isSuccess, String message, int statusCode) {
        this.description = description;
        this.user = user;
        this.userDuplicates = userDuplicates;
        this.isSuccess = isSuccess;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1} {2} {3}")
    public static Object[] getUserWithDifferentParameters() {
        return new Object[][]{
                {"Содание полного дубликата пользователя", (Map.of("email", "alina22222@yandex.ru","password", "1234", "name", "alina")), (Map.of("email", "alina22222@yandex.ru","password", "1234", "name", "alina")), false, "User already exists", HttpURLConnection.HTTP_FORBIDDEN},
                {"Содание дубликата только по email и паролю",(Map.of("email", "alina22222@yandex.ru","password", "1234", "name", "alina1")), (Map.of("email", "alina22222@yandex.ru","password", "1234", "name", "alina")), false, "User already exists", HttpURLConnection.HTTP_FORBIDDEN},
                {"Содание дубликата только по email",(Map.of("email", "alina22222@yandex.ru","password", "12345", "name", "alina")), (Map.of("email", "alina22222@yandex.ru","password", "1234", "name", "alina")), false, "User already exists", HttpURLConnection.HTTP_FORBIDDEN},
                {"Содание дубликата только по паролю","alina22222@yandex.ru", "1234", null, true, null, HttpURLConnection.HTTP_OK},
        };
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

        Account account = createUser(user);
        sendPostRequestRegisterUser(account);
        Response response = sendPostRequestRegisterUser(account);
        compareBodyMessageAndStatus403FromDublicateUser(response);
    /*final String email = "alina22222@yandex.ru";
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
    }*/

    @After
    public void deleteUser(){
        if (getAccessToken(user.getEmail(), user.getPassword())!=null) {
            deleteUser(getAccessToken(user.getEmail(), user.getPassword()).replace("Bearer ",""));
        }
    }
}