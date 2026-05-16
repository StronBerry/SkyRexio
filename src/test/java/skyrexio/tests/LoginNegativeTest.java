package skyrexio.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import skyrexio.base.BaseTest;
import skyrexio.pages.LoginPage;
import user.User;
import user.UserFactory;

@Epic("Skyrexio")
@Feature("Авторизация")
@Owner("StronBerry")
public class LoginNegativeTest extends BaseTest {

    @DataProvider(name = "invalidLoginFormData")
    public Object[][] invalidLoginFormData() {
        return new Object[][]{
                {
                        null,
                        null,
                        true,
                        true,
                        new String[]{"Введите e-mail", "Введите пароль"}
                },
                {
                        "abc123",
                        "12345678",
                        true,
                        false,
                        new String[]{"Некорректный формат e-mail"}
                },
                {
                        "тест@example.com",
                        "12345678",
                        true,
                        false,
                        new String[]{"Email может содержать только латинские буквы и стандартные символы"}
                },
                {
                        "abc123@abc.ab",
                        "1234567",
                        false,
                        true,
                        new String[]{}
                },
                {
                        "abc123@abc.ab",
                        "пароль123",
                        false,
                        true,
                        new String[]{"Пароль должен содержать только латинские буквы, цифры и спецсимволы (!@#$%^&* и т.д.)"}
                },
                {
                        "abc123",
                        null,
                        true,
                        true,
                        new String[]{"Некорректный формат e-mail", "Введите пароль"}
                }
        };
    }

    @Test(
            description = "Негативная валидация формы входа при некорректном заполнении",
            dataProvider = "invalidLoginFormData"
    )
    @Story("Пользователь видит ошибки валидации формы входа")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("LOGIN_002")
    public void loginFormShowsValidationMessages(
            String email,
            String password,
            boolean shouldHighlightEmail,
            boolean shouldHighlightPassword,
            String[] expectedMessages
    ) {
        LoginPage loginPage = new LoginPage(driver)
                .open()
                .fillLoginForm(email, password)
                .triggerLoginFormValidation()
                .shouldHaveDisabledLoginButton()
                .hoverLoginButton()
                .shouldDisplayValidationMessages(expectedMessages);

        if (shouldHighlightEmail) {
            loginPage.shouldHighlightEmailAsInvalid();
        }
        if (shouldHighlightPassword) {
            loginPage.shouldHighlightPasswordAsInvalid();
        }
    }

    @Test(description = "Ошибка авторизации при валидном email и неверном пароле")
    @Story("Пользователь видит ошибку при неверных учетных данных")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("LOGIN_003")
    public void userCannotLoginWithValidEmailAndWrongPassword() {
        User user = UserFactory.validUser();

        new LoginPage(driver)
                .open()
                .enterEmail(user.login())
                .enterPassword("12345678")
                .submitInvalidLogin()
                .shouldDisplayInvalidCredentialsMessage();
    }
}
