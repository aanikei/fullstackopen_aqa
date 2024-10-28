package com.phonebook;

import com.phonebook.models.DataClass;
import com.phonebook.models.Entry;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.response.Response;

@Order(1)
@Tag("API")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAPI {
  private final String baseUrl = "http://localhost:3001/persons";
  private static Entry entry;
  private static final String name = "Test User1";
  private static final String number = "12345";

  @BeforeAll
  public static void setup() {
    entry = new Entry(name, number);
  }

  @Order(1)
  @Test
  @DisplayName("Get all entries in database")
  public void getAllEntries() {
    Response responce = get(baseUrl);
    responce.then().assertThat().statusCode(200);

    String responceString = responce.asString();
    assertEquals(DataClass.getEntriesFromDB(), DataClass.personsJSONArrayToList(responceString));
  }

  @Order(2)
  @Test
  @DisplayName("Create a new entry")
  public void createNewEntry() {
    Response responce = given().
                              contentType("application/json").
                              body(entry).
                        when().
                              post(baseUrl);
    responce.then().assertThat().statusCode(201);

    Entry responsEntry = responce.as(Entry.class);
    entry.setId(responsEntry.getId());
    assertEquals(name, responsEntry.getName());
    assertEquals(number, responsEntry.getNumber());
  }

  @Order(3)
  @Test
  @DisplayName("Update an entry using PUT method")
  public void updateEntry() {
    String newNumber = "098765";
    entry.setNumber(newNumber);
    Response responce = given().
                              contentType("application/json").
                              body(entry).
                        when().
                              put(baseUrl + "/{id}", entry.getId());
    responce.then().assertThat().statusCode(200);

    Entry responsEntry = responce.as(Entry.class);
    assertThat(responsEntry, samePropertyValuesAs(entry, ""));
  }

  @Order(4)
  @Test
  @DisplayName("Delete an entry")
  public void deleteEntry() {
    Response responce = given().
                              contentType("application/json").
                              body(entry).
                        when().
                              delete(baseUrl + "/{id}", entry.getId());
    responce.then().assertThat().statusCode(200).
                    header("Content-Type", "application/json").
                    body("id", equalTo(entry.getId())).
                    body("name", equalTo(entry.getName())).
                    body("number", equalTo(entry.getNumber()));

    getAllEntries();
  }
}
