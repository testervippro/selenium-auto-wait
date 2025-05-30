package io.github.sudharsan_selvaraj.amazon;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.sudharsan_selvaraj.autowait.SeleniumWaitOptions;
import io.github.sudharsan_selvaraj.autowait.SeleniumWaitPlugin;
import io.github.sudharsan_selvaraj.autowait.annotations.IgnoreWait;
import io.github.sudharsan_selvaraj.autowait.annotations.WaitProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.Duration;

public class AmazonTest {

    WebDriver driver;
    @BeforeSuite
    public void setup() {
        WebDriverManager.chromedriver().setup();

    }
    public WebDriver getDriver() {
        SeleniumWaitOptions options = SeleniumWaitOptions.builder()
                .parseAnnotations(true)
                .defaultWaitTime(Duration.ofSeconds(30))
                .packageToBeParsed("io.github.sudharsan_selvaraj")
                .build();
        SeleniumWaitPlugin<ChromeDriver> seleniumWaitPlugin = new SeleniumWaitPlugin<ChromeDriver>(new ChromeDriver(), options);
        return seleniumWaitPlugin.getDriver();
    }

    @Test
    public void test() {
        WebDriver driver = getDriver();

        runTestWthAutoWait(driver);
        runTestIgnoreWait(driver);
        searchAmazonWithCustomWait(driver);

        // driver.quit();
    }

    public void runTestWthAutoWait(WebDriver driver) {
        driver.get("https://www.amazon.in");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.ENTER);
        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
        driver.findElement(By.cssSelector("#add-to-cart-button")).click();

        var expect ="Amazon.in Shopping Cart";
        Assert.assertTrue(driver.getTitle().toLowerCase().equalsIgnoreCase(expect));
    }


    @IgnoreWait
    public void runTestIgnoreWait(WebDriver driver) {
        driver.get("https://www.amazon.in");
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(ExpectedConditions.presenceOfElementLocated(By.id("twotabsearchtextbox")));

        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7", Keys.ENTER);

        new WebDriverWait(driver, Duration.ofSeconds(60))
                .until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("OnePlus 7 Pro")));

        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[2]);

        new WebDriverWait(driver, Duration.ofSeconds(60))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#add-to-cart-button")));

        driver.findElement(By.cssSelector("#add-to-cart-button")).click();

        var expect ="Amazon.in Shopping Cart";
        Assert.assertTrue(driver.getTitle().toLowerCase().equalsIgnoreCase(expect));
    }


    @WaitProperties(
            timeout = 10, //custom wait time in seconds
            exclude = {"sendKeys"} // will not automatically wait for sendKeys method
    )
    public void searchAmazonWithCustomWait(WebDriver driver) {
        driver.get("https://www.amazon.in");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.ENTER);
        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[3]);
        driver.findElement(By.id("add-to-cart-button")).click();
        var expect ="Amazon.in Shopping Cart";
        Assert.assertTrue(driver.getTitle().toLowerCase().equalsIgnoreCase(expect));

    }
}
