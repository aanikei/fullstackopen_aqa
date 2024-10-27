
import time
from selenium.common.exceptions import NoSuchElementException
import pytest
import inspect
import allure

test_entries = None

def verify_page_loaded(home):
	with allure.step(whoami()):
		assert "Phonebook" == home.get_app_header()
    
def verify_all_entries_loaded(home, db_entries):
	with allure.step(whoami()):
		page_entries = []
		for e in home.get_entries():
			page_entries.append(e.text.removesuffix('delete'))

		assert db_entries == page_entries

def verify_new_entry_added(home):
	with allure.step(whoami()):
		home.add_new_entry(test_entries['name'], test_entries['phone'])
		# time.sleep(0.1) # reduce flakiness
		new_entry = (home.get_entries()[-1]).text.removesuffix('delete')
		assert f'{test_entries['name']} {test_entries['phone']}' == new_entry

def verify_notification(home, action: str):
	message = home.get_success_message()
	assert message == f"{action} '{test_entries['name']}'"
	time.sleep(5)
	with pytest.raises(NoSuchElementException):
		home.get_success_message()

def verify_added_notification(home):
	with allure.step(whoami()):
		verify_notification(home, 'Added')

def verify_updated_notification(home):
	with allure.step(whoami()):
		verify_notification(home, 'Updated')

def verify_entry_saved_in_db(home, is_saved: bool):
	with allure.step(whoami()):
		home.refresh_page()

		new_entry = (home.get_entries()[-1]).text.removesuffix('delete')
		if is_saved:
			assert f'{test_entries['name']} {test_entries['phone']}' == new_entry
		else:
			assert f'{test_entries['name']} {test_entries['phone']}' != new_entry

def verify_duplicate_not_inserted(home):
	with allure.step(whoami()):
		num_entries_before = len(home.get_entries())
		home.add_new_entry(test_entries['name'], test_entries['phone'])
		alert_text = home.handle_duplicate_alert()
		assert num_entries_before == len(home.get_entries())
		assert alert_text == f'{test_entries['name']} is already added to phonebook!'

def verify_delete_and_cancel(home):
	with allure.step(whoami()):
		home.delete_last_entry(False)
		last_entry = (home.get_entries()[-1]).text.removesuffix('delete')
		assert f'{test_entries['name']} {test_entries['phone']}' == last_entry

def verify_delete_and_confirm(home):
	with allure.step(whoami()):
		home.delete_last_entry(True)
		time.sleep(0.1) #fix StaleElementReferenceException: Message: stale element reference: stale element not found in the current frame
		last_entry = (home.get_entries()[-1]).text.removesuffix('delete')
		assert f'{test_entries['name']} {test_entries['phone']}' != last_entry

def verify_filter(home, db_entries, s: str):
	with allure.step(whoami()):
		filtered_list = home.filter_entries(s)
		filtered_db = [i for i in db_entries if s.lower() in i.lower()]
		assert filtered_list == filtered_db

def verify_update_and_cancel(home):
	with allure.step(whoami()):
		test_entries['phone'] = '098765'
		home.update_entry(test_entries['name'], test_entries['phone'], False)

def verify_update_and_confirm(home):
	with allure.step(whoami()):
		home.update_entry(test_entries['name'], test_entries['phone'], True)


def whoami():
	return inspect.stack()[1][3].replace('_', ' ')