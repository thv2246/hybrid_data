package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static Properties conpro;
	public static WebDriver dr;
	public static WebDriver startBrowser() throws Throwable {
		conpro = new Properties();
		conpro.load(new FileInputStream("./PropertyFile/Environment.properties"));
		if (conpro.getProperty("Browser").equalsIgnoreCase("chrome")) {
			dr = new ChromeDriver();
			dr.manage().window().maximize();
		}if (conpro.getProperty("Browser").equalsIgnoreCase("firefox")) {
			dr = new FirefoxDriver();	
		}if (conpro.getProperty("Browser").equalsIgnoreCase("edge")) {
			dr = new EdgeDriver();

		}else {
			Reporter.log("Browser value is not matching ");
		}
		return dr;
	}
	public static void openUrl() {
		dr.get(conpro.getProperty("Url"));
	}
	public static void waitForElement(String locatortype, String locatorvalue, String testdata) {
		WebDriverWait mywait = new  WebDriverWait(dr,Duration.ofSeconds(Integer.parseInt(testdata)));
		if (locatortype.equalsIgnoreCase("name")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
		}if (locatortype.equalsIgnoreCase("xpath")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
		}if (locatortype.equalsIgnoreCase("id")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
		}
	}
	public static void typeAction(String locatortype, String locatorvalue, String testdata ) {
		if (locatortype.equalsIgnoreCase("name")) {
			dr.findElement(By.name(locatorvalue)).clear();
			dr.findElement(By.name(locatorvalue)).sendKeys(testdata);
		}if (locatortype.equalsIgnoreCase("id")) {
			dr.findElement(By.id(locatorvalue)).clear();
			dr.findElement(By.id(locatorvalue)).sendKeys(testdata);
		}if (locatortype.equalsIgnoreCase("xpath")) {
			dr.findElement(By.xpath(locatorvalue)).clear();
			dr.findElement(By.xpath(locatorvalue)).sendKeys(testdata);		
		}
	}
	public static void clickAction(String locatortype, String locatorvalue) {
		if (locatortype.equalsIgnoreCase("name")) {
			dr.findElement(By.name(locatorvalue)).click();	
		}if (locatortype.equalsIgnoreCase("id")) {
			dr.findElement(By.id(locatorvalue)).click();
		}if (locatortype.equals("xpath")) {
			dr.findElement(By.xpath(locatorvalue)).click();
		}	
	}

	public static void validateTitle(String exp_title) {
		String  act_title = dr.getTitle();
		try {
			Assert.assertEquals(act_title, exp_title, "Title is not matching");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

	public static void closeBrowser() {
		dr.quit();
	}
	public  static String generatedate() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("DD_MM_YYYY hh_mm");
		return df.format(date);
	}
	public static void dropdownaction(String locatortype,String locatorvalue, String testdata ) {
		if (locatortype.equalsIgnoreCase("xpath")) {
			int value = Integer.parseInt(testdata);
			Select num  = new Select(dr.findElement(By.xpath(locatorvalue)));
			num.selectByIndex(value);
		}if (locatortype.equalsIgnoreCase("id")) {
			int value = Integer.parseInt(testdata);
			Select num  = new Select(dr.findElement(By.id(locatorvalue)));
			num.selectByIndex(value);
		}if (locatortype.equalsIgnoreCase("name")) {
			int value = Integer.parseInt(testdata);
			Select num  = new Select(dr.findElement(By.name(locatorvalue)));
			num.selectByIndex(value);
		}
	}
	public static void capstock(String locatortype, String locatorvalue) throws Throwable {
		String stock_num = "";
		if (locatortype.equalsIgnoreCase("name")) {
			stock_num = dr.findElement(By.name(locatorvalue)).getAttribute("Value");
		}if (locatortype.equalsIgnoreCase("id")) {
			stock_num = dr.findElement(By.id(locatorvalue)).getAttribute("Value");
		}if (locatortype.equalsIgnoreCase("xpath")) {
			stock_num = dr.findElement(By.xpath(locatorvalue)).getAttribute("Value");
		}
		FileWriter fw = new FileWriter("./CaptureData/stocknumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stock_num);
		bw.flush();
		bw.close();
	}
	public static void stocktable() throws Throwable {
		FileReader fr = new FileReader("./Capturedata/stocknumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String expdata = br.readLine();
		if (!dr.findElement(By.xpath(conpro.getProperty("searchbox"))).isDisplayed()) {
			dr.findElement(By.xpath(conpro.getProperty("searchpanel"))).click();
			dr.findElement(By.xpath(conpro.getProperty("searchbox"))).clear();
			dr.findElement(By.xpath(conpro.getProperty("searchbox"))).sendKeys(expdata);
			Thread.sleep(3000);
			dr.findElement(By.xpath(conpro.getProperty("searchbutton"))).click();
			Thread.sleep(4000);
			String actdata = dr.findElement(By.xpath("//table[@class = 'table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
			Reporter.log(actdata+ " "+expdata,true);
			try {
				Assert.assertEquals(actdata, expdata, "stock number not found");
			} catch (Throwable t) {
				System.out.println(t.getMessage());
			}
		}

	}
	public static void capsup(String locatortype, String locatorvalue) throws Throwable
	{
		String supplier_num = "";
		if (locatortype.equalsIgnoreCase("xpath")) {
			supplier_num = dr.findElement(By.xpath(locatorvalue)).getAttribute("Value");
		}if (locatortype.equalsIgnoreCase("id")) {
			supplier_num = dr.findElement(By.id(locatorvalue)).getAttribute("Value");
		}if (locatortype.equalsIgnoreCase("name")) {
			supplier_num = dr.findElement(By.name(locatorvalue)).getAttribute("Value");
		}
		FileWriter fw = new FileWriter("./CaptureData/suppliernumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(supplier_num);
		bw.flush();
		bw.close();


	}
	public static void suppliertable() throws Throwable {
		FileReader fr = new FileReader("./CaptureData/suppliernumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String expdata = br.readLine();
		if (!dr.findElement(By.xpath(conpro.getProperty("searchbox"))).isDisplayed()) {
			dr.findElement(By.xpath(conpro.getProperty("searchpanel"))).click();
			dr.findElement(By.xpath(conpro.getProperty("searchbox"))).clear();
			dr.findElement(By.xpath(conpro.getProperty("searchbox"))).sendKeys(expdata);
			Thread.sleep(3000);
			dr.findElement(By.xpath(conpro.getProperty("searchbutton"))).click();
			Thread.sleep(3000);
			String actdata = dr.findElement(By.xpath("//table[@class = 'table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
			Reporter.log(actdata+ " "+expdata,true);
			try {
				Assert.assertEquals(actdata, expdata, "supplier number not found");
			} catch (Throwable t) {
				System.out.println(t.getMessage());
			}
		}
	}
	public static void capturecustomer(String locatortype, String locatorvalue) throws Throwable 
	{
		String customer_num = "";
		if (locatortype.equalsIgnoreCase("xpath")) {
			customer_num = dr.findElement(By.xpath(locatorvalue)).getAttribute("Value");
		}if (locatortype.equalsIgnoreCase("id")) {
			customer_num = dr.findElement(By.id(locatorvalue)).getAttribute("Value");
		}if (locatortype.equalsIgnoreCase("name")) {
			customer_num = dr.findElement(By.name(locatorvalue)).getAttribute("Value");
		}
		FileWriter fw = new FileWriter("./CaptureData/customernumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(customer_num);
		bw.flush();
		bw.close();

	}
	public static void customerTable() throws Throwable 
	{
		FileReader fr = new FileReader("./CaptureData/customernumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String expdata = br.readLine();
		if (!dr.findElement(By.xpath(conpro.getProperty("searchbox"))).isDisplayed()) {
			dr.findElement(By.xpath(conpro.getProperty("searchpanel"))).click();
			dr.findElement(By.xpath(conpro.getProperty("searchbox"))).clear();
			dr.findElement(By.xpath(conpro.getProperty("searchbox"))).sendKeys(expdata);
			Thread.sleep(3000);
			dr.findElement(By.xpath(conpro.getProperty("searchbutton"))).click();
			Thread.sleep(3000);
			String actdata = dr.findElement(By.xpath("//table[@class = 'table ewTable']/tbody/tr[1]/td[4]/div/span/span")).getText();
			Reporter.log(actdata+ " "+expdata,true);
			try {
				Assert.assertEquals(actdata, expdata, "customer number not found");
			} catch (Throwable t) {
				System.out.println(t.getMessage());
			}
		}
	}
}


