import accountRegistr.Account;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
@Epic("Авторизация")
public class LoginAccountTest extends BaseTest {
    private final String email;
    private final String password;
    private final boolean isSuccess;
    private final String message;
    private final int statusCode;
    Account account = new Account();
    ArrayList<String> loginPass = account.registerNewAccountAndReturnLoginPassword();

    public LoginAccountTest(String email, String password, boolean isSuccess, String message, int statusCode) {
        this.email = email;
        this.password = password;
        this.isSuccess = isSuccess;
        this.message = message;
        this.statusCode = statusCode;
    }
/*
    @Parameterized.Parameters
    public static Object[] getOrderWithDifferentColor() {
        return new Object[][]{
                {loginPass.get(0), loginPass.get(1), true, null, HttpURLConnection.HTTP_CREATED},
                {loginPass.get(0), null, false, "email or password are incorrect", HttpURLConnection.HTTP_UNAUTHORIZED},
        };
    }*/

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

    @After
    public void deleteUser() {
        if (getAccessToken(email, password) != null) {
            deleteUser(getAccessToken(email, password).replace("Bearer ", ""));
        }
    }
}
/*ArrayList<String> loginPass = new ArrayList<>();

    @Before
    public void newAccount() {
        //создаем курьера, с рандомными параметрами
        Account account = new Account();
        //записываем в переменную, чтобы использовать в тестах дальше
        loginPass = account.registerNewAccountAndReturnLoginPassword();
    }

    @Step("Send POST request /api/auth/login")
    public Response sendPostRequestLoginUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/login");
    }*/