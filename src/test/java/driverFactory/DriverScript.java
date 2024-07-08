package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;


public class DriverScript {
	public static WebDriver driver;
	String inputpath ="./FileInput/Controller.xlsx";
	String outputpath ="./FileOutput/Erpresults.xlsx";
	String TCSheet ="MasterTestCases";
	String TCModule ="";
	ExtentReports reports;
	ExtentTest logger;
	public void startTest() throws Throwable
	{
		String Module_status="";
		String Module_new="";
		//create object for Excelfileutil
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		//iterate all rows in TCSheet
		for(int i=1;i<=xl.rowcount(TCSheet);i++)
		{
			if(xl.getcelldata(TCSheet, i, 2).equalsIgnoreCase("Y"))
			{
				
				//store corresponding sheet or testcases into TCModule
				TCModule =xl.getcelldata(TCSheet, i, 1);
				//define path for html report
				reports = new ExtentReports("./target/ExtentReports/"+TCModule+FunctionLibrary.generatedate()+".html");
				logger =reports.startTest(TCModule);
				logger.assignAuthor("Harsha");
				//iterate corresponding sheet
				for(int j=1;j<=xl.rowcount(TCModule);j++)
				{
					//read each cell from TCModule sheet
					String Description = xl.getcelldata(TCModule, j, 0);
					String Object_type = xl.getcelldata(TCModule, j, 1);
					String Ltype = xl.getcelldata(TCModule, j, 2);
					String Lvalue = xl.getcelldata(TCModule, j, 3);
					String TData = xl.getcelldata(TCModule, j, 4);
					try {
						if(Object_type.equalsIgnoreCase("startBrowser"))
						{
							driver =FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(Ltype, Lvalue, TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(Ltype, Lvalue, TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibrary.dropdownaction(Ltype, Lvalue, TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("capstock"))
						{
							FunctionLibrary.capstock(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibrary.stocktable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("capsup"))
						{
							FunctionLibrary.capsup(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						
						if(Object_type.equalsIgnoreCase("suppliertable"))
						{
							FunctionLibrary.suppliertable();
							logger.log(LogStatus.INFO, Description);
							
						}
						if(Object_type.equalsIgnoreCase("captureCustomer"))
						{
							FunctionLibrary.capturecustomer(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						//write a spass into status cell TCModule
						xl.setcelldata(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_status="True";
					}catch(Exception e)
					{
						System.out.println(e.getMessage());
						//write as Fail into status cell TCModule
						xl.setcelldata(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_new="False";
						File screen =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File("./target/Screenshot/"+TCModule+Description+FunctionLibrary.generatedate()+".png"));
					}
					reports.endTest(logger);
					reports.flush();
					if(Module_status.equalsIgnoreCase("True"))
					{
						//write as pass into TCSheet
						xl.setcelldata(TCSheet, i, 3, "Pass", outputpath);
					}
					if(Module_new.equalsIgnoreCase("False"))
					{
						//write as Fail into TCSheet
						xl.setcelldata(TCSheet, i, 3, "Fail", outputpath);
					}
				}
			}
			else
			{
				//write as blocked into status cell in TCSheet
				xl.setcelldata(TCSheet, i, 3, "Blocked", outputpath);
			}
		}
	}
}