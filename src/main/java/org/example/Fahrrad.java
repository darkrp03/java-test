package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class Fahrrad {
    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.fahrrad-xxl.de/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));

        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(baseUrl);
    }

    @Test(priority = 1)
    public void testToAcceptCookie() throws InterruptedException {
        Dimension windowSize = chromeDriver.manage().window().getSize();
        int maxX = windowSize.width;
        int maxY = windowSize.height;

        Actions actions = new Actions(chromeDriver);
        Random rand = new Random();

        int randomX = rand.nextInt(maxX);
        int randomY = rand.nextInt(maxY);

        actions.moveByOffset(randomX, randomY).perform();

        Thread.sleep(500);

        WebElement cookieWindow = chromeDriver.findElement(By.className("fxxl-cookie-mainmodal"));
        WebElement acceptCookieButton = chromeDriver.findElement(By.className("fxxl-cookie-mainmodal__button--acceptall"));

        String cookieStyleBefore = cookieWindow.getAttribute("style");
        acceptCookieButton.click();
        String cookieStyleAfter = cookieWindow.getAttribute("style");

        Assert.assertNotEquals(cookieStyleBefore, cookieStyleAfter);
    }

    @Test(priority = 2)
    public void testTheProductsAvailability() {
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(15));
        WebElement closeSpamButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fxxl-newsletter-popup__button--close")));

        closeSpamButton.click();

        WebElement iconSearch = chromeDriver.findElement(By.className("fxxl-header__icon--search"));

        iconSearch.click();

        WebElement inputSearch = chromeDriver.findElement(By.id("fxxl-header-search-container-field"));
        inputSearch.sendKeys("Bike");
        inputSearch.sendKeys(Keys.ENTER);

        List<WebElement> productsSliders = chromeDriver.findElements(By.className("fxxl-element-artikel--slider"));

        Assert.assertNotEquals(productsSliders.size(), 0);
    }

    @Test(priority = 3)
    public void testToGetPlaceholder() {
        WebElement div = chromeDriver.findElement(By.id("fxxl-marketingspot-placeholder"));

        Assert.assertNotNull(div);
    }

    @Test(priority = 4)
    public void testTheTabWorking() {
        WebElement tabButton = chromeDriver.findElement(By.id("fxxl-tabs-1__menu2"));
        WebElement indicator = chromeDriver.findElement(By.cssSelector("li.indicator"));

        String beforeIndicatorStyle = indicator.getAttribute("style");

        tabButton.click();

        String afterIndicatorStyle = indicator.getAttribute("style");

        Assert.assertNotEquals(beforeIndicatorStyle, afterIndicatorStyle);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (chromeDriver != null) {
            chromeDriver.quit();
        }
    }
}
