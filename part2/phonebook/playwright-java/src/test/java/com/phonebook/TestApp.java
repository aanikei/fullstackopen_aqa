package com.phonebook;

import com.phonebook.models.HomePage;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Order(2)
@Tag("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestApp {
  @Order(1)
  @Test
  @DisplayName("Verify entries")
  @Description("Verification that the displayed entries are the same as in data base")
  @Severity(SeverityLevel.CRITICAL)
  public void entriesAreCorrect() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
      Page page = browser.newPage();
      HomePage homeApp = new HomePage(page);
      homeApp.navigate();
      homeApp.verifyPageLoaded();
      homeApp.verifyAllEntriesLoaded();
    }
  }

  @Order(2)
  @ParameterizedTest
  @MethodSource("dataProvider")
  @DisplayName("Verify add and delete functionality")
  @Description("Verification that the new entries can be added and deleted")
  @Feature("Add entry")
  @Feature("Delete entry")
  @Severity(SeverityLevel.NORMAL)
  public void addAndDeleteButtons(Map<String, String> data) {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
      Page page = browser.newPage();
      HomePage homeApp = new HomePage(page);
      homeApp.setTestData(data);
      homeApp.navigate();
      homeApp.verifyPageLoaded();
      homeApp.verifyNewEntryAdded();
      homeApp.verifyNotification();
      homeApp.verifyEntryPresentInDb(true);
      homeApp.verifyDuplicateNotInserted();
      homeApp.verifyDelete(false);
      homeApp.verifyDelete(true);
      homeApp.verifyEntryPresentInDb(false);
    }
  }

  @Order(3)
  @ParameterizedTest
  @MethodSource("dataProvider")
  @DisplayName("Verify filter functionality")
  @Description("Verification that the filter works as expected")
  @Feature("Filter")
  @Severity(SeverityLevel.NORMAL)
  public void filterFunctionality(Map<String, String> data) {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
      Page page = browser.newPage();
      HomePage homeApp = new HomePage(page);
      homeApp.setTestData(data);
      homeApp.navigate();
      homeApp.verifyPageLoaded();
      homeApp.verifyFilter("d");
      homeApp.verifyFilter("da");
      homeApp.verifyFilter("dan");
      homeApp.verifyFilter("DAN");
      homeApp.verifyNewEntryAdded();
      homeApp.verifyNotification();
      homeApp.verifyDelete(true);
      homeApp.verifyFilter("");
    }
  }

  private static Stream<Arguments> dataProvider() {
    Map<String, String> map = new HashMap<>();
    map.put("name", "Danny");
    map.put("number", "1234567");

    return Stream.of(Arguments.of(Map.of("name", "Danny", "number", "1234567")),
                      Arguments.of(Map.of("name", "Dante Doe", "number", "33-44-55")));
}
}
