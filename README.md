# selenium-auto-wait
selenium-auto-wait automatically manages all weblement waits and makes you to write wait free selenium tests.

## Features
1. Waits till element found when using findElement method. Unlike Webdriver's implicit wait method, you can control this behaviour using annotations.
2. Waits for the element to become intractable before performing any action on it.
3. If you using pageobject model and wants to ignore auto wait for certain methods, you can use `@IgnoreWait` annotation.
4. You can use `@WaitProperties` annotation to control the behaviour of auto wait for a specific method in the page object method.

## Installation

### Maven

```xml
<!-- https://mvnrepository.com/artifact/io.github.testervippro/selenium-auto-wait -->
<dependency>
    <groupId>io.github.testervippro</groupId>
    <artifactId>selenium-auto-wait</artifactId>
    <version>1.0.2</version>
</dependency>
```


### Maven
```xml
<dependency>
   <groupId>org.seleniumhq.selenium</groupId>
   <artifactId>selenium-java</artifactId>
   <version>4.28.0</version>
   <exclusions>
      <exclusion>
         <groupId>net.bytebuddy</groupId>
         <artifactId>byte-buddy</artifactId>
      </exclusion>
   </exclusions>
</dependency>
```
## Quickstart

Initialize the wait plugin using
```java
SeleniumWaitOptions options = SeleniumWaitOptions.builder()
                .parseAnnotations(true)
                .defaultWaitTime(Duration.ofSeconds(30))
                .build();
SeleniumWaitPlugin<ChromeDriver> seleniumWaitPlugin = new SeleniumWaitPlugin<ChromeDriver>(new ChromeDriver(), options);
WebDriver driver =  seleniumWaitPlugin.getDriver();
```
That's it. Now the driver object can be used in the test.

## Available options

* `defaultWaitTime` (Duration) - Used as a timeout while waiting for element.
* `excludedMethods` (List<String>) - List of method names that will be ignored in auto wait.
* `parseAnnotations` (Boolean) - If true, the plugin will look for `@IgnoreWait` or `@WaitProperties` annotation and manages wait based on it. Default value is `false`
* `packageToBeParsed` (String) - if `parseAnnotations` is true, the plugin will parse all the methods from the stacktrace looking for annotations. If you want to search the annotations only on a specific package then you can mention it here.

## Annotation Example:

```java

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
import org.testng.annotations.Test;

import java.time.Duration;

public class WaitTest {

    public WebDriver getDriver() {
        SeleniumWaitOptions options = SeleniumWaitOptions.builder()
                .parseAnnotations(true)
                .defaultWaitTime(Duration.ofSeconds(30))
                .build();

        SeleniumWaitPlugin seleniumWaitPlugin = new SeleniumWaitPlugin(new ChromeDriver(), options);
        return seleniumWaitPlugin.getDriver();
    }

    @Test
    public void test() {
        WebDriver driver = getDriver();
        searchAmazon(driver);
        searchAmazonWithoutWait(driver);
        searchAmazonWithCustomWait(driver);
    }

    public void searchAmazon(WebDriver driver) {
        driver.get("https://www.amazon.in");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.ENTER);
        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
        driver.findElement(By.id("add-to-cart-button")).click();
        driver.findElement(By.id("attach-view-cart-button-form")).click();
    }

    @IgnoreWait // will not automatically wait for any element interaction
    public void searchAmazonWithoutWait(WebDriver driver) {
        driver.get("https://www.amazon.in");
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.presenceOfElementLocated(By.id("twotabsearchtextbox")));
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7", Keys.ENTER);
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("OnePlus 7 Pro")));
        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-button")));
        driver.findElement(By.id("add-to-cart-button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.elementToBeClickable(By.id("attach-view-cart-button-form")));
        driver.findElement(By.id("attach-view-cart-button-form")).click();
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
        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
        driver.findElement(By.id("add-to-cart-button")).click();
        driver.findElement(By.id("attach-view-cart-button-form")).click();
    }
}

```


