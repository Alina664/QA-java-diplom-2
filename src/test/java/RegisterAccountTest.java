import accountRegistr.Account;
import accountRegistr.TokenInfo;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Регистрация")
@RunWith(Parameterized.class)
public class RegisterAccountTest extends BaseTest{
    private final String description;
    private final String email;
    private final String password;
    private final String name;
    private final boolean isSuccess;
    private final String message;
    private final int statusCode;
    HashMap<String, String> user = new HashMap<>();

    public RegisterAccountTest(String description, String email, String password, String name, boolean isSuccess, String message, int statusCode) {
        this.description = description;
        this.email = email;
        this.password = password;
        this.name = name;
        this.isSuccess = isSuccess;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1} {2} {3}")
    public static Object[] getUserWithDifferentParameters() {
        return new Object[][]{
                {"Успешная регистрация", "alina22222@yandex.ru", "1234", "alina", true, null, HttpURLConnection.HTTP_OK},
                {"Регистрация без email",null, "1234", "alina", false, "Email, password and name are required fields", HttpURLConnection.HTTP_FORBIDDEN},
                {"Регистрация без пароля","alina22222@yandex.ru", null, "alina", false, "Email, password and name are required fields", HttpURLConnection.HTTP_FORBIDDEN},
                {"Регистрация без имени","alina22222@yandex.ru", "1234", null, false, "Email, password and name are required fields", HttpURLConnection.HTTP_FORBIDDEN},
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


    @Step("Compare result from request")
    public void compareResult(Response response) {
        response.then().assertThat().body("message", equalTo(message))
                .and().body("success", equalTo(isSuccess))
                .and().statusCode(statusCode);
    }

    @Test
    public void registerAccount(){
        if(email!=null)
            user.put("email", email);
        if(password!=null)
            user.put("password", password);
        if(name!=null)
            user.put("name", name);
        Account account = createUser(user);
        Response response = sendPostRequestRegisterUser(account);
        compareResult(response);
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
