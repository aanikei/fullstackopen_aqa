package com.phonebook;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataClass {
  static public JSONObject loadJsonFromDB() {
    String text = "";
    try {
      text = new String(Files.readAllBytes(Paths.get("../db.json")), StandardCharsets.UTF_8);
    } catch (Exception e) {
      System.out.println(e.toString());
    }

    return new JSONObject(text);
  }

  static public List<String> getEntriesFromDB() {
    JSONObject obj = loadJsonFromDB();
    JSONArray persons = (JSONArray) obj.get("persons");

    return JSONToString(persons);
  }

  static public List<String> JSONToString(JSONArray persons) {
    List<String> list = new ArrayList<>();
    
    for (Object o : persons) {
      JSONObject person = (JSONObject) o;
      String entry = person.get("name") + " " + person.get("number");
      list.add(entry);
    }

    return list;
  }

  static public List<String> JSONToString(String persons) {
    JSONArray jsonArray = new JSONArray(persons);
    return JSONToString((JSONArray)jsonArray);
  }
}
