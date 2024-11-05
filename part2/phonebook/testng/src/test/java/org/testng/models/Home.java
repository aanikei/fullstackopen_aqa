package org.testng.models;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class Home extends Base {
  private By appHeader = By.xpath("//h2[1]");
  private By phonebookEntries = By.xpath("//p");
  private By nameInput = By.xpath("//form/div[1]/input");
	private By numberInput = By.xpath("//form/div[2]/input");
  private By addButton = By.xpath("//button[@type=\"submit\"]");
  private By successMessage = By.className("success");
  private By allButtons = By.tagName("button");
  private By filterInput = By.xpath("//div[1]/input");

  public String getAppHeader() {
    return (waitForPresence(appHeader)).getText();
  }

  public List<String> getPhonebookEntriesText() {
    List<WebElement> elements = findMultiple(phonebookEntries);

    List<String> list = new ArrayList<>();
    for (WebElement e : elements) {
      String entry = e.getText().replace("delete","");
      list.add(entry);
    }

    return list;
  }

  public void addNewEntry(String name, String number) {
    (find(nameInput)).sendKeys(name);
    (find(numberInput)).sendKeys(number);
    (find(addButton)).click();
  }

  public String getSuccessMessage() {
    List<WebElement> list = findMultiple(successMessage);
    return list.size() > 0 ? list.get(0).getText() : null;
  }

  public void clearNewEntryForm() {
    (find(nameInput)).sendKeys(Keys.CONTROL+"A");
    (find(nameInput)).sendKeys(Keys.DELETE);
    (find(numberInput)).sendKeys(Keys.CONTROL+"A");
    (find(numberInput)).sendKeys(Keys.DELETE);
  }

  public void deleteLastEntry() {
    (findMultiple(allButtons)).getLast().click();
  }

  public void setFilter(String filter) {
    (find(filterInput)).sendKeys(Keys.CONTROL+"A");
    (find(filterInput)).sendKeys(Keys.DELETE);
    (find(filterInput)).sendKeys(filter);
  }
}
