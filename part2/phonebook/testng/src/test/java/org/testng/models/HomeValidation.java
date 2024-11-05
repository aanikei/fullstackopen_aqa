package org.testng.models;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.awaitility.Awaitility;
import org.openqa.selenium.Alert;
import org.testng.Assert;

import io.qameta.allure.Allure;

public class HomeValidation {
  protected Home home;
  private String name;
  private String number;
  private String newNumber = "1-2-3-4-5";

  public HomeValidation(Home home) {
    this.home = home;
  }

  public void setTestData(String name, String number) {
    this.name = name;
    this.number = number;
  }

  public void verifyPageLoaded() {
    Allure.step(getCurrentMethodName(), _ -> {
      Assert.assertEquals(home.getAppHeader(), "Phonebook");
    });
  }

  public void verifyAllEntriesLoaded() {
    Allure.step(getCurrentMethodName(), _ -> {
      Assert.assertEquals(home.getPhonebookEntriesText(), DataClass.getEntriesFromDB());
    });
  }

  public void verifyNewEntryAdded() {
    Allure.step(getCurrentMethodName(), _ -> {
      home.addNewEntry(name, number);
      Assert.assertListContains(home.getPhonebookEntriesText(), 
                                i -> i.equals(name + " " + number), 
                                "New entry is not present in the list");
    });
  }

  public void verifyNotification(String action) {
    Allure.step(getCurrentMethodName(), _ -> {
      Assert.assertEquals(home.getSuccessMessage(), action + " '" + name + "'");
      Callable<Boolean> wait = () -> home.getSuccessMessage() == null;
      Awaitility.waitAtMost(Duration.ofSeconds(6)).pollDelay(Duration.ofMillis(500)).until(wait); 
    });
  }

  public void verifyEntryPresentAfterRefresh(boolean present) {
    Allure.step(getCurrentMethodName(), _ -> {
      Home.driver.navigate().refresh();
      if (present) {
        Assert.assertListContains(home.getPhonebookEntriesText(), 
                                  i -> i.equals(name + " " + number), 
                                  "New entry is not present in the list");
      } else {
        Assert.assertListNotContains(home.getPhonebookEntriesText(), 
                                  i -> i.equals(name + " " + number), 
                                  "New entry is still present in the list");
      }
    });
  }

  public void verifyDuplicateNotInserted() {
    Allure.step(getCurrentMethodName(), _ -> {
      home.addNewEntry(name, number);
      handleAlert(true, name + " is already added to phonebook!");
    });
  }

  public void verifyUpdate(boolean confirm) {
    Allure.step(getCurrentMethodName(), _ -> {
      number = newNumber;
      home.clearNewEntryForm();
      home.addNewEntry(name, number);
      handleAlert(confirm, name + " is already added to the phonebook, replate the ole number with a new one?");
    });
  }

  public void verifyDelete(boolean confirm) {
    Allure.step(getCurrentMethodName(), _ -> {
      home.deleteLastEntry();
      handleAlert(confirm, "Do you really want to delete " + name + "?");
    });
  }

  public void verifyFilter(String string) {
    Allure.step(getCurrentMethodName(), _ -> {
      home.setFilter(string);
      List<String> filteredPage = home.getPhonebookEntriesText()
                        .stream().filter(p -> p.contains(string.toLowerCase())).collect(Collectors.toList());
      List<String> filteredDb = DataClass.getEntriesFromDB()
                        .stream().filter(p -> p.contains(string.toLowerCase())).collect(Collectors.toList());
      Assert.assertEquals(filteredPage, filteredDb);
    });
  }

  //https://stackoverflow.com/questions/442747/getting-the-name-of-the-currently-executing-method
  private String getCurrentMethodName() {
    String funcName = StackWalker.getInstance()
                      .walk(s -> s.skip(1).findFirst())
                      .get()
                      .getMethodName();

    return funcName.replaceAll("([a-z])([A-Z])", "$1 $2");
  }

  private void handleAlert(boolean confirm, String text) {
    Alert alert = home.getAlert();
    Assert.assertEquals(alert.getText(), text);
    if (confirm) {
      alert.accept();;
    } else {
      alert.dismiss();
    }
  }
}
