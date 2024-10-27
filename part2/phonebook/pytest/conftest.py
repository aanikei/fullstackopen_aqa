import pytest
from selenium import webdriver
from views.home import HomeView
import json

@pytest.fixture
def driver():
    driver = webdriver.Chrome()
    yield driver
    driver.quit()

@pytest.fixture
def home(driver):
    return HomeView(driver)

@pytest.fixture
def db_entries_raw():
	with open('../db.json', 'r') as db:
		data = json.load(db)
		return data

@pytest.fixture
def db_entries(db_entries_raw) -> list:
	entries = []
	data = db_entries_raw
	for e in data['persons']:
		entries.append(f'{e['name']} {e['number']}')
	
	return entries

class IdStorage:
    id = None