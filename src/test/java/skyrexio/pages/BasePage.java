package skyrexio.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PropertyReader;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected static final String BASE_URL = PropertyReader.getProperty("skyrexio.url");
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    @Step("Открыть базовый URL приложения")
    public void openBaseUrl() {
        driver.get(BASE_URL);
    }

    @Step("Дождаться отображения элемента: {locator}")
    protected WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @Step("Дождаться кликабельности элемента: {locator}")
    protected WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Дождаться отображения всех элементов: {locator}")
    protected List<WebElement> waitUntilAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    @Step("Найти все элементы: {locator}")
    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    @Step("Кликнуть по элементу: {locator}")
    protected void click(By locator) {
        waitUntilClickable(locator).click();
    }

    @Step("Навести курсор на элемент: {locator}")
    protected void hover(By locator) {
        new Actions(driver).moveToElement(waitUntilVisible(locator)).perform();
    }

    @Step("Ввести значение в элемент: {locator}")
    protected void type(By locator, String value) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    @Step("Получить текст элемента: {locator}")
    protected String getText(By locator) {
        return waitUntilVisible(locator).getText();
    }

    @Step("Проверить отображение элемента: {locator}")
    protected boolean isVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    @Step("Проверить, что элемент неактивен: {locator}")
    protected boolean isDisabled(By locator) {
        return !waitUntilVisible(locator).isEnabled();
    }
}
