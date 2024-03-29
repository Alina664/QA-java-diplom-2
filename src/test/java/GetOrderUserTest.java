import account.Account;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;

@Epic("Получение заказа пользователя")
public class GetOrderUserTest extends BaseTest{
    Account account = new Account();
    ArrayList<String> user = account.registerNewAccountAndReturnLoginPassword();
    String tokenInfo;

    @Before
    public void newAccount() {
        tokenInfo = getAccessToken(user.get(0), user.get(1)).replace("Bearer ", "");
    }

    @Test
    @Description("Получаем список заказа пользователя без авторизации")
    public void checkGetOrderUserNotAuth(){
       Response response = sendGetRequestOrders();
        response.then().assertThat().body("message", equalTo("You should be authorised"))
                .and().body("success", equalTo(false))
                .and().statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @Test
    @Description("Получаем список заказа пользователя с авторизацией")
    public void checkGetOrderUserWithAuth(){
        Response response = sendGetRequestOrders(tokenInfo);
        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(HttpURLConnection.HTTP_OK);
    }

    @After
    public void deleteUser() {
        if (getAccessToken(user.get(0), user.get(1)) != null) {
            deleteUser(getAccessToken(user.get(0), user.get(1)).replace("Bearer ", ""));
        }
    }
}
