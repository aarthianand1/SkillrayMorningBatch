package pomimplementation;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import genericlibraries.ExcelFileUtility;
import genericlibraries.AutoConstantPath;
import genericlibraries.JavaUtility;
import genericlibraries.PropertyFileUttility;
import genericlibraries.WebDriverUtility;
import pompages.ContactPage;
import pompages.CreateNewContactPage;
import pompages.HomePage;
import pompages.LoginPage;
import pompages.NewContactInfoPage;

public class CreateContactTest {

	public static void main(String[] args) throws IOException {
		
		WebDriverUtility webdriver = new WebDriverUtility();
		JavaUtility javaUtility = new JavaUtility();
		
		PropertyFileUttility property = new PropertyFileUttility();
		property.propertyFileInitialization(AutoConstantPath.PROPERTY_FILE_PATH);
		
		ExcelFileUtility excel = new ExcelFileUtility();
		excel.excelFileInitialization(AutoConstantPath.EXCEL_FILE_PATH);
		
		String url = property.getDataFromPropertyFile("url");
		String username = property.getDataFromPropertyFile("username");
		String password = property.getDataFromPropertyFile("password");
		long time = Long.parseLong(property.getDataFromPropertyFile("timeouts")); 
		
		WebDriver driver = webdriver.openBrowserAndApplication(url, time);
		
		LoginPage loginPage = new LoginPage(driver);
		HomePage home = new HomePage(driver);
		ContactPage contacts = new ContactPage(driver);
		CreateNewContactPage createNewContact = new CreateNewContactPage(driver);
		NewContactInfoPage newContact = new NewContactInfoPage(driver);
		
		if (loginPage.getLogo().isDisplayed())
			System.out.println("Pass: Vtiger login page is diplayed");
		else
			System.out.println("Fail: Vtiger login page is not displayed");

		loginPage.loginToApplication(username, password);

		if (home.getPageHeader().contains("Home"))
			System.out.println("Pass : Login successful");
		else
			System.out.println("Fail : Login not successful");
		
		home.clickContactsTab();
		
		if (contacts.getPageHeader().contains("Contacts"))
			System.out.println("Pass : Contacts page displayed");
		else
			System.out.println("Fail : Contacts page not displayed");

		contacts.clickPlusButton();
		
		if (createNewContact.getPageHeader().contains("Creating New Contact"))
			System.out.println("Pass : Creating new Contact page is displayed");
		else
			System.out.println("Fail : Creating new Contact page is not displayed");

		Map<String,String> map = excel.fetchMultipleDataBasedOnKeyFromExcel("TestData", "Create Contact");
		
		createNewContact.selectFirstNameSalutation(webdriver, map.get("First Name Salutation"));
		String contactName = map.get("Last Name") + javaUtility.generateRandomNumber(100);
		createNewContact.setLastName(contactName);
		createNewContact.selectExistingOrganization(webdriver, map.get("Organization Name"), driver);
		createNewContact.uploadContactImage(map.get("Contact Image"));
		createNewContact.clickSaveButton();

		if (newContact.getPageHeader().contains(contactName))
			System.out.println("Pass : New contact created successfully");
		else
			System.out.println("Fail : Contact is not created");

		newContact.clickContactsLink();
		
		if (contacts.getPageHeader().contains("Contacts"))
			System.out.println("Pass : Contacts page displayed");
		else
			System.out.println("Fail : Contacts page is not displayed");

		if (contacts.getLastContactName().equalsIgnoreCase(contactName)) {
			System.out.println("Test Case Passed");
			excel.writeDataIntoExcel("TestData", "Pass", AutoConstantPath.EXCEL_FILE_PATH, "Create Contact");
		}
			
		else {
			System.out.println("Test Case Failed");
			excel.writeDataIntoExcel("TestData", "Fail", AutoConstantPath.EXCEL_FILE_PATH, "Create Contact");
		}
			
		home.signOutFromVtiger(webdriver);
		
		excel.closeExcel();
		webdriver.closeBrowser();

	}

}