# Description

Part 1, apps: The Phonebook.

Tools: Selenium, REST Assured.

Frameworks: pytest, requests, Playwright for Java, JUnit.

Languages: Python, Java.

Test cases for The Phonebook API:
| ID | Description | Data | Steps | Expected Result |
| --- | --- | --- | --- | --- |
| P2-PhAPI1 | Verify read action for persons | Entries array | 1. Send a GET request to `/persons`. | Status code is 200 and returned array is the same as entries array |
| P2-PhAPI2 | Verify create action for persons | `{'name': 'Test User1', 'number': '12345'}` | 1. Send a POST request to `/persons`. | Status code is 201 and returned object is the same as the sent object + id |
| P2-PhAPI3 | Verify update action for persons | `{'name': 'Test User1', 'number': '098765'}` | 1. Send a PUT request to `/persons/{id}`. | Status code is 200 and returned object has updated number |
| P2-PhAPI4 | Verify delete action for persons | None | 1. Send a DELETE request to `/persons/{id}`. | Status code is 200 and the response contains deleted record |

Test cases for The Phonebook app:
| ID | Description | Data | Steps | Expected Result |
| --- | --- | --- | --- | --- |
| P2-Ph1 | Verify the entries are displayed correctly | Entries array | 1. Navigate to http://localhost:5173/ <br/>2. Verify that the page is loaded <br/>3. Verify that the displayed phone entries are present in the provided array | Displayed phone entries present in the provided array |
| P2-Ph2 | Verify that the "add" and "delete" buttons work | Entries array | Navigate to http://localhost:5173/ <br/>2. Verify that the page loads successfully <br/>3. Fill in the name and number input fields, then click the "add" button <br/>4. Verify that the new entry is added to the Numbers list <br/>5. Verify that the notification "Added..." is displayed for 5 seconds <br/>6. Reload the page and verify that the new entry is still present in the list <br/>7. Add the same entry again and verify that a pop-up appears containing the inserted name, and that the new entry is not added to the Numbers list <br/>8. Add the entry with the same name but different phone and verify that a pop-up alert appears <br/>9. Click the "Cancel" button and verify that the existing entry was not changed <br/>10. Add the entry with the same name and confirm update <br/>11. Verify that the entry  was updated with a new number <br/>12. Verify that the notification "Updated..." is displayed for 5 seconds <br/>13. Click the "delete" button related to the new entry and verify that a pop-up alert appears <br/>14. Click the "Cancel" button and verify that the entry is still present in the list <br/>15. Click the "delete" button related to the new entry and confirm deletion <br/>16. Verify that the new entry is no longer present in the list <br/>17. Reload the page and verify that the entry is still not present in the list | The buttons are working |
| P2-Ph3 | Verify that the filter functionality works | Entries array | 1. Navigate to http://localhost:5173/ <br/>2. Verify that the page is loaded <br/>3. Enter 1 lowercase character into the filter field and verify that the Numbers list contains entries with this character <br/>4. Verify that filtered-out entries do not contain the character in the filter <br/>5. Perform the same checks for a 2-character filter <br/>6. Perform the same checks for a filter that leaves only one entry <br/>7. Perform the same checks for an uppercase filter that leaves only one entry <br/>8. Add a new entry and verify that it is automatically added to the filtered list <br/>9. Remove an entry and verify that it is automatically removed from the filtered list <br/>10. Perform the filter check for a filter that leaves no entry <br/>11. Clear the filter and verify that the full Numbers list is displayed | The filter works as expected |
