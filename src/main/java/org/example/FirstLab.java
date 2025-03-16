package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class FirstLab {
    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.nmu.org.ua/ua";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));

        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(baseUrl);
    }

    @Test
    public void testHeaderExists() {
        WebElement header = chromeDriver.findElement(By.id("masthead"));
        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnForStudent() {
        WebElement forStudentButton = chromeDriver.findElement(By.xpath("/html/body/div[1]/footer/div[1]/div/div/div[1]/div/div/nav/div/ul/li[1]/a"));

        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "https://old.nmu.org.ua/ua/content/students/";
        chromeDriver.get(studentPageUrl);

        WebElement searchField = chromeDriver.findElement(By.tagName("input"));

        Assert.assertNotNull(searchField);

        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")) +
                String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition: (%d, %d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize: %d%d", searchField.getSize().height, searchField.getSize().width)
        );

        String inputValue = "I need info";
        searchField.sendKeys(inputValue);

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), studentPageUrl);
    }

    @Test
    public void testSlider() {
        WebElement nextButton = chromeDriver.findElement(By.className("swiper-button-next"));
        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("div.swiper-button-next"));

        Assert.assertEquals(nextButton, nextButtonByCss);

        WebElement currentSlide = chromeDriver.findElement(By.className("swiper-wrapper"));
        String currentSlideStyle = currentSlide.getAttribute("style");

        nextButton.click();

        WebElement nextSlide = chromeDriver.findElement(By.className("swiper-wrapper"));
        String nextSlideStyle = nextSlide.getAttribute("style");

        Assert.assertNotEquals(currentSlideStyle, nextSlideStyle);

        WebElement previousButton = chromeDriver.findElement(By.className("swiper-button-prev"));
        previousButton.click();

        WebElement prevSlide = chromeDriver.findElement(By.className("swiper-wrapper"));
        String prevSlideStyle = prevSlide.getAttribute("style");

        Assert.assertNotEquals(currentSlideStyle, prevSlideStyle);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (chromeDriver != null) {
            chromeDriver.quit();
        }
    }
}
