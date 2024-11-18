#pip install pytest requests allure-pytest pymongo[snappy,gssapi,srv,tls] dnspython python-dotenv bcrypt
#pip install pyjwt jsonschema
#pytest --alluredir allure-results
#allure serve allure-results
import pytest
import requests
import allure # type: ignore
from bson import ObjectId

baseUrl = 'http://localhost:3003/api'

post_blogs= [
  { "title": "Test Blog 1", "author": "Test Author 1", "url": "http://localhost:123", "likes": 0 },
  { "title": "Test Blog 2", "author": "Test Author 2", "url": "http://localhost:456", "likes": 1 },
  { "title": "Test Blog 3", "author": "Test Author 3", "url": "http://localhost:789", "likes": 2 }
]

@allure.title("POST /blogs (negative)") # type: ignore
@allure.description("Verify POST for blogs (negative, no authentication)") # type: ignore
@allure.testcase("P4-BlAPI8") # type: ignore
def test_post_blogs_no_authentication(mongo_db): # type: ignore  
  response = requests.post(f"{baseUrl}/blogs", json=post_blogs[0])

  assert response.status_code == 401
  assert 0 == mongo_db['blogs'].count_documents({})


@allure.title("POST /blogs (negative)") # type: ignore
@allure.description("Verify POST for blogs (negative, no title)") # type: ignore
@allure.testcase("P4-BlAPI9") # type: ignore
def test_post_blogs_no_title(mongo_db, test_user_jwt): # type: ignore
  token = test_user_jwt
  blog = post_blogs[0].copy()
  del blog['title']
  response = requests.post(f"{baseUrl}/blogs", json=blog, headers={ 'Authorization': f'Bearer {token}'})
  
  assert response.status_code == 400
  assert 0 == mongo_db['blogs'].count_documents({})

@allure.title("POST /blogs (negative)") # type: ignore
@allure.description("Verify POST for blogs (negative, no url)") # type: ignore
@allure.testcase("P4-BlAPI10") # type: ignore
def test_post_blogs_no_url(mongo_db, test_user_jwt): # type: ignore
  token = test_user_jwt
  blog = post_blogs[0].copy()
  del blog['url']
  response = requests.post(f"{baseUrl}/blogs", json=blog, headers={ 'Authorization': f'Bearer {token}'})
  
  assert response.status_code == 400
  assert 0 == mongo_db['blogs'].count_documents({})

@allure.title("POST /blogs (positive)") # type: ignore
@allure.description("Verify POST for blogs (positive)") # type: ignore
@allure.testcase("P4-BlAPI11") # type: ignore
def test_post_blogs(mongo_db, test_user_jwt): # type: ignore
  token = test_user_jwt
  blog = post_blogs[0].copy()
  
  response = requests.post(f"{baseUrl}/blogs", json=blog, headers={ 'Authorization': f'Bearer {token}'})
  
  assert response.status_code == 201
  assert 1 == mongo_db['blogs'].count_documents({})

@allure.title("POST /blogs (positive, no likes)") # type: ignore
@allure.description("Verify POST for blogs (positive, no likes)") # type: ignore
@allure.testcase("P4-BlAPI12") # type: ignore
def test_post_blogs_no_likes(mongo_db, test_user_jwt): # type: ignore
  token = test_user_jwt
  blog = post_blogs[0].copy()
  del blog['likes']

  response = requests.post(f"{baseUrl}/blogs", json=blog, headers={ 'Authorization': f'Bearer {token}'})
  
  assert response.status_code == 201
  blog_db = mongo_db['blogs'].find_one()
  assert 0 == blog_db['likes']

@allure.title("GET /blogs") # type: ignore
@allure.description("Verify GET for all blogs") # type: ignore
@allure.testcase("P4-BlAPI13") # type: ignore
def test_get_blogs(mongo_db): # type: ignore
  mongo_db['blogs'].insert_many(post_blogs)
  response = requests.get(f"{baseUrl}/blogs")

  assert response.status_code == 200
  assert len(post_blogs) == mongo_db['blogs'].count_documents({})

@allure.title("PUT /blogs (negative)") # type: ignore
@allure.description("Verify PUT for blogs (negative, no authentication)") # type: ignore
@allure.testcase("P4-BlAPI14") # type: ignore
@pytest.mark.xfail(reason="authentication not working")
def test_put_blogs_no_authentication(mongo_db): # type: ignore
  blog = post_blogs[0].copy()

  insert_result = mongo_db['blogs'].insert_one(blog)
  id = str(insert_result.inserted_id)
  
  blog['likes'] = 100
  blog["_id"] = str(blog["_id"])
  response = requests.put(f"{baseUrl}/blogs/{id}", json=blog)

  assert response.status_code == 401
  record = mongo_db['blogs'].find_one({ "_id": ObjectId(id) })
  assert record['likes'] == post_blogs[0]['likes']

@allure.title("PUT /blogs (positive)") # type: ignore
@allure.description("Verify PUT for blogs (positive, with authentication)") # type: ignore
@allure.testcase("P4-BlAPI15") # type: ignore
@pytest.mark.xfail(reason="authentication not working")
def test_put_blogs(mongo_db, test_user_jwt): # type: ignore
  token = test_user_jwt
  #insert blog with user field present
  blog = post_blogs[0].copy()
  user = mongo_db['users'].find_one()
  blog["user"] = str(user['_id'])
  blog_insert_result = mongo_db['blogs'].insert_one(blog)
  id = str(blog_insert_result.inserted_id)
  #update user's blogs field
  mongo_db['users'].find_one_and_update({"_id": user["_id"]}, {'$set': {'blogs': blog_insert_result.inserted_id}})
  
  blog["likes"] = 100
  blog["_id"] = str(blog["_id"])
  response = requests.put(f"{baseUrl}/blogs/{id}", json=blog, headers={ 'Authorization': f'Bearer {token}'})

  assert response.status_code == 200
  record = mongo_db['blogs'].find_one({ "_id": blog_insert_result.inserted_id })
  assert record['likes'] == blog['likes']

@allure.title("DELETE /blogs (negative)") # type: ignore
@allure.description("Verify PUT for blogs (negative, no authentication)") # type: ignore
@allure.testcase("P4-BlAPI16") # type: ignore
def test_delete_blogs_no_authentication(mongo_db): # type: ignore
  blog = post_blogs[0].copy()

  insert_result = mongo_db['blogs'].insert_one(blog)
  id = str(insert_result.inserted_id)
  
  response = requests.delete(f"{baseUrl}/blogs/{id}")

  assert response.status_code == 401
  assert 1 == mongo_db['blogs'].count_documents({})

@allure.title("DELETE /blogs (positive)") # type: ignore
@allure.description("Verify PUT for blogs (positive, with authentication)") # type: ignore
@allure.testcase("P4-BlAPI17") # type: ignore
def test_delete_blogs_with_authentication(mongo_db, test_user_jwt): # type: ignore
  token = test_user_jwt
  #insert blog with user field present
  blog = post_blogs[0].copy()
  user = mongo_db['users'].find_one()
  blog["user"] = str(user['_id'])
  blog_insert_result = mongo_db['blogs'].insert_one(blog)
  id = str(blog_insert_result.inserted_id)
  #update user's blogs field
  mongo_db['users'].find_one_and_update({"_id": user["_id"]}, {'$set': {'blogs': blog_insert_result.inserted_id}})
    
  response = requests.delete(f"{baseUrl}/blogs/{id}", headers={ 'Authorization': f'Bearer {token}'})

  assert response.status_code == 204
  assert 0 == mongo_db['blogs'].count_documents({})