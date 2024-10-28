package com.phonebook.models;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Allure;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.awaitility.Awaitility;

public class HomePage {
  private final Page page;
  private final Locator appHeader;
  private final Locator nameInput;
  private final Locator numberInput;
  private final Locator filterInput;
  private final Locator addButton;
  private String name;
  private String number;
  private String newEntry;

  public HomePage(Page page) {
    this.page = page;
    this.appHeader = page.locator("//h2[1]");
    this.nameInput = page.locator("//form/div[1]/input");
	  this.numberInput = page.locator("//form/div[2]/input");
	  this.filterInput = page.getByRole(AriaRole.TEXTBOX).first();
	  this.addButton = page.locator("//button[@type=\"submit\"]");
  }

  public void navigate() {
    Allure.step(getCurrentMethodName(), step -> {
      page.navigate("http://localhost:5173/");
    });
  }

  public void verifyPageLoaded() {
    Allure.step(getCurrentMethodName(), step -> {
      assertThat(appHeader).hasText("Phonebook");
    });
  }

  public void verifyAllEntriesLoaded() {
    Allure.step(getCurrentMethodName(), step -> {
      assertEquals(DataClass.getEntriesFromDB(), DataClass.getEntriesFromPage(page));
    });
  }

  public void verifyNewEntryAdded() {
    Allure.step(getCurrentMethodName(), step -> {
      addNewEntry();
      page.getByText(newEntry).isVisible();
    });
  }

  public void verifyNotification() {
    Allure.step(getCurrentMethodName(), step -> {
      page.getByText("Added '" + name + "'").isVisible();
      //https://stackoverflow.com/questions/74598850/wait-until-element-change-state-in-playwright-with-java
      Callable<Boolean> wait = () -> page.getByText("Added '" + name + "'").isHidden();
      Awaitility.waitAtMost(Duration.ofSeconds(6)).pollDelay(Duration.ofMillis(500)).until(wait); 
    });
  }

  public void verifyEntryPresentInDb(boolean present) {
    Allure.step(getCurrentMethodName(), step -> {
      page.reload();

      if (present) {
        assertEquals(newEntry, DataClass.getEntriesFromPage(page).getLast());
      } else {
        assertNotEquals(newEntry, DataClass.getEntriesFromPage(page).getLast());
      }
    });
  }

  public void verifyDuplicateNotInserted() {
    Allure.step(getCurrentMethodName(), step -> {
      int numEntriesBefore = DataClass.getEntriesFromPage(page).size();
      page.onceDialog(dialog -> {
        assertEquals(name + " is already added to phonebook!", dialog.message());
        dialog.dismiss();
      });
      addNewEntry();
      assertEquals(numEntriesBefore, DataClass.getEntriesFromPage(page).size());
    });
  }

  public void verifyDelete(boolean confirm) {
    Allure.step(getCurrentMethodName(), step -> {
      List<Locator> buttons = page.getByRole(AriaRole.BUTTON).all();
      page.onceDialog(dialog -> {
        assertEquals("Do you really want to delete " + name + "?", dialog.message());
        if (confirm) {
          dialog.accept();
        } else {
          dialog.dismiss();
        }
        
      });
      
      buttons.getLast().click();

      if (confirm) {
        page.getByText(newEntry).isHidden();
      } else {
        page.getByText(newEntry).isVisible();
      }
    });
  }

  public void verifyFilter(String filter) {
    Allure.step(getCurrentMethodName(), step -> {
      filterInput.press("Control+KeyA");
      filterInput.press("Delete");
      filterInput.clear();

      if (!filter.isEmpty()) {
        filterInput.fill(filter);
      }

      List<String> filteredList = DataClass.getEntriesFromPage(page).stream()
              .collect(Collectors.filtering(i -> i.contains(filter), Collectors.toList()));

      List<String> filteredDb = DataClass.getEntriesFromDB().stream()
      .collect(Collectors.filtering(i -> i.contains(filter), Collectors.toList()));

      assertEquals(filteredDb, filteredList);
    });
  }

  public void setTestData(Map<String, String> map) {
    this.name = map.get("name");
    this.number = map.get("number");
    this.newEntry = name + " " + number;
  }

  private void addNewEntry() {
    nameInput.fill(name);
    numberInput.fill(number);
    addButton.click();
  }

  private String getCurrentMethodName() {
    String funcName = StackWalker.getInstance()
                      .walk(s -> s.skip(1).findFirst())
                      .get()
                      .getMethodName();

    return funcName.replaceAll("([a-z])([A-Z])", "$1 $2");
  }
}
