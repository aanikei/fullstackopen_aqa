import requests
import allure # type: ignore

baseUrl = 'http://localhost:3003/api'

headers = { 'Content-Type': 'application/json' }

@allure.title("POST /login (negative)") # type: ignore
@allure.description("Verify POST for login (negative, invalid username)") # type: ignore
@allure.testcase("P4-BlAPI5") # type: ignore
def test_post_login_invalid_username(test_user): # type: ignore
  del test_user["name"]
  test_user['username'] = 'Test'
  
  response = requests.post(f"{baseUrl}/login", json=test_user, headers=headers)
  assert response.status_code == 401
  
  data = response.json()
  assert data == { 'error': 'invalid username or password' }


@allure.title("POST /login (negative)") # type: ignore
@allure.description("Verify POST for login (negative, invalid password)") # type: ignore
@allure.testcase("P4-BlAPI6") # type: ignore
def test_post_login_invalid_password(test_user): # type: ignore  
  del test_user["name"]
  test_user['password'] = 'Secret'
  
  response = requests.post(f"{baseUrl}/login", json=test_user, headers=headers)
  assert response.status_code == 401
  
  data = response.json()
  assert data == { 'error': 'invalid username or password' }

@allure.title("POST /login (positive)") # type: ignore
@allure.description("Verify POST for login (positive)") # type: ignore
@allure.testcase("P4-BlAPI7") # type: ignore
def test_post_login(test_user): # type: ignore 
  name = test_user.pop("name")
  
  response = requests.post(f"{baseUrl}/login", json=test_user, headers=headers)
  assert response.status_code == 200
  
  data = response.json()
  assert data["token"]
  assert data["username"] == test_user["username"]
  assert data["name"] == name