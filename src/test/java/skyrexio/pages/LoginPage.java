package skyrexio.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import user.User;

public class LoginPage extends BasePage {

    private final By pageTitle = By.xpath("//h1[normalize-space()='Вход']");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу входа")
    public LoginPage open() {
        openBaseUrl();
        waitUntilVisible(pageTitle);
        return this;
    }

    @Step("Ввести email пользователя: {email}")
    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    @Step("Ввести пароль пользователя")
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    @Step("Нажать кнопку входа")
    public HomePage submitLogin() {
        click(loginButton);
        return new HomePage(driver).waitUntilLoaded();
    }

    @Step("Авторизоваться пользователем")
    public HomePage loginAs(User user) {
        return enterEmail(user.login())
                .enterPassword(user.password())
                .submitLogin();
    }
}
