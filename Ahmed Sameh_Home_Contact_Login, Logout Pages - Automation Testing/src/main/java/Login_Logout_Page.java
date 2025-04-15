import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Login_Logout_Page {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public Login_Logout_Page(WebDriver driver)
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    // Elements & Locators
    public WebElement loginLinkElement() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("login2")));
    }

    public WebElement usernameFieldElement() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
    }

    public WebElement passwordFieldElement() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword")));
    }

    public WebElement loginButtonElement() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Log in')]")));
    }

    public WebElement logoutLinkElement() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("logout2")));
    }

    // Methods
    public void clickLoginLink() {
        loginLinkElement().click();
    }

    public void enterUsername(String username) {
        usernameFieldElement().clear();
        usernameFieldElement().sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordFieldElement().clear();
        passwordFieldElement().sendKeys(password);
    }

    public void clickLoginButton() {
        loginButtonElement().click();
    }

    public boolean isWelcomeMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public String getWelcomeMessageText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser"))).getText();
        } catch (Exception e) {
            return getWelcomeMessageText();
        }
    }
    public void clickLogoutLink() {
        logoutLinkElement().click();
    }

    public void loginSuccessfully(String username, String password) {
        clickLoginLink();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}