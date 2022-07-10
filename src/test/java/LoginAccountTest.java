import account.Account;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
@Epic("Авторизация")
public class LoginAccountTest extends BaseTest {
    private final String description;
    private final String email;
    private final String password;
    private final boolean isSuccess;
    private final String message;
    private final int statusCode;
    static Account account = new Account();
    static ArrayList<String> user = account.registerNewAccountAndReturnLoginPassword();

    public LoginAccountTest(String description, String email, String password, boolean isSuccess, String message, int statusCode) {
        this.description = description;
        this.email = email;
        this.password = password;
        this.isSuccess = isSuccess;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters(name = "{0}: {1}, {2}")
    public static Object[] getAccount() {
        return new Object[][]{
                {"Пользователь существует",user.get(0), user.get(1), true, null, HttpURLConnection.HTTP_OK},
                {"Пользователь с таким паролем и логином не существует",user.get(0), user.get(0), false, "email or password are incorrect", HttpURLConnection.HTTP_UNAUTHORIZED},
        };
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

    @Step("Compare result from request")
    public void compareResult(Response response) {
        response.then().assertThat().body("message", equalTo(message))
                .and().body("success", equalTo(isSuccess))
                .and().statusCode(statusCode);
    }

    @Test
    public void loginUser() {
        Account account = new Account(email, password);
        Response response = sendPostRequestAuthUser(account);
        compareResult(response);
    }

    @After
    public void deleteUser() {
        if (getAccessToken(user.get(0), user.get(1)) != null) {
            deleteUser(getAccessToken(user.get(0), user.get(1)).replace("Bearer ", ""));
        }
    }
}
