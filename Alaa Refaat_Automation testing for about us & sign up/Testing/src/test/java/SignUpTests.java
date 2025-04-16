
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
public class SignUpTests {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @DataProvider(name = "signUpData")
    public Object[][] signUpData() {
        return new Object[][] {
                {"Alaa", "123", "Error"}, // TC_REG_2
                {"Alaa_hassan", "Alaa_hassan#123", "Success"}, // TC_REG_3
                {"Alaa_hassan", "Alaa_hassan#123", "Exists"}  // TC_REG_4
        };
    }

    @Test
    public void testSignUpFormFieldsVisible() {
        driver.get("https://www.demoblaze.com/");
        driver.findElement(By.id("signin2")).click();

        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        WebElement password = driver.findElement(By.id("sign-password"));
        WebElement signUpButton = driver.findElement(By.xpath("//button[text()='Sign up']"));

        Assert.assertTrue(username.isDisplayed(), "Username field is not visible!");
        Assert.assertTrue(password.isDisplayed(), "Password field is not visible!");
        Assert.assertTrue(signUpButton.isDisplayed(), "Sign up button is not visible!");
    }

    @Test(dataProvider = "signUpData")
    public void testSignUpProcess(String username, String password, String expectedResult) {
        driver.get("https://www.demoblaze.com/");
        driver.findElement(By.id("signin2")).click();

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        WebElement passwordField = driver.findElement(By.id("sign-password"));
        WebElement signUpButton = driver.findElement(By.xpath("//button[text()='Sign up']"));

        usernameField.clear();
        passwordField.clear();

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        signUpButton.click();

        // Wait for alert and capture its text
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();

        if (expectedResult.equals("Success")) {
            Assert.assertTrue(alertText.contains("Sign up successful"), "Expected success message, got: " + alertText);
        } else if (expectedResult.equals("Error")) {
            Assert.assertFalse(alertText.contains("Sign up successful"), "Expected error, but got success!");
        } else if (expectedResult.equals("Exists")) {
            Assert.assertTrue(alertText.contains("This user already exist"), "Expected existing user error, got: " + alertText);
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


