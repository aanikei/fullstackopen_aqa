import requests
import allure # type: ignore
from jsonschema import validate

blog= { 
            "title": "Test Blog 1", 
            "author": "Test Author 1", 
            "url": "http://localhost:123", 
            "likes": 0 }

baseUrl = 'http://localhost:3003/api'

blog_schema = {
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "title": "Blog",
  "description": "A record with title/author and url",
  "type": "object",
  "properties": {
    "title": { "type": "string" },
    "author": { "type": "string" },
    "url": { "type": "string" },
    "likes": { "type": "integer" },
    "id": { "type": "string" },
    "user": { 
      "type": "object",
      "properties": {
        "username": { "type": "string" },
        "name": { "type": "string" },
        "id": { "type": "string" }
      },
      "required": [ "username", "name", "id" ]
    },
  },
  "required": [ "title", "author", "url", "likes", "id", "user" ]
}

user_schema = {
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "title": "User",
  "description": "User that creates blog entries",
  "type": "object",
  "properties": {
    "username": { "type": "string" },
    "name": { "type": "string" },
    "id": { "type": "string" },
    "blogs": { 
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "title": { "type": "string" },
          "author": { "type": "string" },
          "url": { "type": "string" },
          "id": { "type": "string" }
        },
        "required": [ "title", "author", "url", "id" ]
      },
      "minItems": 0
    },
  },
  "required": [ "username", "name", "id" ]
}

@allure.title("Schema validation") # type: ignore
@allure.description("Verify valid objects returned by GET /blogs and /users") # type: ignore
@allure.testcase("P4-BlAPI15") # type: ignore
def test_schema(mongo_db, test_user): # type: ignore
  #insert blog with user field present
  user = mongo_db['users'].find_one()
  blog["user"] = str(user['_id'])
  blog_insert_result = mongo_db['blogs'].insert_one(blog)
  id = str(blog_insert_result.inserted_id)
  #update user's blogs field
  mongo_db['users'].find_one_and_update({"_id": user["_id"]}, {'$set': {'blogs': blog_insert_result.inserted_id}})
  
  response_blogs = requests.get(f"{baseUrl}/blogs")
  response_blogs_json = response_blogs.json()

  response_users = requests.get(f"{baseUrl}/users")
  response_users_json = response_users.json()

  validate(instance=response_blogs_json[0], schema=blog_schema)
  validate(instance=response_users_json[0], schema=user_schema)
  

  