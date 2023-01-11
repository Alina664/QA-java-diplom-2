package account;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

@Data
public class Account {
    private String email;
    private String password;
    private String name;
    private HashMap<String, String> account = new HashMap<>();

    public Account(HashMap<String, String> account) {
        this.email = account.get("email");
        this.password = account.get("password");
        this.name = account.get("name");
    }

    public Account() {
    }

    public Account(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public ArrayList<String> registerNewAccountAndReturnLoginPassword() {
        // с помощью библиотеки RandomStringUtils генерируем логин
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        String userEmail = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        String userName = RandomStringUtils.randomAlphabetic(10);

        // создаём список, чтобы метод мог его вернуть
        ArrayList<String> loginPass = new ArrayList<>();

        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = "{\"email\":\"" + userEmail + "\","
                + "\"password\":\"" + userPassword + "\","
                + "\"name\":\"" + userName + "\"}";

        // отправляем запрос на регистрацию курьера и сохраняем ответ в переменную response класса Response
        TokenInfo account = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/register").as(TokenInfo.class);

        // если регистрация прошла успешно (код ответа 201), добавляем в список логин и пароль курьера
        if (account.isSuccess()) {
            loginPass.add(userEmail);
            loginPass.add(userPassword);
        }

        // возвращаем список
        return loginPass;
    }
}
