package testScripts;

import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestScript05 {
	
	private WebDriver driver; // Selenium control driver
	private String baseUrl; // baseUrl of website Guru99

	/**
	 * Before Testing Setup test environment before executing test
	 * 
	 * @throws Exception
	 * 
	 */
	
	@DataProvider(name = "GuruTest")
	public Object[][] testData() throws Exception {
		return Util.getDataFromExcel(Util.FILE_PATH, Util.SHEET_NAME,
				Util.TABLE_NAME);
	}
	
	@BeforeMethod
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", "C:\\Workspace\\selenium-java-3.141.59\\geckodriver-v0.24.0-win64\\geckodriver.exe");

//		File pathToBinary = new File(Util.FIREFOX_PATH);
//		FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
//		FirefoxProfile firefoxProfile = new FirefoxProfile();
		driver = new FirefoxDriver();

		// Setting Base URL of website Guru99
		baseUrl = Util.BASE_URL;
		driver.manage().timeouts()
				.implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);
		// Go to http://www.demo.guru99.com/V4/
		driver.get(baseUrl + "/V4/");
	}
	
	@Test(dataProvider = "GuruTest")
	public void TestCase05(String username, String password) throws Exception {
		String actualAlertMsg;
		
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(username);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("btnLogin")).click();
		
		takeSnapShot(driver, "screenshots\\login-" + username+ "-" + password + ".png");
		
		try{
			Alert alt = driver.switchTo().alert();
			actualAlertMsg = alt.getText();
			alt.accept();
			
			assertEquals(actualAlertMsg, Util.EXPECT_ERROR);
		}
		
		catch (NoAlertPresentException Ex){ 
	    	// Get text displayes on login page 
			String pageText = driver.findElement(By.tagName("tbody")).getText();

			// Extract the dynamic text mngrXXXX on page		
			String[] parts = pageText.split(Util.PATTERN);
			String dynamicText = parts[1];

			// Check that the dynamic text is of pattern mngrXXXX
			// First 4 characters must be "mngr"
			assertTrue(dynamicText.substring(1, 5).equals(Util.FIRST_PATTERN));
			// remain stores the "XXXX" in pattern mngrXXXX
			String remain = dynamicText.substring(dynamicText.length() - 4);
			// Check remain string must be numbers;
			assertTrue(remain.matches(Util.SECOND_PATTERN));
			
        } 
	}
	
	public static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception{
		//convert web driver to TakeScreenShot
		TakesScreenshot screenshot = ((TakesScreenshot)webdriver);
		
		//call getScreenshotAs to create img file
		File SrcFile = screenshot.getScreenshotAs(OutputType.FILE);
		
		//move img file to new dest
		File DestFile = new File(fileWithPath);
		
		//Copy File at dest
		FileUtils.copyFile(SrcFile, DestFile);
	}
	
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}
}
