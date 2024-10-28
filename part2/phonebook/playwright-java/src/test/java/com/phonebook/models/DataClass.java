package com.phonebook.models;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class DataClass {
  static public List<String> getEntriesFromPage(Page page) {
    List<String> list = new ArrayList<>();
    for (Locator l : page.getByRole(AriaRole.PARAGRAPH).all()) {
      String entry = l.textContent().replace("delete","");
      list.add(entry);
    }

    return list;
  }

  static public List<String> getEntriesFromDB() {
    String text = "";
    try {
      text = new String(Files.readAllBytes(Paths.get("../db.json")), StandardCharsets.UTF_8);
    } catch (Exception e) {
      System.out.println(e.toString());
    }

    return JSONToString(text);
  }

  static public List<String> JSONToString(String json) {
    List<String> list = new ArrayList<>();

    JSONObject obj = new JSONObject(json);
    JSONArray persons = (JSONArray) obj.get("persons");
    for (Object o : persons) {
      JSONObject person = (JSONObject) o;
      String entry = person.get("name") + " " + person.get("number");
      list.add(entry);
    }

    return list;
  }

  static public List<String> personsJSONArrayToList(String json) {
    List<String> list = new ArrayList<>();

    JSONArray persons = new JSONArray(json);
    for (Object o : persons) {
      JSONObject person = (JSONObject) o;
      String entry = person.get("name") + " " + person.get("number");
      list.add(entry);
    }

    return list;
  }
}
