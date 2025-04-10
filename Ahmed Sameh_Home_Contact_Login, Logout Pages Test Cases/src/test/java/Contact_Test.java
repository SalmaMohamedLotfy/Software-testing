import org.testng.annotations.Test;

public class Contact_Test extends TestBase{

   protected final String Email = "orebaz90@gmail.com";
   protected final String Name = "Ahmed Sameh";
   protected final String Message = "Hello World";
   protected String expectedResult = "Thanks for the message!!";
   protected String expectedResultContact = "New message";

   @Test(priority = 1)
   public void verify_Navigation(){
      homePage.contactLinkElement().click();
      contactPage.contactMessageText();
      soft.assertEquals(contactPage.contactMessageText().contains(expectedResultContact),true);
      soft.assertAll();
   }
   @Test(priority = 2, dependsOnMethods = "verify_Navigation")
   public void verify_ValidScenario(){
      homePage.contactLinkElement().click();
      contactPage.enterContactMail(Email);
      contactPage.enterContactName(Name);
      contactPage.enterContactMSG(Message);
      contactPage.contactMSG_Button().click();
      soft.assertEquals(getAlertTextAndAccept(),expectedResult);
      soft.assertAll();
   }
   @Test(priority = 3, dependsOnMethods = "verify_Navigation")
   public void verify_ValidationMSG_For_MissingField(){
      homePage.contactLinkElement().click();
      contactPage.enterContactMail(Email);
      contactPage.enterContactMSG(Message);
      contactPage.contactMSG_Button().click();
      soft.assertEquals(getAlertTextAndAccept().contains(expectedResult),false);
      soft.assertAll();
   }
   @Test(priority = 4, dependsOnMethods = "verify_Navigation")
   public void verify_SendMSG_EmptyFields(){
      homePage.contactLinkElement().click();
      contactPage.contactMSG_Button().click();
      soft.assertEquals(getAlertTextAndAccept().contains(expectedResult),false);
      soft.assertAll();
   }
}