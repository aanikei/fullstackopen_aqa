import pytest
import requests
import allure # type: ignore
from conftest import insert_user

baseUrl = 'http://localhost:3003/api'

headers = { 'Content-Type': 'application/json' }

post_users_short_password_data= [
  {"username": "Test user 1", "name": "Test user 1", "password": "S"}, #1
  {"username": "Test user 2", "name": "Test user 2", "password": "Se"} #2
]

post_users_valid_password_data= [
  {"username": "Test user 3", "name": "Test user 3", "password": "Sec"}, #3
  {"username": "Test user 4", "name": "Test user 4", "password": "Secr"}, #4
  {"username": "Test user 5", "name": "Test user 5", "password": "Secret1234"} #10
]

@allure.title("POST /users (negative)") # type: ignore
@allure.description("Verify POST for users (negative, short password)") # type: ignore
@allure.testcase("P4-BlAPI1") # type: ignore
@pytest.mark.parametrize("test_data", post_users_short_password_data)
def test_post_users_short_password(test_data: dict[str, str], mongo_db): # type: ignore
  response = requests.post(f"{baseUrl}/users", json=test_data, headers=headers)
  assert response.status_code == 400
  
  data = response.json()
  assert data == { 'error': 'password must be at least 3 characters long' }
  assert mongo_db['users'].count_documents({}) == 0
  
  
@allure.title("POST /users (positive)") # type: ignore
@allure.description("Verify POST for users (positive)") # type: ignore
@allure.testcase("P4-BlAPI2") # type: ignore
@pytest.mark.parametrize("test_data", post_users_valid_password_data)
def test_post_users_valid_password(test_data: dict[str, str], mongo_db): # type: ignore
  response = requests.post(f"{baseUrl}/users", json=test_data, headers=headers)
  assert response.status_code == 201
  
  data = response.json()
  assert test_data['username'] == data['username']
  assert test_data['name'] == data['name']
  assert [] == data['blogs']
  assert data['id']
  assert mongo_db['users'].count_documents({}) == 1


@allure.title("POST /users (negative)") # type: ignore
@allure.description("Verify POST for users (negative, existing username)") # type: ignore
@allure.testcase("P4-BlAPI3") # type: ignore
@pytest.mark.parametrize("test_data", post_users_valid_password_data)
def test_post_users_existing_username(test_data: dict[str, str], mongo_db): # type: ignore
  insert_user(mongo_db, test_data)

  response = requests.post(f"{baseUrl}/users", json=test_data, headers=headers)
  assert response.status_code == 400
  
  data = response.json()
  assert data == { 'error': 'username must be unique' }


@allure.title("Get /users") # type: ignore
@allure.description("Verify GET for users") # type: ignore
@allure.testcase("P4-BlAPI4") # type: ignore
def test_post_users_existing_password(mongo_db): # type: ignore
  mongo_db['users'].insert_many(post_users_valid_password_data) # type: ignore

  response = requests.get(f"{baseUrl}/users")
  assert response.status_code == 200
  
  data = response.json()
  assert len(data) == mongo_db['users'].count_documents({})