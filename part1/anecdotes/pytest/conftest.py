import pytest
from selenium import webdriver
from views.home import HomeView

@pytest.fixture
def driver():
    driver = webdriver.Chrome()
    yield driver
    driver.quit()

@pytest.fixture
def home(driver):
    return HomeView(driver)
