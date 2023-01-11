import account.Account;
import account.TokenInfo;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

@Epic("Изменение пользователя")
public class EditAccountTest extends BaseTest{
    HashMap<String, String> logPass = new HashMap<>();
    String oldEmail = "alina2222234@yandex.ru";
    String oldPassword = "1234";
    String oldName = "alina";
    String newEmail = "newEmail123@yandex.ru";
    String newPassword = "newPassword";
    String newName = "newName";
    String duplicateEmail = "dublicates123@yandex.ru";
    TokenInfo tokenInfo;
    Account account;
    Account accountDuplicate;

    @Before
    public void newAccount() {
        account = new Account(oldEmail, oldPassword, oldName);
        tokenInfo = sendPostRequestRegisterUser(account).as(TokenInfo.class);

        accountDuplicate = new Account(duplicateEmail, oldPassword, oldName);
        sendPostRequestRegisterUser(accountDuplicate);
    }

    @Step("Compare result from request not auth")
    public void compareResultWithNotAuth(Response response) {
        response.then().assertThat().body("message", equalTo("You should be authorised"))
                .and().body("success", equalTo(false))
                .and().statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @Step("Compare result from request with email exist user")
    public void compareResultWithExistUser(Response response) {
        response.then().assertThat().body("message", equalTo("User with such email already exists"))
                .and().body("success", equalTo(false))
                .and().statusCode(HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test
    @Description("Меняем имя на другое с авторизацией")
    public void editAccountFromName(){
        logPass.put("name",newName);
        Account editAccount = new Account(logPass);
        TokenInfo response = sendPatchRequestEditUser(editAccount, tokenInfo.getAccessToken().replace("Bearer ", "")).as(TokenInfo.class);
        if(response.isSuccess()) {
            account.setName(newName);
        }
        Assert.assertEquals("Сравниваем новое и старое имя", newName,response.getUser().getName());
    }

    @Test
    @Description("Меняем имя на другое без авторизации")
    public void editAccountFromNameNotAuth(){
        logPass.put("name",newName);
        Account editAccount = new Account(logPass);
        Response response = sendPatchRequestEditUser(editAccount);
        compareResultWithNotAuth(response);
    }

    @Test
    @Description("Меняем пароль на другое без авторизации")
    public void editAccountFromPasswordNotAuth(){
        logPass.put("password",newPassword);
        Account editAccount = new Account(logPass);
        Response response = sendPatchRequestEditUser(editAccount);
        compareResultWithNotAuth(response);
    }

    @Test
    @Description("Меняем email на другое без авторизации")
    public void editAccountFromEmailNotAuth(){
        logPass.put("email",newEmail);
        Account editAccount = new Account(logPass);
        Response response = sendPatchRequestEditUser(editAccount);
        compareResultWithNotAuth(response);
    }

    @Test
    @Description("Меняем пароль на другой")
    public void editAccountFromPassword(){
        logPass.put("password",newPassword);
        account.setPassword(newPassword);
        Account editAccount = new Account(logPass);
        TokenInfo response = sendPatchRequestEditUser(editAccount, tokenInfo.getAccessToken().replace("Bearer ", "")).as(TokenInfo.class);
        Assert.assertNotNull("Проверяем авторизацию с новым паролем", getAccessToken(account.getEmail(), account.getPassword()));
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    @Description("Меняем email на другой")
    public void editAccountFromEmail(){
        logPass.put("email",newEmail);
        account.setEmail(newEmail);
        Account editAccount = new Account(logPass);
        TokenInfo response = sendPatchRequestEditUser(editAccount, tokenInfo.getAccessToken().replace("Bearer ", "")).as(TokenInfo.class);
        Assert.assertNotNull("Проверяем авторизацию с новым email", getAccessToken(account.getEmail(), account.getPassword()));
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    @Description("Меняем email на другой существующий")
    public void editAccountFromEmailExist(){
        logPass.put("email",duplicateEmail);
        account.setEmail(duplicateEmail);
        Account editAccount = new Account(logPass);
        Response response = sendPatchRequestEditUser(editAccount, tokenInfo.getAccessToken().replace("Bearer ", ""));
        compareResultWithExistUser(response);
    }

    @After
    public void deleteUser() {
        if (getAccessToken(account.getEmail(), account.getPassword()) != null) {
            deleteUser(getAccessToken(account.getEmail(), account.getPassword()).replace("Bearer ", ""));
        }
        if (getAccessToken(accountDuplicate.getEmail(), accountDuplicate.getPassword()) != null) {
            deleteUser(getAccessToken(accountDuplicate.getEmail(), accountDuplicate.getPassword()).replace("Bearer ", ""));
        }
    }
}
