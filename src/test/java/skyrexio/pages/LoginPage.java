package skyrexio.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.testng.Assert;
import user.User;

import java.util.Arrays;

public class LoginPage extends BasePage {

    private final By pageTitle = By.xpath("//h1[normalize-space()='Вход']");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By emailInputContainer = By.xpath("//input[@id='email']/parent::*");
    private final By passwordInputContainer = By.xpath("//input[@id='password']/parent::*");

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

    @Step("Заполнить форму входа email: {email}, password: {password}")
    public LoginPage fillLoginForm(String email, String password) {
        if (email != null) {
            enterEmail(email);
        }
        if (password != null) {
            enterPassword(password);
        }
        return this;
    }

    @Step("Активировать валидацию формы входа")
    public LoginPage triggerLoginFormValidation() {
        waitUntilVisible(emailInput).click();
        waitUntilVisible(passwordInput).click();
        waitUntilVisible(pageTitle).click();
        return this;
    }

    @Step("Нажать кнопку входа")
    public HomePage submitLogin() {
        click(loginButton);
        return new HomePage(driver).waitUntilLoaded();
    }

    @Step("Нажать кнопку входа с невалидными учетными данными")
    public LoginPage submitInvalidLogin() {
        click(loginButton);
        return this;
    }

    @Step("Навести курсор на кнопку входа")
    public LoginPage hoverLoginButton() {
        hover(loginButton);
        return this;
    }

    @Step("Проверить, что кнопка входа неактивна")
    public LoginPage shouldHaveDisabledLoginButton() {
        Assert.assertTrue(isDisabled(loginButton), "Кнопка 'Войти' должна быть неактивна");
        return this;
    }

    @Step("Проверить сообщения валидации: {expectedMessages}")
    public LoginPage shouldDisplayValidationMessages(String... expectedMessages) {
        Arrays.stream(expectedMessages).forEach(message -> {
            By messageLocator = By.xpath("//*[normalize-space()='" + message + "']");
            Assert.assertTrue(waitUntilVisible(messageLocator).isDisplayed(), "Сообщение валидации не отображается: " + message);
        });
        return this;
    }

    @Step("Проверить, что поле email подсвечено ошибкой")
    public LoginPage shouldHighlightEmailAsInvalid() {
        shouldDisplayInvalidFieldColor(emailInputContainer);
        return this;
    }

    @Step("Проверить, что поле пароля подсвечено ошибкой")
    public LoginPage shouldHighlightPasswordAsInvalid() {
        shouldDisplayInvalidFieldColor(passwordInputContainer);
        return this;
    }

    @Step("Проверить toast об ошибке авторизации")
    public LoginPage shouldDisplayInvalidCredentialsMessage() {
        By toast = By.xpath("//*[contains(normalize-space(), 'неверный email или пароль') or contains(normalize-space(), 'Неверный email или пароль')]");
        Assert.assertTrue(waitUntilVisible(toast).isDisplayed(), "Toast 'неверный email или пароль' не отображается");
        return this;
    }

    private void shouldDisplayInvalidFieldColor(By locator) {
        WebElement fieldContainer = waitUntilPresent(locator);
        String borderColorHex = Color.fromString(fieldContainer.getCssValue("border-color")).asHex();

        Assert.assertTrue(
                isRedHexColor(borderColorHex),
                "Поле должно быть подсвечено красным. Фактический цвет border-color: " + borderColorHex
        );
    }

    private boolean isRedHexColor(String hexColor) {
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

        return red >= 200 && green <= 100 && blue <= 100;
    }

    @Step("Авторизоваться пользователем")
    public HomePage loginAs(User user) {
        return enterEmail(user.login())
                .enterPassword(user.password())
                .submitLogin();
    }
}
