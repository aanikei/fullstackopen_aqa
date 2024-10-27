import requests
import allure

from conftest import IdStorage

baseUrl = 'http://localhost:3001/persons'
new_entry = {'name': 'Test User1', 'number': '12345'}

@allure.title("Get all entries in database")
def test_get_all_entries(db_entries_raw):
  response = requests.get(baseUrl)

  assert response.status_code == 200
  data = response.json()
  assert db_entries_raw['persons'] == data

@allure.title("Create a new entry")
def test_create_new_entry():
  response = requests.post(baseUrl, json=new_entry)

  assert response.status_code == 201
  data = response.json()
  assert data["name"] == new_entry["name"]
  assert data["number"] == new_entry["number"]
  IdStorage.id = data["id"]

@allure.title("Update an entry using PUT method")
def test_update_entry():
  new_number = {'number': '098765'}
  response = requests.put(f'{baseUrl}/{IdStorage.id}', json={**new_entry, **new_number})

  assert response.status_code == 200
  data = response.json()
  assert data["name"] == new_entry["name"]
  assert data["number"] == new_number["number"]

@allure.title("Delete an entry")
def test_delete_entry(db_entries_raw):
  response = requests.delete(f'{baseUrl}/{IdStorage.id}')

  assert response.status_code == 200
  test_get_all_entries(db_entries_raw)