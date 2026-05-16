package skyrexio.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import skyrexio.base.BaseTest;
import skyrexio.pages.LoginPage;
import user.User;
import user.UserFactory;

@Epic("Skyrexio")
@Feature("Авторизация")
@Owner("StronBerry")
public class LoginPositiveTest extends BaseTest {

    @Test(description = "Успешная авторизация с переходом на главную страницу")
    @Story("Пользователь входит в приложение с валидными учетными данными")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("LOGIN_001")
    public void userCanLoginWithValidCredentials() {
        User user = UserFactory.validUser();

        new LoginPage(driver)
                .open()
                .loginAs(user)
                .shouldBeOpened();
    }
}
