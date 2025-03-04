package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import org.openqa.selenium.support.ui.Select;
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
    	System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver-linux64/chromedriver");
        
        // Optional Chrome options
    	ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Important for Docker
        options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in Docker
        options.addArguments("--headless=new"); // Headless mode (new improves stability)
        options.addArguments("--disable-gpu"); // Disable GPU acceleration
        options.addArguments("--remote-debugging-port=9222"); // Avoid conflicts
        options.addArguments("--disable-extensions"); // Prevent extension conflicts
        options.addArguments("--incognito"); // Force fresh profile every run
        options.addArguments("--disable-popup-blocking"); // Prevent issues with pop-ups

        // Unique user-data directory per test run (fixes the issue)
        String userDataDir = "/tmp/chrome-user-data-" + System.currentTimeMillis();
        options.addArguments("--user-data-dir=" + userDataDir);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeClass
    public void createNewUser() {
        try {
            // Navigate to registration page
            driver.get(BASE_URL + "register");

            // Generate unique email and password
            userEmail = generateUniqueEmail();
            userPassword = "Test@Pass123";

            // Fill registration form
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='FirstName']")))
                .sendKeys("Test");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='LastName']")))
                .sendKeys("User");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='Email']")))
                .sendKeys(userEmail);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='Password']")))
                .sendKeys(userPassword);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='ConfirmPassword']")))
                .sendKeys(userPassword);

            // Select gender (optional)
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='gender-male']")))
                .click();

            // Submit registration
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='register-button']")))
                .click();

            // Click continue after registration
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'register-continue-button')]")))
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
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='Email']")))
                .sendKeys(userEmail);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='Password']")))
                .sendKeys(userPassword);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'login-button')]")))
                .click();

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
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Digital downloads')]")))
                .click();

            // Read products from file located in `src/test/resources`
            try (BufferedReader reader = new BufferedReader(new FileReader(getClass().getClassLoader().getResource(dataFile).getFile()))) {
                String product;
                while ((product = reader.readLine()) != null) {
                    product = product.trim(); // Clean up any extra spaces
                    if (!product.isEmpty()) {
                        // Find all matching products by name
                        List<WebElement> productElements = driver.findElements(
                            By.xpath("//div[contains(@class, 'product-item') and .//a[contains(text(), '" + product + "')]]")
                        );

                        if (!productElements.isEmpty()) {
                        	Thread.sleep(1000);
                            // Iterate over the product elements and click on each "Add to Cart" button
                            for (WebElement productElement : productElements) {
                                WebElement addToCartButton = productElement.findElement(
                                    By.xpath(".//input[contains(@class, 'product-box-add-to-cart-button')]")
                                );
                                wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
                                System.out.println("Added to cart: " + product);
                            }
                        } else {
                            System.out.println("Product not found: " + product);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }

            // Open Shopping Cart and Checkout Process
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'ico-cart')]")))
                .click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='termsofservice']")))
                .click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"checkout\"]")))
                .click();
            if(driver.findElements(By.xpath("//*[@id=\"billing-address-select\"]")).size() > 0){
            	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'new-address-next-step-button')]")))
	            .click();
            }
            else if(driver.findElements(By.xpath("//*[@id=\"BillingNewAddress_CountryId\"]")).size() > 0){
            	Select dropdown = new Select(driver.findElement(By.xpath("//*[@id=\"BillingNewAddress_CountryId\"]")));
                dropdown.selectByIndex(1);
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"BillingNewAddress_City\"]")))
                .sendKeys("TestCity");
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"BillingNewAddress_Address1\"]")))
                .sendKeys("TestDaress");
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"BillingNewAddress_ZipPostalCode\"]")))
                .sendKeys("12345");
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"BillingNewAddress_PhoneNumber\"]")))
                .sendKeys("12345678");
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'new-address-next-step-button')]")))
                .click();
            }
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'payment-method-next-step-button')]")))
                .click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'payment-info-next-step-button')]")))
                .click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'confirm-order-next-step-button')]")))
                .click();

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