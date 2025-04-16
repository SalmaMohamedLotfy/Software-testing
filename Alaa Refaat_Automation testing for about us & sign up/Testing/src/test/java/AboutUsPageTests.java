
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
public class AboutUsPageTests {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {

        driver = new ChromeDriver();
        driver. manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test(priority = 1)
    public void testCompanyDetailsAndMissionDisplayed() {
        driver.get("https://www.demoblaze.com/");

        WebElement aboutUsLink = driver.findElement(By.cssSelector("a[data-target='#videoModal']"));
        aboutUsLink.click();

        WebElement companyDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("videoModalLabel")));
        WebElement missionStatement = driver.findElement(By.xpath("//div[@id='videoModal']//div[@class='modal-body']"));

        Assert.assertTrue(companyDetails.isDisplayed(), "Company details are not displayed!");
        Assert.assertTrue(missionStatement.isDisplayed(), "Mission statement is not displayed!");
    }

    @Test(priority = 2)
    public void testContactUsButtonFunctionality() {
        driver.get("https://www.demoblaze.com/");

        WebElement contactUsButton = driver.findElement(By.cssSelector("a[data-target='#exampleModal']"));
        Assert.assertTrue(contactUsButton.isDisplayed(), "Contact Us button is not visible!");

        contactUsButton.click();

        WebElement contactModalTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exampleModalLabel")));
        Assert.assertEquals(contactModalTitle.getText(), "New message", "Contact modal title is incorrect!");
    }

    @Test(priority = 3)
    public void testResponsiveness() {
        driver.get("https://www.demoblaze.com/");

        WebElement aboutUsLink = driver.findElement(By.cssSelector("a[data-target='#videoModal']"));

        driver.manage().window().setSize(new Dimension(1920, 1080));
        Assert.assertTrue(aboutUsLink.isDisplayed(), "About Us link not visible in Desktop view!");

        driver.manage().window().setSize(new Dimension(768, 1024));
        Assert.assertTrue(aboutUsLink.isDisplayed(), "About Us link not visible in Tablet view!");

        driver.manage().window().setSize(new Dimension(375, 667));
        Assert.assertTrue(aboutUsLink.isDisplayed(), "About Us link not visible in Mobile view!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


