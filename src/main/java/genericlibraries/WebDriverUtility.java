package genericlibraries;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;


public class WebDriverUtility {
	public WebDriver driver;
	
	
	public WebDriver openBrowserAndApplication(String url,long time) {
		WebDriverManager.chromedriver().setup();
		driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
		return driver;
	}

	
	public void mouseHoverToElement(WebElement element) {
		Actions a = new Actions(driver);
		a.moveToElement(element).perform();
	}
	
	
	public void dropDown(WebElement element, String text) {
		Select s = new Select(element);
		s.selectByVisibleText(text);
	}
	
	public void dropDown(String text, WebElement element) {
		Select s = new Select(element);
		s.selectByValue(text);
	}
	
	
	public void switchToFrame(String index) {
		driver.switchTo().frame(index);
	}
	
	
	public void switchBackFromFrame() {
		driver.switchTo().defaultContent();
	}
	
	
	public void scrollTillElement(Object element) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		jse.executeScript("arguments[0].scrollIntoView(true);",element);
	}
	
	public void getScreenshot(WebDriver driver,JavaUtility javaUtility, String classname) {
		String currentTime = javaUtility.currentTime();
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File dest = new File("./screenshot/"+classname+"_"+currentTime+".png");
		try {
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void alertPopup() {
		driver.switchTo().alert().accept();
	}
	
	
	public String getParentWindow() {
		return driver.getWindowHandle();
	}
	
	
	public void switchToWindow(String windowID) {
		driver.switchTo().window(windowID);
	}
	
	
	public void handleChildBrowserPopup(Object expectedTitle) {
		Set<String> windowTitles = driver.getWindowHandles();
		for(String windowID : windowTitles) {
			driver.switchTo().window(windowID);
			String actualTitle = driver.getTitle();
			if (actualTitle.equals(expectedTitle))
				break;
		}
	}
	
	
	public void closeBrowser() {
		driver.quit();
	}
}



