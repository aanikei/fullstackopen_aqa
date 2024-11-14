# Description

Part 4, apps: blog list backend.

Tools: REST Assured, Postman.

Frameworks: pytest, requests, Playwright for Python, Playwright for Java, Playwright, TestNG.

Languages: Python, Java, JavaScript.

Test cases for blog list's backend:
| ID | Description | Data | Steps | Expected Result |
| --- | --- | --- | --- | --- |
| P4-BlAPI1 | Verify POST for users (negative) | Password with 2 characters | 1. Send a POST request to `api/users`. | Status code is 400, { 'error': 'password must be at least 3 characters long' }, user not created |
| P4-BlAPI1 | Verify POST for users (positive) | Password with 3 characters | 1. Send a POST request to `api/users`. | Status code is 201, new user created |
| P4-BlAPI1 | Verify POST for users (positive) | Password with 4 characters | 1. Send a POST request to `api/users`. | Status code is 201, new user created |
| P4-BlAPI1 | Verify POST for users (positive) | Password with 10 characters | 1. Send a POST request to `api/users`. | Status code is 201, new user created |
| P4-BlAPI1 | Verify POST for users (negative) | Existing username | 1. Send a POST request to `api/users`. | Status code is 400, { 'error': 'username must be unique' }, user not created |
| P4-BlAPI1 | Verify GET for users (positive) | - | 1. Send a GET request to `api/users`. | Status code is 200, 3 records returned, no blogs associated |
| P4-BlAPI1 | Verify POST for login (negative) | Invalid username | 1. Send a POST request to `api/login`. | Status code is 401, { 'error': 'invalid username or password' } |
| P4-BlAPI1 | Verify POST for login (negative) | Invalid password | 1. Send a POST request to `api/login`. | Status code is 401, { 'error': 'invalid username or password' } |
| P4-BlAPI1 | Verify POST for login (positive) | Valid credentials | 1. Send a POST request to `api/login`. | Status code is 200, token returned |
| P4-BlAPI1 | Verify POST for blogs (negative, no authentication) | - | 1. Send a POST request to `api/blogs` with correct data / not authenticated | Status code is 401 |
| P4-BlAPI1 | Verify POST for blogs (negative, no title) | - | 1. Send a POST request to `api/blogs` without title / authenticated | Status code is 400 |
| P4-BlAPI1 | Verify POST for blogs (negative, no url) |  | 1. Send a POST request to `api/blogs` without url / authenticated | Status code is 400 |
| P4-BlAPI1 | Verify POST for blogs (positive) | - | 1. Send a POST request to `api/blogs` | Status code is 201 |
| P4-BlAPI1 | Verify POST for blogs (positive) | - | 1. Send a POST request to `api/blogs` without likes | Status code is 201 and likes: 0 |
| P4-BlAPI1 | Verify GET for all blogs (positive) | - | 1. Send a GET request to `api/blogs`. | Status code is 200, 2 records returned, users populated. Schema is correct |
| P4-BlAPI1 | Verify PUT for blogs (negative, no authentication) | - | 1. Send a PUT request to `api/blogs/{id}` with new likes count / not authenticated | Status code is 401 and likes count NOT updated |
| P4-BlAPI1 | Verify PUT for blogs (positive) | - | 1. Send a PUT request to `api/blogs/{id}` with new likes count | Status code is 200 and likes count updated |
| P4-BlAPI1 | Verify DELETE for blogs (negative, no authentication) | - | 1. Send a DELETE request to `api/blogs/{id}` without being authenticated | Status code is 401 and the record in NOT deleted |
| P4-BlAPI1 | Verify DELETE for blogs (positive) | - | 1. Send a DELETE request to `api/blogs/{id}` | Status code is 200 and the record is deleted |
| P4-BlAPI1 | Verify GET for users (positive) | - | 1. Send a GET request to `api/users`. | Status code is 200, 3 records returned, with blogs associated. Schema is correct |