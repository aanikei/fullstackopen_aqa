from selenium.webdriver.common.by import By
from views.base import BaseView
import time
from selenium.webdriver.common.keys import Keys

class HomeView(BaseView):
	url = 'http://localhost:5173/'
	app_header = (By.XPATH, '//h2[1]')
	all_entries = (By.XPATH, '//p')
	name_input = (By.XPATH, '//form/div[1]/input')
	number_input = (By.XPATH, '//form/div[2]/input')
	filter_input = (By.XPATH, '//div[1]/input')
	add_button = (By.XPATH, '//button[@type="submit"]')
	success_message = (By.CLASS_NAME, 'success')
	delete_buttons = (By.TAG_NAME, 'button')

	def get_app_header(self) -> str:
		self.driver.get(self.url)
		return self.wait_for_presence(self.app_header).text

	def get_entries(self) -> list:
		return self.find_multiple(self.all_entries)
	
	def add_new_entry(self, name: str, phone: str) -> None:
		self.find(self.name_input).send_keys(name)
		self.find(self.number_input).send_keys(phone)
		self.find(self.add_button).click()

	def get_success_message(self):
		return self.find(self.success_message).text
	
	def refresh_page(self):
		self.driver.refresh()
		
	def handle_duplicate_alert(self):
		alert = self.get_alert()
		alert_text = alert.text
		alert.accept()
		return alert_text

	def delete_last_entry(self, confirm: bool):
		self.find_multiple(self.delete_buttons)[-1].click()
		alert = self.get_alert()
		if confirm:
			alert.accept()
		else:
			alert.dismiss()

	def filter_entries(self, s: str):
		input_text_field = self.find(self.filter_input)
		input_text_field.send_keys(Keys.CONTROL + "a")
		input_text_field.send_keys(Keys.DELETE)

		if s:
			self.find(self.filter_input).send_keys(s)
			
		time.sleep(0.1)
		return [i.text.removesuffix('delete') for i in self.get_entries()]
