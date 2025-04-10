import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ContactPage {
    protected final WebDriver driver;
    protected  final WebDriverWait wait;

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }
    //Elements
    public String contactMessageText(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exampleModalLabel"))).getText();
    }
    public WebElement contactMail(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-email")));
    }
    public WebElement contactName(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-name")));
    }
    public WebElement contactMSG(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message-text")));
    }
    public WebElement contactMSG_Button(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@onclick=\"send()\"]")));
    }
    //Methods
    public void enterContactMail(String mail){
        contactMail().clear();
        contactMail().sendKeys(mail);
    }
    public void enterContactName(String name){
        contactName().clear();
        contactName().sendKeys(name);
    }
    public void enterContactMSG(String MSG){
        contactMSG().clear();
        contactMSG().sendKeys(MSG);
    }
}