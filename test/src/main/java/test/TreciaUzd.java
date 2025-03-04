package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TreciaUzd {
    private static final String BASE_URL = "https://demowebshop.tricentis.com/";
    private WebDriver driver;
    private WebDriverWait wait;

    // Unique user credentials
    private String userEmail;
    private String userPassword;

    @BeforeSuite
    public void setupChromeDriver() {
        // Set up ChromeDriver path
    	System.setProperty("webdriver.chrome.driver", "/home/aldas/chromedriver-linux64/chromedriver");
        
        // Optional Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
    }

    @BeforeClass
    public void createNewUser() {
        // Initialize WebDriver for user registration
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate to registration page
            driver.get(BASE_URL + "register");

            // Generate unique email and password
            userEmail = generateUniqueEmail();
            userPassword = "Test@Pass123";

            // Fill registration form
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FirstName")))
                .sendKeys("Test");
            driver.findElement(By.id("LastName")).sendKeys("User");
            driver.findElement(By.id("Email")).sendKeys(userEmail);
            driver.findElement(By.id("Password")).sendKeys(userPassword);
            driver.findElement(By.id("ConfirmPassword")).sendKeys(userPassword);

            // Select gender (optional)
            driver.findElement(By.id("gender-male")).click();

            // Submit registration
            driver.findElement(By.id("register-button")).click();

            // Click continue after registration
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".button-1.register-continue-button")))
                .click();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    @BeforeMethod
    public void loginBeforeTest() {
        // Initialize WebDriver for login
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate to login page
            driver.get(BASE_URL + "login");

            // Login with created credentials
            driver.findElement(By.id("Email")).sendKeys(userEmail);
            driver.findElement(By.id("Password")).sendKeys(userPassword);
            driver.findElement(By.cssSelector(".button-1.login-button")).click();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void testPurchaseFromData1() {
        purchaseDigitalDownloads("data1.txt");
    }

    @Test(priority = 2)
    public void testPurchaseFromData2() {
        purchaseDigitalDownloads("data2.txt");
    }

    private void purchaseDigitalDownloads(String dataFile) {
        try {
            // Navigate to Digital Downloads
            driver.findElement(By.linkText("Digital downloads")).click();

            // Read products from file
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                String product;
                while ((product = reader.readLine()) != null) {
                    // Find and add product to cart
                    List<WebElement> productElements = driver.findElements(
                        By.xpath("//div[contains(@class, 'product-item') and .//a[contains(text(), '" + product + "')]]")
                    );
                    
                    if (!productElements.isEmpty()) {
                        productElements.get(0).findElement(By.cssSelector(".button-2.product-box-add-to-cart-button")).click();
                    }
                }
            }

            // Open Shopping Cart
            driver.findElement(By.cssSelector(".ico-cart")).click();

            // Proceed to Checkout
            driver.findElement(By.id("termsofservice")).click();
            driver.findElement(By.id("checkout")).click();

            // Billing Address (use existing or first address)
            driver.findElement(By.cssSelector(".button-1.new-address-next-step-button")).click();

            // Payment Method
            driver.findElement(By.cssSelector(".button-1.payment-method-next-step-button")).click();

            // Payment Information
            driver.findElement(By.cssSelector(".button-1.payment-info-next-step-button")).click();

            // Confirm Order
            driver.findElement(By.cssSelector(".button-1.confirm-order-next-step-button")).click();

            // Verify order confirmation
            WebElement orderConfirmation = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class, 'section order-completed')]")
                )
            );

            assert orderConfirmation.isDisplayed() : "Order not placed successfully";

        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Test failed: " + e.getMessage();
        }
    }

    @AfterMethod
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Utility method to generate unique email
    private String generateUniqueEmail() {
        return "testuser_" + new Random().nextInt(10000) + "@example.com";
    }
}