
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Add_to_Cart {
    @Test
    public void Add_one_element() throws InterruptedException {

        WebDriver driver= new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com");

        Thread.sleep(1000);
        driver.findElement(By.xpath("//a[contains(text(), 'Samsung galaxy s6')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.className("btn-success")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }
}

