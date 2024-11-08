package com.phonebook;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

public class PhonebookApiTest {
  private final String baseUrl = "http://localhost:3001/api/persons";

  @AfterClass
  public void cleanup() {
    Response response = get(baseUrl);
    String responseString = response.asString();

    JSONArray responseArray = new JSONArray(responseString);

    JSONObject dbObj = DataClass.loadJsonFromDB();
    JSONArray personsArray = (JSONArray) dbObj.get("persons");
    List<String> dbIds = new ArrayList<>();
    for (Object o : personsArray) {
      JSONObject object = (JSONObject) o;
      dbIds.add(object.get("id").toString());
    }

    for (Object o : responseArray) {
      JSONObject object = (JSONObject) o;
      String id = object.get("id").toString();
      if (!dbIds.contains(id)) {
                          delete(baseUrl + "/" + id);
      }
    }
  }

  //GET all
  @Test(priority = 1)
  @Description("Verify read of all records: GET /persons")
  @Severity(SeverityLevel.CRITICAL)
  public void testGetAllEntries() {
    Allure.step(getCurrentMethodName(), _ -> {
      Response response = given().
                                filter(new AllureRestAssured()).
                          get(baseUrl);
      response.then().assertThat().statusCode(200);

      String responseString = response.asString();
      Assert.assertEquals(DataClass.getEntriesFromDB(), DataClass.JSONToString(responseString));
    });
  }

  //GET single invalid id, inexistent id, valid id
  @Test(priority = 2)
  @Description("Verify read of single record with invalid id: GET /persons/{id} (invalid id)")
  public void testGetEntryInvalidId() {
    Allure.step(getCurrentMethodName(), _ -> {
                          given().
                                filter(new AllureRestAssured()).
                          when().
                                get(baseUrl + "/" + "098").
                          then().
                                statusCode(400).
                                body("error", equalTo("malformatted id"));
    });
  }

  @Test(priority = 3)
  @Description("Verify read of single record with inexistent id: GET /persons/{id} (inexistent id)")
  public void testGetEntreInexistentId() {
    Allure.step(getCurrentMethodName(), _ -> {
                          given().
                                filter(new AllureRestAssured()).
                          when().
                                get(baseUrl + "/" + "6650cc0ec000000000000000").
                          then().
                                statusCode(404);
    });
  }

  @Test(priority = 4)
  @Description("Verify read of single record with valid id: GET /persons/{id} (valid id)")
  @Severity(SeverityLevel.CRITICAL)
  public void testGetEntry() {
    Allure.step(getCurrentMethodName(), _ -> {
                          given().
                                filter(new AllureRestAssured()).
                          when().
                                get(baseUrl + "/" + "6650cc0eca1e455a28ea2fdc").
                          then().
                                statusCode(200).
                                body("name", equalTo("Marge")).
                                and().
                                body("number", equalTo("01-345678"));
    });
  }
  //End GET single

  //POST invalid data, valid data
  @Test(dataProvider = "postInvalid", priority = 5)
  @Description("Verify create will not succeed with incorrect data: POST /persons (invalid data)")
  public void testPostInvalidData(String body, String expectedResult) {
    Allure.step(getCurrentMethodName(), _ -> {
                          given().
                                filter(new AllureRestAssured()).
                                contentType("application/json").
                                body(body).
                          when().
                                post(baseUrl).
                          then().
                                statusCode(400).
                                body("error", equalTo(expectedResult));
    });
  }

  @Test(dataProvider = "postValid", priority = 6)
  @Description("Verify create will succeed with correct data: POST /persons (valid data)")
  public void testPostValidData(String body) {
    Allure.step(getCurrentMethodName(), _ -> {
      Response response = given().
                                filter(new AllureRestAssured()).
                                contentType("application/json").
                                body(body).
                          when().
                                post(baseUrl);
                                
      response.then().assertThat().statusCode(200);
      String responseString = response.asString();
      JSONObject responseJson = new JSONObject(responseString);
      responseJson.remove("id");
      Assert.assertEquals((new JSONObject(body)).toString(), responseJson.toString());
    });
  }
  //End POST

  //PUT invalid data, invalid id, valid data
  @Test(dataProvider = "putInvalid", priority = 7)
  @Description("Verify update will not succeed with incorrect data: PUT /persons (invalid data)")
  public void testPutInvalidData(String body, String expectedResult) {
    Allure.step(getCurrentMethodName(), _ -> {
      String id = createTestUser("{\"name\": \"PUT Test\", \"number\": \"000-00000\"}");
      
      JSONObject bodyJson = new JSONObject(body);
      bodyJson.put("id", id);

                          given().
                                filter(new AllureRestAssured()).
                                contentType("application/json").
                                body(bodyJson.toString()).
                          when().
                                put(baseUrl + "/" + id).
                          then().
                                statusCode(400).
                                body("error", equalTo(expectedResult));

                                delete(baseUrl + "/" + id).
                          then().
                                statusCode(204);
                                
    });
  }

  @Test(dataProvider = "putValid", priority = 8)
  @Description("Verify update will not succeed with incorrect id: PUT /persons/{id} (invalid id)")
  public void testPutInvalidId(String body) {
    Allure.step(getCurrentMethodName(), _ -> {
      String id = "0123456";
      JSONObject bodyJson = new JSONObject(body);
      bodyJson.put("id", id);

                          given().
                                filter(new AllureRestAssured()).
                                contentType("application/json").
                                body(bodyJson.toString()).
                          when().
                                put(baseUrl + "/" + id).
                          then().
                                statusCode(400).
                                body("error", equalTo("malformatted id"));
    });
  }

  @Test(dataProvider = "putValid", priority = 9)
  @Description("Verify update will succeed with correct data: PUT /persons/{id} (valid data)")
  public void testPutValidData(String body) {
    Allure.step(getCurrentMethodName(), _ -> {
      String id = createTestUser("{\"name\": \"PUT Test 2\", \"number\": \"000-00000\"}");

      JSONObject bodyJson = new JSONObject(body);
      bodyJson.put("id", id);

                          given().
                                filter(new AllureRestAssured()).
                                contentType("application/json").
                                body(bodyJson.toString()).
                          when().
                                put(baseUrl + "/" + id).
                          then().
                                statusCode(200);

                                delete(baseUrl + "/" + id).
                          then().
                                statusCode(204);
    });
  }
  //End PUT

  //DELETE invalid id, valid id
  @Test(priority = 10)
  @Description("Verify delete will return 400 with incorrect id: DELETE /persons/{id} (invalid id)")
  public void testDeleteInvalidId() {
    Allure.step(getCurrentMethodName(), _ -> {
                          given().
                                filter(new AllureRestAssured()).
                          delete(baseUrl + "/" + "0001000").
                          then().
                                statusCode(400);
    });
  }

  @Test(priority = 11)
  @Description("Verify delete will succeed with existent id: DELETE /persons/{id} (valid id)")
  public void testDeleteValidId() {
    Allure.step(getCurrentMethodName(), _ -> {
      String id = createTestUser("{\"name\": \"DELETE Test\", \"number\": \"000-00000\"}");
                          delete(baseUrl + "/" + id).
                          then().
                                statusCode(204);
    });
  }
  //End DELETE

  @DataProvider(name = "postInvalid")
  public static Object[][] postInvalidData() {
    return new Object[][] {
      { new JSONObject("{\"number\": \"040-1234556\"}").toString(), "The name is missing" },
      { new JSONObject("{\"name\": \"T\", \"number\": \"040-1234556\"}").toString(), "Person validation failed: name: Path `name` (`T`) is shorter than the minimum allowed length (3)." },
      { new JSONObject("{\"name\": \"Te\", \"number\": \"040-1234556\"}").toString(), "Person validation failed: name: Path `name` (`Te`) is shorter than the minimum allowed length (3)." },
      { new JSONObject("{\"name\": \"Test User1\"}").toString(), "The number is missing" },
      { new JSONObject("{\"name\": \"Test\", \"number\": \"12345567\"}").toString(), "Person validation failed: number: 12345567 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"Test\", \"number\": \"040-2233\"}").toString(), "Person validation failed: number: 040-2233 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"Test\", \"number\": \"-040123456\"}").toString(), "Person validation failed: number: -040123456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"Test\", \"number\": \"0-40123456\"}").toString(), "Person validation failed: number: 0-40123456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"Test\", \"number\": \"0401-23456\"}").toString(), "Person validation failed: number: 0401-23456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"Test\", \"number\": \"040-1-23456\"}").toString(), "Person validation failed: number: 040-1-23456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"Test\", \"number\": \"-040-123456\"}").toString(), "Person validation failed: number: -040-123456 is not a valid phone number!" }
    };
  }

  @DataProvider(name = "postValid")
  public static Object[][] postValidData() {
    return new Object[][] {
      { new JSONObject("{\"name\": \"Us1\", \"number\": \"040-12345\"}").toString() },
      { new JSONObject("{\"name\": \"Test User1\", \"number\": \"04-012345\"}").toString() },
      { new JSONObject("{\"name\": \"Us2\", \"number\": \"04-0123456789\"}").toString() },
      { new JSONObject("{\"name\": \"Test User2\", \"number\": \"040-123456789\"}").toString() }
    };
  }

  @DataProvider(name = "putInvalid")
  public static Object[][] putInvalidData() {
    return new Object[][] {
      { new JSONObject("{\"name\": \"PUT Test\"}").toString(), "The number is missing" },
      { new JSONObject("{\"name\": \"PUT Test\", \"number\": \"12345567\"}").toString(), "Validation failed: number: 12345567 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"PUT Test\", \"number\": \"040-2233\"}").toString(), "Validation failed: number: 040-2233 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"PUT Test\", \"number\": \"-040123456\"}").toString(), "Validation failed: number: -040123456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"PUT Test\", \"number\": \"0-40123456\"}").toString(), "Validation failed: number: 0-40123456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"PUT Test\", \"number\": \"0401-23456\"}").toString(), "Validation failed: number: 0401-23456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"PUT Test\", \"number\": \"040-1-23456\"}").toString(), "Validation failed: number: 040-1-23456 is not a valid phone number!" },
      { new JSONObject("{\"name\": \"PUT Test\", \"number\": \"-040-123456\"}").toString(), "Validation failed: number: -040-123456 is not a valid phone number!" }
    };
  }

  @DataProvider(name = "putValid")
  public static Object[][] putValidData() {
    return new Object[][] {
      { new JSONObject("{\"name\": \"PUT Test 2\", \"number\": \"040-12345\"}").toString() },
      { new JSONObject("{\"name\": \"PUT Test 2\", \"number\": \"04-012345\"}").toString() },
    };
  }

  private String createTestUser(String body) {
    Response response = given().
                                contentType("application/json").
                                body(body).
                          when().
                                post(baseUrl);
      
    response.then().assertThat().statusCode(200);
    String responseString = response.asString();
    JSONObject responseJson = new JSONObject(responseString);
    return (String)responseJson.get("id");
  }

  //https://stackoverflow.com/questions/442747/getting-the-name-of-the-currently-executing-method
  private String getCurrentMethodName() {
    String funcName = StackWalker.getInstance()
                      .walk(s -> s.skip(1).findFirst())
                      .get()
                      .getMethodName();

    return funcName.replaceAll("([a-z])([A-Z])", "$1 $2");
  }
}
