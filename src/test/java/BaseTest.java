import accountRegistr.Account;
import accountRegistr.TokenInfo;
import io.restassured.RestAssured;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    public void deleteUser(String token) {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .delete("/api/auth/user");
    }

    public String getAccessToken(String email, String password){
        HashMap<String, String> user = new HashMap<>();
        if(email!=null)
            user.put("email", email);
        if(password!=null)
            user.put("password", password);
        Account account = new Account(user);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(account)
                .when()
                .post("/api/auth/login")
                .as(TokenInfo.class).getAccessToken();
    }


}
