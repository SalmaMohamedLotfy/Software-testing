package Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testng.asserts.SoftAssert;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;


import java.time.Duration;
public class testing {
    WebDriver driver;
    WebDriverWait wait;



    @BeforeTest
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\slotfy\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    }


    public void addProductToCart() {
        driver.get("https://www.demoblaze.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Place Order')]"))).click();
    }

    @Test(priority = 1, description = "Verify 'Place Order' button is disabled or not clickable when cart is empty")
    public void testPlaceOrderButtonDisabledWhenCartEmpty() {
        driver.get("https://www.demoblaze.com/");

        // Go to cart
        driver.findElement(By.id("cartur")).click();

        // Wait for the cart to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));

        // Remove all items if any exist
        List<WebElement> rows = driver.findElements(By.cssSelector("#tbodyid > tr"));
        while (!rows.isEmpty()) {
            WebElement row = rows.get(0);
            row.findElement(By.linkText("Delete")).click();
            wait.until(ExpectedConditions.stalenessOf(row));
            rows = driver.findElements(By.cssSelector("#tbodyid > tr")); // Refresh the list
        }

        // Try to locate the 'Place Order' button
        WebElement placeOrderBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Place Order']")));

        // Check if it's clickable
        boolean isClickable;
        try {
            wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            isClickable = true;
        } catch (TimeoutException e) {
            isClickable = false;
        }

        // Assert it's NOT clickable when cart is empty
        Assert.assertFalse(isClickable, "'Place Order' button should be disabled or not clickable when cart is empty");
    }

    @Test(priority = 2, description = "Verify 'Place Order' button is clickable and order form appears with all inputs when products are added to the cart")
    public void testPlaceOrderButtonAndFormAfterAddingProducts() {
        driver.get("https://www.demoblaze.com/");

        // Navigate to the product page (Example: Sony vaio i7)
        WebElement productLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Samsung galaxy s6")));
        productLink.click();

        // Wait for the 'Add to cart' button to be clickable and click it
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']")));
        addToCartBtn.click();

        // Wait for the confirmation alert to appear and accept it
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();

        // Navigate back to the home page to add the next product (if needed, repeat for other products)
        driver.navigate().back();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navbarExample")));  // Wait for the page to load again

        // Navigate to the cart
        driver.findElement(By.id("cartur")).click();




        // Locate the 'Place Order' button
        WebElement placeOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']")));

        // Assert that the 'Place Order' button is clickable
        Assert.assertTrue(placeOrderBtn.isEnabled(), "'Place Order' button should be enabled and clickable");

        // Click the 'Place Order' button
        placeOrderBtn.click();

        // Wait for the order modal to appear
        WebElement orderModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderModal")));
        Assert.assertTrue(orderModal.isDisplayed(), "Order modal should appear after clicking 'Place Order'");

        // Verify all input fields are present in the order form
        WebElement nameInput = orderModal.findElement(By.id("name"));
        WebElement countryInput = orderModal.findElement(By.id("country"));
        WebElement cityInput = orderModal.findElement(By.id("city"));
        WebElement cardInput = orderModal.findElement(By.id("card"));
        WebElement monthInput = orderModal.findElement(By.id("month"));
        WebElement yearInput = orderModal.findElement(By.id("year"));

        Assert.assertTrue(nameInput.isDisplayed(), "Name input field is not displayed");
        Assert.assertTrue(countryInput.isDisplayed(), "Country input field is not displayed");
        Assert.assertTrue(cityInput.isDisplayed(), "City input field is not displayed");
        Assert.assertTrue(cardInput.isDisplayed(), "Card input field is not displayed");
        Assert.assertTrue(monthInput.isDisplayed(), "Month input field is not displayed");
        Assert.assertTrue(yearInput.isDisplayed(), "Year input field is not displayed");

        // Verify if these fields are also enabled for user interaction (not readonly or disabled)
        Assert.assertTrue(nameInput.isEnabled(), "Name input field should be enabled");
        Assert.assertTrue(countryInput.isEnabled(), "Country input field should be enabled");
        Assert.assertTrue(cityInput.isEnabled(), "City input field should be enabled");
        Assert.assertTrue(cardInput.isEnabled(), "Card input field should be enabled");
        Assert.assertTrue(monthInput.isEnabled(), "Month input field should be enabled");
        Assert.assertTrue(yearInput.isEnabled(), "Year input field should be enabled");
    }


    @Test(priority = 3, description = "Test with valid details")
    public void testValidPurchase() {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        // Enter valid details
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025"); // Valid Year


        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Verify success message
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Thank you for your purchase!')]")));
        softAssert.assertTrue(successMessage.isDisplayed(), "Purchase was not successful!");

        // Click on "OK" button
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'OK')]"))).click();
        softAssert.assertAll();}
    @Test(priority = 4, description = "Test with invalid name but all other fields valid")
    public void testInvalidName() {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        // Enter details with INVALID Name
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("@#$12345");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");



        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            softAssert.assertTrue(alertText.contains("Invalid") || alertText.contains("Name"),
                    "Expected error for invalid name , but got: " + alertText);

            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted invalid name (@#$12345) and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }



    @Test(priority = 5, description = "Test if invalid Country (numeric) is rejected")
    public void testInvalidCountry() {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        // Fill fields with INVALID city (numbers only)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("1234");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");

        // Submit order
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Verify behavior
        try {
            // 1. Check if an error alert appears (expected for invalid country)
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            softAssert.assertTrue(alertText.contains("Invalid") || alertText.contains("Country"),
                    "Expected error for invalid Country, but got: " + alertText);
            alert.accept();
        } catch (TimeoutException e) {
            // 2. If no alert, check if order was (incorrectly) completed
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted invalid country (1234) and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }
        softAssert.assertAll();
    }


    @Test(priority = 6, description = "Test if invalid city (numeric) is rejected")
    public void testInvalidCity() {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        // Fill fields with INVALID city (numbers only)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("1234"); // Invalid city
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");

        // Submit order
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Verify behavior
        try {
            // 1. Check if an error alert appears (expected for invalid city)
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            softAssert.assertTrue(alertText.contains("Invalid") || alertText.contains("City"),
                    "Expected error for invalid city, but got: " + alertText);
            alert.accept();
        } catch (TimeoutException e) {
            // 2. If no alert, check if order was (incorrectly) completed
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted invalid city (1234) and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }
        softAssert.assertAll();
    }



    @Test(priority = 7, description = "Test with invalid credit card but all other fields valid")
    public void testInvalidCreditCard() {
        SoftAssert softAssert = new SoftAssert();

        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("abcd1234");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");


        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            softAssert.assertTrue(alertText.contains("Invalid") || alertText.contains("Credit card"),
                    "Expected error for invalid credit card , but got: " + alertText);

            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted invalid credit card (abcd1234) and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }

    @Test(priority = 8,  description = "Test with invalid month but all other fields valid")
    public void testInvalidMonth() {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("abc");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");


        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            softAssert.assertTrue(alertText.contains("Invalid") || alertText.contains("Month"),
                    "Expected error for invalid month , but got: " + alertText);

            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted invalid month (abc) and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }

    @Test(priority = 9,  description = "Test with invalid expiry year")
    public void testInvalidExpiryYear()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        // Enter details with INVALID Expiry Year
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("USA");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("New York");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("abcd");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            softAssert.assertTrue(alertText.contains("Invalid") || alertText.contains("Year"),
                    "Expected error for invalid year , but got: " + alertText);
            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted invalid Year (abcd) and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }


    @Test(priority = 10,  description = "Test with empty name but all other fields valid")
    public void testEmptyName()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();
            softAssert.assertTrue(alertText.contains("Please fill out Name and Creditcard."), "Error message should be displayed for empty name.");
            alert.accept();
        } catch (TimeoutException e) {
            softAssert.fail("No error alert displayed for empty name.");
        }
        softAssert.assertAll();
    }

    @Test(priority = 11,  description = "Test with empty country but all other fields valid")
    public void testEmptyCountry()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();

            softAssert.assertTrue(alertText.contains("Please fill out Country") || alertText.contains("Country"),
                    "Expected an error message related to empty country, but got: " + alertText);

            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted empty country  and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }


    @Test(priority = 12,  description = "Test with empty city but all other fields valid")
    public void testEmptyCITY()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();

            softAssert.assertTrue(alertText.contains("please fill out City") || alertText.contains("Country"),
                    "Expected an error message related to empty city, but got: " + alertText);

            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted empty City  and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }



    @Test(priority = 13,  description = "Test with empty credit cart but all other fields valid")
    public void testEmptycreditcart()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();

            softAssert.assertTrue(alertText.contains("Please fill out Name and Creditcard."), "Error message should be displayed for empty name.");
            alert.accept();
        } catch (TimeoutException e) {
            softAssert.fail("No error alert displayed for empty credit cart.");
        }
        softAssert.assertAll();
    }


    @Test(priority = 14,  description = "Test with empty month but all other fields valid")
    public void testEmptyMonth()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("2025");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();

            softAssert.assertTrue(alertText.contains("please fill out Month") || alertText.contains("Month"),
                    "Expected an error message related to empty Month, but got: " + alertText);

            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted empty Month  and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }


    @Test(priority = 15,  description = "Test with empty year but all other fields valid")
    public void testEmptyYear()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Salma");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys("1234 5678 9012 3456");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys("12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys("");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Check for alert/error message
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();

            softAssert.assertTrue(alertText.contains("please fill out Year") || alertText.contains("Year"),
                    "Expected an error message related to empty Month, but got: " + alertText);

            alert.accept();
        } catch (TimeoutException e) {
            try {
                WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(., 'Thank you for your purchase!')]")));

                softAssert.fail("BUG: System accepted empty Year and completed purchase.");

            } catch (TimeoutException e2) {
                softAssert.fail("No error alert or confirmation message appeared.");
            }
        }

        softAssert.assertAll(); }


    @Test(priority = 16, enabled = false, description = "Verify confirmation message displays correct Order ID, Amount, Credit Card Number, and Date")
    public void testOrderConfirmationDetails() {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        // Enter valid details
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("John Doe");
        driver.findElement(By.id("country")).sendKeys("USA");
        driver.findElement(By.id("city")).sendKeys("New York");
        driver.findElement(By.id("card")).sendKeys("1234 5678 9012 3456");
        driver.findElement(By.id("month")).sendKeys("12");
        driver.findElement(By.id("year")).sendKeys("2025");


        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();

        // Verify confirmation message appears
        WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='lead text-muted ']")));
        String messageText = confirmationMessage.getText();

        // Validate that the confirmation contains expected details
        softAssert.assertTrue(messageText.contains("Order ID:"), "Order ID is missing in confirmation message.");
        softAssert.assertTrue(messageText.contains("Amount:"), "Amount is missing in confirmation message.");
        softAssert.assertTrue(messageText.contains("Credit Card Number:"), "Credit Card Number is missing in confirmation message.");
        softAssert.assertTrue(messageText.contains("Date:"), "Date is missing in confirmation message.");

        // Click on "OK" button
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'OK')]"))).click();
        softAssert.assertAll();}

    @Test(priority = 17, description = "Verify confirmation message details match entered input values")
    public void testConfirmationMessageDetails()  {
        SoftAssert softAssert = new SoftAssert();
        addProductToCart();

        // Extract expected amount from cart
        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        String expectedAmount = totalElement.getText().trim(); // Get amount displayed in the cart

        // Input values
        String expectedName = "John Doe";
        String expectedCreditCard = "1234 5678 9012 3456";
        String expectedMonth = "12";
        String expectedYear = "2025";
        String expectedMaskedCard = "**** **** **** " + expectedCreditCard.substring(expectedCreditCard.length() - 4);

        // Enter details
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(expectedName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country"))).sendKeys("Egypt");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city"))).sendKeys("Cairo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card"))).sendKeys(expectedCreditCard);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month"))).sendKeys(expectedMonth);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year"))).sendKeys(expectedYear);


        // Click Purchase
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Purchase')]"))).click();


        // Verify confirmation message contains correct details
        WebElement confirmationBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sweet-alert")));
        String confirmationText = confirmationBox.getText();

        softAssert.assertTrue(confirmationText.contains(expectedName), "Name in confirmation message is incorrect!");
        softAssert.assertTrue(confirmationText.contains(expectedMaskedCard), "Credit Card Number is not masked correctly!");
        softAssert.assertTrue(confirmationText.contains("Amount: " + expectedAmount), "Amount in confirmation message is incorrect!");

        // Extract and verify the generated date (ignoring the random day)
        Pattern datePattern = Pattern.compile("\\b(\\d{1,2})/(\\d{1,2})/(\\d{4})\\b"); // Match DD/MM/YYYY format
        Matcher matcher = datePattern.matcher(confirmationText);

        if (matcher.find()) {
            String extractedMonth = matcher.group(2);
            String extractedYear = matcher.group(3);

            // Ensure the month and year match the input, ignoring the randomly generated day
            softAssert.assertEquals(extractedMonth, expectedMonth, "Month in confirmation message is incorrect!");
            softAssert.assertEquals(extractedYear, expectedYear, "Year in confirmation message is incorrect!");
        } else {
            Assert.fail("No valid date found in confirmation message!");
        }
        Pattern orderIdPattern = Pattern.compile("Id: (\\d+)"); // Looks for "Order ID: 12345"
        Matcher orderIdMatcher = orderIdPattern.matcher(confirmationText);

        if (orderIdMatcher.find()) {
            String extractedOrderId = orderIdMatcher.group(1); // Get the Order ID from the message
            softAssert.assertFalse(extractedOrderId.isEmpty(), "Order ID is missing from the confirmation message!");
            softAssert.assertTrue(extractedOrderId.matches("\\d+"), "Order ID is not a valid number!");
            System.out.println("Order ID extracted: " + extractedOrderId);
        } else {
            softAssert.fail("Order ID not found in confirmation message!");
        }
        // Click OK to close the confirmation
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'OK')]"))).click();
        softAssert.assertAll();  }



    @Test(priority = 18, description = "Verify cart total updates correctly after adding multiple products")
    public void testCartTotalCalculation() {
        driver.get("https://www.demoblaze.com/");


        // List of products to add (from your image)
        List<String> products = Arrays.asList(
                "Sony vaio i7",
                "Nexus 6",
                "Samsung galaxy s6",
                "Apple monitor 24",
                "MacBook Pro",
                "ASUS Full HD"
        );

        int expectedTotal = 0;

        for (String product : products) {
            int productPrice = addProductToCartAndGetPrice(product);
            expectedTotal += productPrice;
            System.out.println("Added " + product + " (Price: " + productPrice + "), Current Total: " + expectedTotal);
        }

        // Verify the total matches expected (2900 as shown in your image)
        verifyCartTotal(expectedTotal);

    }

    /**
     * Adds product to cart and returns its price
     */
    private int addProductToCartAndGetPrice(String productName) {
        // Navigate to appropriate category
        if (productName.contains("vaio") || productName.contains("MacBook Pro")) {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();
        } else if  (productName.contains("monitor") || productName.contains("ASUS")) {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Monitors"))).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
        }

        // Click on product
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText(productName))).click();
        } catch (StaleElementReferenceException e) {
            // If element is stale, try to locate it again
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText(productName))).click();
        }
        // Get product price
        WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[@class='price-container']")));
        String priceText = priceElement.getText().split(" ")[0].replace("$", "");
        int price = Integer.parseInt(priceText);

        // Add to cart
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]"))).click();

        // Handle alert
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        // Return to home page
        driver.findElement(By.xpath("//a[contains(text(),'Home')]")).click();

        return price;
    }

    /**
     * Verifies cart total matches expected value
     */
    private void verifyCartTotal(int expectedTotal) {
        SoftAssert softAssert = new SoftAssert();
        driver.findElement(By.id("cartur")).click();

        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        int actualTotal = Integer.parseInt(totalElement.getText().trim());

        softAssert.assertEquals(actualTotal, expectedTotal,
                String.format("Cart total is incorrect! Expected: %d but got: %d", expectedTotal, actualTotal));
        softAssert.assertAll();}

    @Test(priority = 19, description = "Verify cart total updates correctly after removing 2 products")
    public void testRemoveTwoProductsAndVerifyTotal()  {
        SoftAssert softAssert = new SoftAssert();
        driver.get("https://www.demoblaze.com/");
        driver.findElement(By.id("cartur")).click();

        // Products and their prices in the cart
        Map<String, Integer> cartItems = new HashMap<>();
        cartItems.put("Sony vaio i7", 790);
        cartItems.put("Nexus 6", 650);
        cartItems.put("Samsung galaxy s6", 360);
        cartItems.put("Apple monitor 24", 400);
        cartItems.put("MacBook Pro", 1100);
        cartItems.put("ASUS Full HD", 230);

        // Products to remove
        List<String> productsToRemove = Arrays.asList("Samsung galaxy s6", "Apple monitor 24");

        // Wait for cart table to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));

        // Remove selected products from the cart
        for (String product : productsToRemove) {
            removeProductFromCart(product);
            cartItems.remove(product); // Remove from map to keep remaining
        }

        // Calculate expected total after removals
        int expectedTotal = cartItems.values().stream().mapToInt(Integer::intValue).sum();

        // Wait for total to update (non-empty and not stale)
        wait.until(driver -> {
            String totalText = driver.findElement(By.id("totalp")).getText().trim();
            return !totalText.isEmpty() && Integer.parseInt(totalText) == expectedTotal;
        });

        // Get actual total
        WebElement totalElement = driver.findElement(By.id("totalp"));
        int actualTotal = Integer.parseInt(totalElement.getText().trim());

        // Assert the total is as expected
        softAssert.assertEquals(actualTotal, expectedTotal,
                "Cart total mismatch after removing 2 products. Expected: " + expectedTotal + ", Actual: " + actualTotal);
        softAssert.assertAll();}

    private void removeProductFromCart(String productName) {
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#tbodyid > tr")));

        for (WebElement row : rows) {
            String name = row.findElements(By.tagName("td")).get(1).getText().trim();
            if (name.equals(productName)) {
                row.findElement(By.linkText("Delete")).click();
                wait.until(ExpectedConditions.stalenessOf(row)); // Wait for row to disappear
                break;
            }
        }
    }

    @Test(priority = 20, description = "Verify cart total is empty after removing all products")
    public void testClearCartAndVerifyEmptyTotal() {
        driver.get("https://www.demoblaze.com/");
        driver.findElement(By.id("cartur")).click();

        // Products to remove (remaining after previous test)
        List<String> productsToRemove = Arrays.asList(
                "Sony vaio i7",
                "Nexus 6",
                "MacBook Pro",
                "ASUS Full HD"
        );

        // Wait for cart table to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));

        // Remove each product
        for (String product : productsToRemove) {
            removeProductFromCart(product);
        }

        // Wait for all rows to be gone (cart empty)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#tbodyid > tr")));

        // Try to get total text
        WebElement totalElement = driver.findElement(By.id("totalp"));
        String totalText = totalElement.getText().trim();

        // Assert total is either empty or 0
        Assert.assertTrue(totalText.isEmpty() || totalText.equals("0"),
                "Expected cart total to be empty or 0, but got: " + totalText);
    }

    @Test(priority = 21, description = "Verify cart retains products after logging in")
    public void testCartPersistenceAfterLogin() {
        driver.get("https://www.demoblaze.com/");

        // Add products to the cart before logging in
        WebElement productLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Nokia lumia 1520")));
        productLink.click();

        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']")));
        addToCartBtn.click();
        wait.until(ExpectedConditions.alertIsPresent()).accept(); // Handle the cart alert

        // Navigate back to homepage and wait for the homepage to load
        driver.get("https://www.demoblaze.com/");

        // Add another product to the cart
        productLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Samsung galaxy s6")));
        productLink.click();

        addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']")));
        addToCartBtn.click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        // Navigate to the login page and log in
        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login2")));
        loginBtn.click();

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword")));
        usernameInput.sendKeys("Salma Mohamed");
        passwordInput.sendKeys("bashera23");

        WebElement loginSubmitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Log in']")));
        loginSubmitBtn.click();

        driver.get("https://www.demoblaze.com/cart.html");

        // Wait until the cart page is fully loaded
        WebElement cartTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));

        // Verify that the cart has items after login
        List<WebElement> cartItems = cartTable.findElements(By.cssSelector("tr"));
        Assert.assertTrue(cartItems.size() > 0, "Cart should not be empty after login.");

        // Verify that the total price is correctly displayed
        WebElement totalPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        String totalPriceText = totalPriceElement.getText();
        Assert.assertNotNull(totalPriceText, "Total price should be displayed.");
        Assert.assertTrue(totalPriceText.matches("^[0-9]+\\.[0-9]{2}$"), "Total price should have the correct format.");
    }

    @Test(priority = 22, description = "Verify that credit card input is masked and only last four digits are visible")
    public void testCreditCardInputMasking() {
        ;
        addProductToCart();



        WebElement creditCardField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card")));


        String creditCardNumber = "1234 5678 9012 3456";
        creditCardField.sendKeys(creditCardNumber);

        String fieldValue = creditCardField.getAttribute("value");
        System.out.println(fieldValue);

        Assert.assertTrue(fieldValue.matches(".*\\d{4}$"), "Credit card input is not properly masked.");

        Assert.assertTrue(fieldValue.matches("\\*{12}\\d{4}"), "Credit card number is not properly masked.");
    }


    @AfterTest
public void tearDown() {
    if (driver != null) {
        driver.quit(); // Close the browser after the test
    }}}







