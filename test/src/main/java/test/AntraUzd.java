package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class AntraUzd {
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void main(String[] args) {
        setupDriver();
        
        try {
            testProgressBar();
            driver.manage().deleteAllCookies();
            testWebTables();
        } catch (Exception e) {
            System.out.println("Tests failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void setupDriver() {
        System.setProperty("webdriver.chrome.driver", "/home/aldas/chromedriver-linux64/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    private static void testProgressBar() {
        System.out.println("Starting Progress Bar Test...");
        try {
            driver.get("https://web.archive.org/web/20250112093337/https://demoqa.com/");
            
            WebElement widgetsCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'card')]//h5[text()='Widgets']")));
            widgetsCard.click();

            WebElement progressBarMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Progress Bar']")));
            progressBarMenu.click();
            
            WebElement startButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("startStopButton")));
            startButton.click();
            
            WebElement progressBar = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@role='progressbar']")));
            wait.until(ExpectedConditions.attributeToBe(progressBar, "aria-valuenow", "100"));
            
            WebElement resetButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("resetButton")));
            resetButton.click();
            
            String finalValue = progressBar.getDomAttribute("aria-valuenow");
            if ("0".equals(finalValue)) {
                System.out.println("Progress Bar Test passed! Progress bar reset to 0%");
            } else {
                System.out.println("Progress Bar Test failed! Progress bar value is: " + finalValue);
            }
            
        } catch (Exception e) {
            System.out.println("Progress Bar Test failed with error: " + e.getMessage());
            throw e;
        }
    }

    private static void testWebTables() {
        System.out.println("\nStarting Web Tables Test...");
        try {
            driver.get("https://web.archive.org/web/20250112093337/https://demoqa.com/");
            
            WebElement elementsCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'card')]//h5[text()='Elements']")));
            elementsCard.click();

            WebElement webTablesMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Web Tables']")));
            webTablesMenu.click();
            AtomicInteger recordIndex = new AtomicInteger(0);
            wait.until(driver -> {
                addNewRecord(recordIndex.getAndIncrement());

                WebElement totalPagesElement = driver.findElement(By.className("-totalPages"));
                return Integer.parseInt(totalPagesElement.getText()) == 2;
            });

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".-next button")));
            nextButton.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".-pageJump input[value='2']")));

            WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@title='Delete']")));
            deleteButton.click();

            wait.until(ExpectedConditions.textToBePresentInElementLocated(
            	    By.className("-totalPages"), "1"));

            WebElement nextButtonAfterDelete = driver.findElement(By.cssSelector(".-next button"));
            if (nextButtonAfterDelete.getDomAttribute("disabled") != null) {
                System.out.println("Web Tables Test passed! Pagination reduced to one page.");
            } else {
                System.out.println("Web Tables Test failed! Multiple pages still exist.");
            }

        } catch (Exception e) {
            System.out.println("Web Tables Test failed with error: " + e.getMessage());
            throw e;
        }
    }

    private static void addNewRecord(int index) {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.id("addNewRecordButton")));
        addButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.id("firstName"))).sendKeys("TestUser" + index);
        driver.findElement(By.id("lastName")).sendKeys("LastName" + index);
        driver.findElement(By.id("userEmail")).sendKeys("test" + index + "@example.com");
        driver.findElement(By.id("age")).sendKeys("3" + index);
        driver.findElement(By.id("salary")).sendKeys("5000" + index);
        driver.findElement(By.id("department")).sendKeys("Department" + index);

        driver.findElement(By.id("submit")).click();
    }
}