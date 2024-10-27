#pytest -s
#pytest --alluredir allure-results
import pytest
import views.phonebook_app as app
import allure

test_data = [
    {'name': 'Danny', 'phone': '1234567'},
    {'name': 'DANTE', 'phone': '33-44-55-0'},
]

@allure.title("Verify entries")
@allure.description("Verification that the displayed entries are the same as in data base")
@allure.testcase("P2-Ph1")
def test_entries_are_correct(home, db_entries):
    app.verify_page_loaded(home)
    app.verify_all_entries_loaded(home, db_entries)

@allure.title("Verify add and delete functionality")
@allure.description("Verification that the new entries can be added and deleted")
@allure.feature("Add entry")
@allure.feature("Delete entry")
@allure.testcase("P2-Ph2")
@pytest.mark.parametrize("entry", test_data)
def test_add_and_delete_buttons(home, entry):
    app.test_entries = entry
    app.verify_page_loaded(home)
    app.verify_new_entry_added(home)
    app.verify_added_notification(home)
    app.verify_entry_saved_in_db(home, True)
    app.verify_duplicate_not_inserted(home)
    app.verify_update_and_cancel(home)
    app.verify_update_and_confirm(home)
    app.verify_updated_notification(home)
    app.verify_entry_saved_in_db(home, True)
    app.verify_delete_and_cancel(home)
    app.verify_delete_and_confirm(home)
    app.verify_entry_saved_in_db(home, False)
    
@allure.title("Verify filter functionality")
@allure.description("Verification that the filter works as expected")
@allure.feature("Filter")
@allure.testcase("P2-Ph3")
@pytest.mark.parametrize("entry", test_data)
def test_filter_functionality(home, db_entries, entry):
    app.test_entries = entry
    app.verify_page_loaded(home)
    app.verify_filter(home, db_entries, 'd')
    app.verify_filter(home, db_entries, 'da')
    app.verify_filter(home, db_entries, 'dan')
    app.verify_filter(home, db_entries, 'DAN')
    app.verify_new_entry_added(home)
    app.verify_added_notification(home)
    app.verify_delete_and_confirm(home)
    app.verify_filter(home, db_entries, 'NO_SUCH_NAME')
    app.verify_filter(home, db_entries, '')
