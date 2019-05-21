package testScripts;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
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
	public void TestCase05(String username, String password) {
		/*
		 * select username input, clear
		 * input username
		 * select password input, clear
		 * input password
		 * click button
		 * try: move to alert, get text, close alert, check text
		 * catch: check manager id
		 */
		String actualTitle;
		String actualAlertMsg;
		
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(username);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("btnLogin")).click();
		
		try{
			Alert alt = driver.switchTo().alert();
			actualAlertMsg = alt.getText();
			alt.accept();
			
			assertEquals(actualAlertMsg, Util.EXPECT_ERROR);
		}
		
		catch(NoAlertPresentException Ex) {
			actualTitle = driver.getTitle();
			
			assertEquals(actualTitle, Util.EXPECT_TITLE);
		}
	}
	
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}
}
