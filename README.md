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
<dependency>
    <groupId>io.github.testervippro</groupId>
    <artifactId>selenium-auto-wait</artifactId>
    <version>1.1.1</version>
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

```


