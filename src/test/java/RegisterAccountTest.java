import account.Account;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.HashMap;

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
                {"Успешная регистрация", RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), true, null, HttpURLConnection.HTTP_OK},
                {"Регистрация без email",null, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), false, "Email, password and name are required fields", HttpURLConnection.HTTP_FORBIDDEN},
                {"Регистрация без пароля", RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", null, RandomStringUtils.randomAlphabetic(10), false, "Email, password and name are required fields", HttpURLConnection.HTTP_FORBIDDEN},
                {"Регистрация без имени", RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", RandomStringUtils.randomAlphabetic(10), null, false, "Email, password and name are required fields", HttpURLConnection.HTTP_FORBIDDEN},
        };
    }

    @Step("Create user")
    public Account createUser(HashMap<String, String> account) {
        return new Account(account);
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
        compareResult(response, message, isSuccess, statusCode);
    }

    @After
    public void deleteUser(){
        if (getAccessToken(email, password)!=null) {
            deleteUser(getAccessToken(email, password).replace("Bearer ",""));
        }
    }
}
