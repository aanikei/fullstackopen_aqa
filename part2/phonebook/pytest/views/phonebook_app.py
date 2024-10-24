
import time
from selenium.common.exceptions import NoSuchElementException
import pytest

test_entries = [
	{'name': 'Danny', 'phone': '1234567'}
]

def verify_page_loaded(home):
    assert "Phonebook" == home.get_app_header()
    
def verify_all_entries_loaded(home, db_entries):
	page_entries = []
	for e in home.get_entries():
		page_entries.append(e.text.removesuffix('delete'))

	assert db_entries == page_entries

def verify_new_entry_added(home):
	home.add_new_entry(test_entries[0]['name'], test_entries[0]['phone'])

	new_entry = (home.get_entries()[-1]).text.removesuffix('delete')
	assert f'{test_entries[0]['name']} {test_entries[0]['phone']}' == new_entry

def verify_notification(home):
	message = home.get_success_message()
	assert message == f"Added '{test_entries[0]['name']}'"
	time.sleep(5)
	with pytest.raises(NoSuchElementException):
		home.get_success_message()

def verify_entry_saved_in_db(home, is_saved: bool):
	home.refresh_page()

	new_entry = (home.get_entries()[-1]).text.removesuffix('delete')
	if is_saved:
		assert f'{test_entries[0]['name']} {test_entries[0]['phone']}' == new_entry
	else:
		assert f'{test_entries[0]['name']} {test_entries[0]['phone']}' != new_entry

def verify_duplicate_not_inserted(home):
	num_entries_before = len(home.get_entries())
	home.add_new_entry(test_entries[0]['name'], test_entries[0]['phone'])
	alert_text = home.handle_duplicate_alert()
	assert num_entries_before == len(home.get_entries())
	assert alert_text == f'{test_entries[0]['name']} is already added to phonebook!'

def verify_delete_and_cancel(home):
	home.delete_last_entry(False)
	last_entry = (home.get_entries()[-1]).text.removesuffix('delete')
	assert f'{test_entries[0]['name']} {test_entries[0]['phone']}' == last_entry

def verify_delete_and_confirm(home):
	home.delete_last_entry(True)
	time.sleep(0.1) #fix StaleElementReferenceException: Message: stale element reference: stale element not found in the current frame
	last_entry = (home.get_entries()[-1]).text.removesuffix('delete')
	assert f'{test_entries[0]['name']} {test_entries[0]['phone']}' != last_entry

def verify_filter(home, db_entries, s: str):
	filtered_list = home.filter_entries(s)
	filtered_db = [i for i in db_entries if s in i.lower()]
	assert filtered_list == filtered_db
