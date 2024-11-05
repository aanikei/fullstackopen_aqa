package org.testng;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.models.Base;
import org.testng.models.Home;
import org.testng.models.HomeValidation;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class HomeTest {
  private Base base;
  private Home home;
  private HomeValidation hv;
  private WebDriver driver;
  private static String url = "http://localhost:5173/";

  @BeforeTest
  public void setUp() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.get(url);

    base = new Base();
    base.init(driver);
    home = new Home();
    hv = new HomeValidation(home);
  }

  @AfterTest
  public void tearDown() {
    driver.quit();
  }

  @Test
  @Description("Verification that the displayed entries are the same as in data base")
  @Severity(SeverityLevel.CRITICAL)
  public void testEntriesAreCorrect() {
    hv.verifyPageLoaded();
    hv.verifyAllEntriesLoaded();
  }

  @Test(dataProvider = "testData")
  @Description("Verification that the new entries can be added and deleted")
  @Severity(SeverityLevel.NORMAL)
  @Feature("Add entry")
  @Feature("Delete entry")
  public void testAddAndDeleteButtons(String name, String number) {
    hv.setTestData(name, number);
    hv.verifyPageLoaded();
    hv.verifyNewEntryAdded();
    hv.verifyNotification("Added");
    hv.verifyEntryPresentAfterRefresh(true);
    hv.verifyDuplicateNotInserted();
    hv.verifyUpdate(false); //cancel update
    hv.verifyUpdate(true);
    hv.verifyNotification("Updated");
    hv.verifyEntryPresentAfterRefresh(true);
    hv.verifyDelete(false); //cancel deletion
    hv.verifyDelete(true);
    hv.verifyEntryPresentAfterRefresh(false);
  }

  @Test(dataProvider = "testData")
  @Description("Verification that the filter works as expected")
  @Feature("Filter")
  @Severity(SeverityLevel.NORMAL)
  public void testFilterFunctionality(String name, String number) {
    hv.setTestData(name, number);
    hv.verifyPageLoaded();
    hv.verifyFilter("d");
    hv.verifyFilter("da");
    hv.verifyFilter("dan");
    hv.verifyFilter("DAN");
    hv.verifyNewEntryAdded();
    hv.verifyNotification("Added");
    hv.verifyDelete(true);
    hv.verifyFilter("");
  }

  @DataProvider(name = "testData")
  public Object[][] createData() {
    return new Object[][] {
      { "Danny", "1234567" },
      { "Dante", "0987654"}
    };
  }
}
