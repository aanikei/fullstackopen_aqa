# Description

Part 1, apps: The Phonebook.

Tools: Selenium.

Frameworks: pytest.

Languages: Python.

Test cases for The Phonebook app:
| ID | Description | Data | Steps | Expected Result |
| --- | --- | --- | --- | --- |
| P2-Ph1 | Verify the entries are displayed correctly | Entries array | 1. Navigate to http://localhost:5173/ <br/>2. Verify that the page is loaded <br/>3. Verify that the displayed phone entries are present in the provided array | Displayed phone entries present in the provided array |
| P2-Ph2 | Verify that the "add" and "delete" buttons work | Entries array | Navigate to http://localhost:5173/ <br/>2. Verify that the page loads successfully <br/>3. Fill in the name and number input fields, then click the "add" button <br/>4. Verify that the new entry is added to the Numbers list <br/>5. Verify that the notification "Added..." is displayed for 5 seconds <br/>6. Reload the page and verify that the new record is still present in the list <br/>7. Add the same entry again and verify that a pop-up appears containing the inserted name, and that the new entry is not added to the Numbers list <br/>8. Click the "delete" button related to the new entry and verify that a pop-up alert appears <br/>9. Click the "Cancel" button and verify that the entry is still present in the list <br/>10. Click the "delete" button related to the new entry and confirm deletion <br/>11. Verify that the new record is no longer present in the list <br/>12. Reload the page and verify that the record is still not present in the list | The buttons are working |
| P2-Ph3 | Verify that the filter functionality works | Entries array | 1. Navigate to http://localhost:5173/ <br/>2. Verify that the page is loaded <br/>3. Enter 1 character into the filter field and verify that the Numbers list contains entries with this character <br/>4. Verify that filtered-out entries do not contain the character in the filter <br/>5. Perform the same checks for a 2-character filter <br/>6. Perform the same checks for a filter that leaves only one entry <br/>7. Add a new entry and verify that it is automatically added to the filtered list <br/>8. Remove an entry and verify that it is automatically removed from the filtered list <br/>9. Clear the filter and verify that the full Numbers list is displayed | The filter works as expected |
