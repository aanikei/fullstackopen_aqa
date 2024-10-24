#pytest -s
import views.phonebook_app as app

def test_entries_are_correct(home, db_entries):
    app.verify_page_loaded(home)
    app.verify_all_entries_loaded(home, db_entries)

def test_add_and_delete_buttons(home):
    app.verify_page_loaded(home)
    app.verify_new_entry_added(home)
    app.verify_notification(home)
    app.verify_entry_saved_in_db(home, True)
    app.verify_duplicate_not_inserted(home)
    app.verify_delete_and_cancel(home)
    app.verify_delete_and_confirm(home)
    app.verify_entry_saved_in_db(home, False)
    
def test_filter_functionality(home, db_entries):
    app.verify_page_loaded(home)
    app.verify_filter(home, db_entries, 'd')
    app.verify_filter(home, db_entries, 'da')
    app.verify_filter(home, db_entries, 'dan')
    app.verify_new_entry_added(home)
    app.verify_notification(home)
    app.verify_delete_and_confirm(home)
    app.verify_filter(home, db_entries, '')
