import org.testng.annotations.*;

public class Login_Logout_Test extends TestBase {
    // Test Data
    protected final String username = "admin";
    protected final String password = "admin";
    protected final String INVALID_PASSWORD = "asd123";
    protected final String NON_EXISTENT_USER = "orebaz9090";
    protected final String NON_EXISTENT_PASS = "asd9090";

    @Test(priority = 4)
    public void Login_ValidCredentials() {
        loginPage.clickLoginLink();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        soft.assertTrue(loginPage.isWelcomeMessageDisplayed(), "Welcome message not displayed after valid login");
        soft.assertEquals(loginPage.getWelcomeMessageText(),"Welcome " + username, "Incorrect welcome message text");
        soft.assertAll();
    }

    @Test(dependsOnMethods = {"Login_ValidCredentials"})
    public void LogoutFunctionality_AfterLogin() {
        loginPage.clickLogoutLink();

        soft.assertTrue(loginPage.loginLinkElement().isDisplayed(),
                "Login link not visible after logout");
        soft.assertFalse(loginPage.isWelcomeMessageDisplayed(),
                "Welcome message still visible after logout");
    }

    @Test(priority = 3)
    public void Login_ValidUsername_InvalidPassword() {
        loginPage.clickLoginLink();
        loginPage.enterUsername(username);
        loginPage.enterPassword(INVALID_PASSWORD);
        loginPage.clickLoginButton();

        String alertText = getAlertTextAndAccept();
        soft.assertEquals(alertText, "Wrong password.", "Incorrect alert message for wrong password");
        soft.assertFalse(loginPage.isWelcomeMessageDisplayed(), "Welcome message shown with wrong password");
        soft.assertAll();
    }

    @Test(priority = 2)
    public void Login_NonExistentAccount() {
        loginPage.clickLoginLink();
        loginPage.enterUsername(NON_EXISTENT_USER);
        loginPage.enterPassword(NON_EXISTENT_PASS);
        loginPage.clickLoginButton();
        String alertText = getAlertTextAndAccept();
        soft.assertEquals(alertText, "User does not exist.",
                "Incorrect alert message for non-existent user");

        soft.assertFalse(loginPage.isWelcomeMessageDisplayed(),
                "Welcome message shown for non-existent account");
        soft.assertAll();
    }

    @Test(priority = 1)
    public void Login_EmptyFields() {
        loginPage.clickLoginLink();
        loginPage.clickLoginButton();

        String alertText = getAlertTextAndAccept();
        soft.assertEquals(alertText, "Please fill out Username and Password.",
                "Incorrect alert message for empty fields");

        soft.assertFalse(loginPage.isWelcomeMessageDisplayed(),
                "Welcome message shown with empty credentials");
        soft.assertAll();
    }
}