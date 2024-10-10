# Description

Part 1, apps: Anecdotes.

Tools: Selenium.

Frameworks: Robot Framework, pytest, TestNG, Playwright.

Languages: Python, Java, JavaScript.

Test cases for Anecdotes app:
| ID | Description | Data | Steps | Expected Result |
| --- | --- | --- | --- | --- |
| P1-A1 | Verify an anecdote is displayed correctly | Anecdotes array | 1. Navigate to http://localhost:5173/ <br/>2. Verify that the page is loaded <br/>3. Verify that the displayed anecdote is present in the provided array | Displayed anecdote present in the provided array |
| P1-A2 | Verify that the "Next Anecdote" button works | Anecdotes array | 1. Navigate to http://localhost:5173/ <br/>2. Verify that the page is loaded <br/>3. Click on the "Next Anecdote" button several times <br/>4. Verify that the anecdote was changed | New anecdote is displayed |
| P1-A3 | Verify that the "Vote" functionality works | Anecdotes array | 1. Navigate to http://localhost:5173/ <br/>2. Verify that the page is loaded <br/>3. Click the "Vote" button N times and verify that the current vote count and most voted count were changed for the correct value <br/>4. Change the anecdote, click the "Vote" button N-1 times and verify that the old anecdote is still displayed as the most voted anecdote and the current vote count is correct <br/>5. Click the "Vote" button 2 more times and verify that the current anecdote now has the most votes <br/>6. Select the previously voted anecdote and verify that the number of its votes was not changed | Vote functionality works as expected |
