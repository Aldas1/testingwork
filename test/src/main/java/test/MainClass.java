package test;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/home/aldas/chromedriver-linux64/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            driver.get("https://demowebshop.tricentis.com/");

            driver.findElement(By.linkText("Gift Cards")).click();

            List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));

            //FIXME perrasyti vienu XPath sakiniu
            String selectedProductId = null;
            for (WebElement product : products) {
                WebElement priceElement = product.findElement(By.cssSelector(".actual-price"));
                double price = Double.parseDouble(priceElement.getText().replaceAll("[^0-9.]", ""));

                if (price > 99) {
                    selectedProductId = product.getDomAttribute("data-productid");
                    product.findElement(By.cssSelector(".details h2 a")).click();
                    break;
                }
            }

            if (selectedProductId == null) {
                System.out.println("No valid gift card found!");
            }


            driver.findElement(By.id("giftcard_" + selectedProductId + "_RecipientName")).sendKeys("Aldas");
            driver.findElement(By.id("giftcard_" + selectedProductId + "_SenderName")).sendKeys("Aldo draugas");

            driver.findElement(By.id("addtocart_" + selectedProductId + "_EnteredQuantity")).clear();
            driver.findElement(By.id("addtocart_" + selectedProductId + "_EnteredQuantity")).sendKeys("5000");
            
            driver.findElement(By.id("add-to-cart-button-" + selectedProductId)).click();
            
            Thread.sleep(1000);

            driver.findElement(By.cssSelector(".add-to-wishlist-button")).click();

            driver.findElement(By.linkText("Jewelry")).click();

            driver.findElement(By.partialLinkText("Create Your Own Jewelry")).click();

            Select materialSelect = new Select(driver.findElement(By.id("product_attribute_71_9_15")));
            materialSelect.selectByVisibleText("Silver (1 mm)");

            WebElement lengthInput = driver.findElement(By.id("product_attribute_71_10_16"));
            lengthInput.clear();
            lengthInput.sendKeys("80");

            WebElement pendantRadio = driver.findElement(By.xpath("//label[contains(text(),'Star')]/preceding-sibling::input[@type='radio']"));
            if (!pendantRadio.isSelected()) {
                pendantRadio.click();
            }

            WebElement qtyField = driver.findElement(By.id("addtocart_71_EnteredQuantity"));
            qtyField.clear();
            qtyField.sendKeys("26");

            driver.findElement(By.cssSelector(".add-to-cart-button")).click();
            
            Thread.sleep(1000);
            
            driver.findElement(By.cssSelector(".add-to-wishlist-button")).click();

            driver.findElement(By.xpath("//a[contains(@class, 'ico-wishlist')]")).click();

            List<WebElement> checkboxes = driver.findElements(By.cssSelector(".add-to-cart input[type='checkbox']"));
            for (WebElement checkbox : checkboxes) {
                if (!checkbox.isSelected()) {
                    checkbox.click();
                }
            }

            driver.findElement(By.name("addtocartbutton")).click();

            WebElement subTotalElement = driver.findElement(By.cssSelector(".cart-total-right span"));
            String subTotal = subTotalElement.getText().replaceAll("[^0-9.]", "");

            if (subTotal.equals("1002600.00")) {
                System.out.println("Test Passed: Sub-Total is correct.");
            } else {
                System.out.println("Test Failed: Expected 1002600.00 but got " + subTotal);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}

