import org.apache.commons.lang3.time.DurationFormatUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Home_Test extends TestBase{

    @Test(priority = 1)
    public void verify_Navigation(){
        soft.assertTrue(homePage.homeLinkElement().isDisplayed(), "Home link not displayed");
        soft.assertTrue(homePage.contactLinkElement().isDisplayed(), "Contact link not displayed");
        soft.assertTrue(homePage.aboutUsLinkElement().isDisplayed(), "About us link not displayed");
        soft.assertTrue(homePage.cartLinkElement().isDisplayed(), "Cart link not displayed");
        soft.assertTrue(loginPage.loginLinkElement().isDisplayed(), "Login link not displayed");
        soft.assertTrue(homePage.signUpLinkElement().isDisplayed(), "Signup link not displayed");
        soft.assertAll();
    }
    @Test(dependsOnMethods = {"verify_LoginPage_Navigation"})
    public void verify_Navigation_AfterLogin(){
        loginPage.loginSuccessfully(login_Test.username, login_Test.password);
        soft.assertTrue(homePage.homeLinkElement().isDisplayed(), "Home link not displayed");
        soft.assertTrue(homePage.contactLinkElement().isDisplayed(), "Contact link not displayed");
        soft.assertTrue(homePage.aboutUsLinkElement().isDisplayed(), "About us link not displayed");
        soft.assertTrue(homePage.cartLinkElement().isDisplayed(), "Cart link not displayed");
        soft.assertTrue(loginPage.logoutLinkElement().isDisplayed(),"Logout link not displayed");
        soft.assertTrue(loginPage.isWelcomeMessageDisplayed(),"Welcome message not displayed");
        soft.assertAll();
    }
    @Test(priority = 2)
    public void verify_LoginPage_Navigation() throws InterruptedException {
        loginPage.clickLoginLink();
        Thread.sleep(1000);
        soft.assertTrue(homePage.verifyLoginPageLoaded().isDisplayed(),"Navigation to Login Page failed");
        soft.assertAll();
    }
}