package pomimplementation;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import genericlibraries.ExcelFileUtility;
import genericlibraries.AutoConstantPath;
import genericlibraries.JavaUtility;
import genericlibraries.PropertyFileUttility;
import genericlibraries.WebDriverUtility;
import pompages.CreatingNewOrganizationPage;
import pompages.HomePage;
import pompages.LoginPage;
import pompages.NewOrganizationInfoPage;
import pompages.OrganizationsPage;

public class CreateOrganizationTest {

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
		
		WebDriver driver = webdriver.openBrowserAndApplication( url, time);
		
		LoginPage loginPage = new LoginPage(driver);
		HomePage home = new HomePage(driver);
		OrganizationsPage organizations = new OrganizationsPage(driver);
		CreatingNewOrganizationPage createOrganization =	new CreatingNewOrganizationPage(driver);
		NewOrganizationInfoPage newOrganizationInfo = new NewOrganizationInfoPage(driver);
		
		if (loginPage.getLogo().isDisplayed())
			System.out.println("Pass: Vtiger login page is diplayed");
		else
			System.out.println("Fail: Vtiger login page is not displayed");

		loginPage.loginToApplication(username, password);

		if (home.getPageHeader().contains("Home"))
			System.out.println("Pass : Login successful");
		else
			System.out.println("Fail : Login not successful");
		
		home.clickOrganizationsTab();

		if (organizations.getPageHeader().contains("Organizations"))
			System.out.println("Pass : Organizations page displayed");
		else
			System.out.println("Fail : Organizations page not displayed");

		organizations.clickPlusButton();
		
		if (createOrganization.getPageHeader().contains("Creating New Organization"))
			System.out.println("Pass : Creating new organization page is displayed");
		else
			System.out.println("Fail : Creating new organization page is not displayed");
		
		Map<String,String> map =excel.fetchMultipleDataBasedOnKeyFromExcel("TestData", "Create Organization");
		
		String newOrganizationName = map.get("Organization Name")+javaUtility.generateRandomNumber(100);
				
		createOrganization.setOrganizationName(newOrganizationName);
		createOrganization.selectIndustry(webdriver, map.get("Industry"));
		createOrganization.clickGroupRadioButton();
		createOrganization.selectGroupFromDropdown(webdriver, map.get("Group"));
		createOrganization.clickSave();

		if (newOrganizationInfo.getPageHeader().contains(newOrganizationName))
			System.out.println("Pass : New organization created successfully");
		else
			System.out.println("Fail : Organization is not created");

		newOrganizationInfo.clickOrganization();

		if (organizations.getPageHeader().contains("Organizations"))
			System.out.println("Pass : Organizations page displayed");
		else
			System.out.println("Fail : Organizations page is not displayed");

		if (organizations.getNewOrganization().equalsIgnoreCase(newOrganizationName)) {
			System.out.println("Test Case Passed");
			excel.writeDataIntoExcel("TestData", "Pass", AutoConstantPath.EXCEL_FILE_PATH, "Create Organization");
		}
			
		else {
			System.out.println("Test Case Failed");
			excel.writeDataIntoExcel("TestData", "Fail", AutoConstantPath.EXCEL_FILE_PATH, "Create Organization");
		}
			
		home.signOutFromVtiger(webdriver);

		webdriver.closeBrowser();
		excel.closeExcel();
	}

}