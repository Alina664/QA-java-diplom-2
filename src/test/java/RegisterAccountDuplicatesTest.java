import account.Account;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
@Epic("Регистрация")
public class RegisterAccountDuplicatesTest extends BaseTest{

    private final String description;
    private final Account user;
    private final Account userDuplicates;
    private final boolean isSuccess;
    private final String message;
    private final int statusCode;

    public RegisterAccountDuplicatesTest(String description, Account user, Account userDuplicates, boolean isSuccess, String message, int statusCode) {
        this.description = description;
        this.user = user;
        this.userDuplicates = userDuplicates;
        this.isSuccess = isSuccess;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters(name = "{0}: {1}, {2}")
    public static Object[] getUserWithDifferentParameters() {
        return new Object[][]{
                {"Содание полного дубликата пользователя",new Account("alina22222@yandex.ru", "1234", "alina"),
                        new Account("alina22222@yandex.ru","1234","alina"), false, "User already exists", HttpURLConnection.HTTP_FORBIDDEN},
                {"Содание дубликата только по email и паролю",new Account("alina22222@yandex.ru","1234","alina1"),
                        new Account("alina22222@yandex.ru","1234", "alina"), false, "User already exists", HttpURLConnection.HTTP_FORBIDDEN},
                {"Содание дубликата только по email",new Account("alina22222@yandex.ru","12345",  "alina"),
                        new Account("alina22222@yandex.ru",  "1234", "alina"), false, "User already exists", HttpURLConnection.HTTP_FORBIDDEN},
        };
    }

    @Step("Compare result from request")
    public void compareResult(Response response) {
        response.then().assertThat().body("message", equalTo(message))
                .and().body("success", equalTo(isSuccess))
                .and().statusCode(statusCode);
    }

    @Test
    public void registerDuplicatesUser() {
        sendPostRequestRegisterUser(user);
        Response response = sendPostRequestRegisterUser(userDuplicates);
        compareResult(response);
    }

    @After
    public void deleteUser(){
        //удаляем созданного пользователя
        if (getAccessToken(user.getEmail(), user.getPassword())!=null) {
            deleteUser(getAccessToken(user.getEmail(), user.getPassword()).replace("Bearer ",""));
        }
        //удаляем пользователя-дубликата
        if (getAccessToken(userDuplicates.getEmail(), userDuplicates.getPassword())!=null) {
            deleteUser(getAccessToken(userDuplicates.getEmail(), userDuplicates.getPassword()).replace("Bearer ",""));
        }
    }
}