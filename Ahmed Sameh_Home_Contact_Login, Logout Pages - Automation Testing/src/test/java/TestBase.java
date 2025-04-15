import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class TestBase {

    SoftAssert soft = new SoftAssert();
    protected WebDriver driver;
    protected HomePage homePage;
    protected Login_Logout_Page loginPage;
    protected ContactPage contactPage;
    protected Login_Logout_Test login_Test;

    public String getAlertTextAndAccept() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (Exception e) {
            System.out.println("No alert was present.");
            return null;
        }
    }
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.demoblaze.com/");
        driver.manage().window().maximize();
    }
    @BeforeMethod(alwaysRun = true)
    public void resetState() {
        driver.navigate().refresh();
        homePage = new HomePage(driver);
        loginPage = new Login_Logout_Page(driver);
        contactPage = new ContactPage(driver);
        login_Test = new Login_Logout_Test();
    }
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}