package skyrexio.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class HomePage extends BasePage {

    private final By statisticsTitle = By.xpath("//h2[normalize-space()='Статистика']");
    private final By homeNavigationLink = By.xpath("//nav[@aria-label='Main']//a[normalize-space()='Главная']");
    private final By accountsNavigationLink = By.xpath("//nav[@aria-label='Main']//a[normalize-space()='Аккаунты']");
    private final By terminalNavigationButton = By.xpath("//nav[@aria-label='Main']//button[normalize-space()='Терминал']");
    private final By botsNavigationButton = By.xpath("//nav[@aria-label='Main']//button[normalize-space()='Боты']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Дождаться загрузки главной страницы")
    public HomePage waitUntilLoaded() {
        waitUntilVisible(homeNavigationLink);
        waitUntilVisible(statisticsTitle);
        return this;
    }

    @Step("Проверить успешную авторизацию и загрузку главной страницы")
    public HomePage shouldBeOpened() {
        Assert.assertEquals(driver.getCurrentUrl(), "https://test.skyrexio.com/home", "Открыт некорректный URL после авторизации");
        Assert.assertTrue(isVisible(homeNavigationLink), "Пункт меню 'Главная' не отображается");
        Assert.assertTrue(isVisible(accountsNavigationLink), "Пункт меню 'Аккаунты' не отображается");
        Assert.assertTrue(isVisible(terminalNavigationButton), "Пункт меню 'Терминал' не отображается");
        Assert.assertTrue(isVisible(botsNavigationButton), "Пункт меню 'Боты' не отображается");
        Assert.assertTrue(isVisible(statisticsTitle), "Блок 'Статистика' не отображается");
        return this;
    }
}
