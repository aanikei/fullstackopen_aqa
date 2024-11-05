package org.testng.models;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataClass {
static public List<String> getEntriesFromDB() {
    String text = "";
    try {
      text = new String(Files.readAllBytes(Path.of("../db.json")), StandardCharsets.UTF_8);
    } catch (Exception e) {
      System.out.println(e.toString());
    }

    return JSONToString(text);
  }

  static private List<String> JSONToString(String json) {
    List<String> list = new ArrayList<>();

    JSONObject obj = new JSONObject(json);
    JSONArray persons = (JSONArray)obj.get("persons");
    for (Object o : persons) {
      JSONObject person = (JSONObject)o;
      String entry = person.get("name") + " " + person.get("number");
      list.add(entry);
    }

    return list;
  }
}
