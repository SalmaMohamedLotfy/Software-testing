import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
   protected final WebDriver driver;

   public HomePage(WebDriver driver){
       this.driver = driver;
   }
   //Elements
    public WebElement homeLinkElement(){
       By homeLinkLocator = By.xpath("//a[contains(text(),'Home')]");
       return driver.findElement(homeLinkLocator);
    }
    public WebElement contactLinkElement(){
        By ContactLinkLocator = By.xpath("//a[@class='nav-link' and contains(text(),'Contact')]");
        return driver.findElement(ContactLinkLocator);
    }
    public WebElement aboutUsLinkElement(){
        By aboutUsLinkLocator = By.xpath("//a[contains(text(),'About us')]");
        return driver.findElement(aboutUsLinkLocator);
    }
    public WebElement cartLinkElement(){
        By CartLinkLocator = By.id("cartur");
        return driver.findElement(CartLinkLocator);
    }
    public WebElement signUpLinkElement(){
        By signULinkLocator = By.id("signin2");
        return driver.findElement(signULinkLocator);
    }
    public WebElement verifyLoginPageLoaded(){
       By loginText = By.xpath("//h5[@id=\"logInModalLabel\"]");
       return driver.findElement(loginText);
    }
}