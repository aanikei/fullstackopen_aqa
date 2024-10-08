package com.anecdotes;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Base {
  public static WebDriver driver;

  public void init(WebDriver driver) {
    Base.driver = driver;
  }

  public WebElement waitForPresence(By locator) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
    wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    return find(locator);
  }

  public WebElement find(By locator) {
    return driver.findElement(locator);
  }
}
