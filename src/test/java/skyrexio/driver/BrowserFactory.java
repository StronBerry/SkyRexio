package skyrexio.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import utils.PropertyReader;

public final class BrowserFactory {

    private BrowserFactory() {
    }

    public static WebDriver createDriver(String browser) {
        String normalizedBrowser = browser == null ? "chrome" : browser.trim().toLowerCase();

        return switch (normalizedBrowser) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "safari" -> new SafariDriver();
            default -> throw new RuntimeException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        if (isHeadless()) {
            options.addArguments("--headless=new");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        if (isHeadless()) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);
    }

    private static boolean isHeadless() {
        String systemValue = System.getProperty("headless");
        String configValue = PropertyReader.getProperty("browser.headless");
        String value = systemValue != null ? systemValue : configValue;

        return value == null || Boolean.parseBoolean(value);
    }
}
