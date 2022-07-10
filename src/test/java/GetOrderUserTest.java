import account.Account;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
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

    @Step("Send Get request auth /api/orders")
    public Response sendGetRequestOrders(String token) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
                .when()
                .get("/api/orders");
    }

    @Step("Send Get request not auth /api/orders")
    public Response sendGetRequestOrders() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/orders");
    }

    @Test
    @Description("Получаем список заказа пользователя без авторизации")
    public void getOrderUserNotAuth(){
       Response response = sendGetRequestOrders();
        response.then().assertThat().body("message", equalTo("You should be authorised"))
                .and().body("success", equalTo(false))
                .and().statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @Test
    @Description("Получаем список заказа пользователя с авторизацией")
    public void getOrderUserWithAuth(){
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
