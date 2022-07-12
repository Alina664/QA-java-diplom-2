import account.Account;
import ingredients.Ingredients;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;

@Epic("Создание заказа")
public class CreateOrdersTest extends BaseTest{
    Account account = new Account();
    ArrayList<String> ingredient  = new ArrayList<>();
    Ingredients ingredientList = new Ingredients();
    String ingredientNotExist = "61c0c5a71d1f82001bdaa3121";
    ArrayList<String> user = account.registerNewAccountAndReturnLoginPassword();
    String tokenInfo;

    @Before
    public void getIngredient() {
        tokenInfo = getAccessToken(user.get(0), user.get(1)).replace("Bearer ", "");
        ingredientList = given()
                .header("Content-type", "application/json")
                .get("/api/ingredients").as(Ingredients.class);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингридиентами")
    @Description("Создаем заказ с авторизацией и ингридиентами")
    public void createOrderWithAuthAndIngredients(){
        ingredient.add(ingredientList.getData().get(0).get_id());
        ingredient.add(ingredientList.getData().get(1).get_id());
        Ingredients ingredients = new Ingredients(ingredient);
        Response response = sendPostRequestCreateOrder(ingredients, tokenInfo);
        compareResult(response, null, true, HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Создаем заказ без авторизации")
    public void createOrderNotAuth(){
        ingredient.add(ingredientList.getData().get(0).get_id());
        ingredient.add(ingredientList.getData().get(1).get_id());
        Ingredients ingredients = new Ingredients(ingredient);
        Response response = sendPostRequestCreateOrder(ingredients);
        compareResult(response, null, true, HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов")
    @Description("Создаем заказ без ингридиентов")
    public void createOrderNotIngredients(){
        Response response = sendPostRequestCreateOrder(tokenInfo);
        compareResult(response, "Ingredient ids must be provided", false, HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Создаем заказ с неверным хешем ингредиентов")
    public void createOrderNotRightHash(){
        ingredient.add(ingredientList.getData().get(0).get_id());
        ingredient.add(ingredientNotExist);
        Ingredients ingredients = new Ingredients(ingredient);
        Response response = sendPostRequestCreateOrder(ingredients ,tokenInfo);
        response.then().assertThat().statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    @After
    public void deleteUser(){
        if (getAccessToken(user.get(0), user.get(1))!=null) {
            deleteUser(getAccessToken(user.get(0), user.get(1)).replace("Bearer ",""));
        }
    }
}
