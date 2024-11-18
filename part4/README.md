# Description

Part 4, apps: blog list backend.

Tools: REST Assured, Postman.

Frameworks: pytest, requests, Playwright for Python, Playwright for Java, Playwright, TestNG.

Languages: Python, Java, JavaScript.

Test cases for blog list's backend:
| ID | Description | Data | Steps | Expected Result |
| --- | --- | --- | --- | --- |
| P4-BlAPI1 | Verify POST for users (negative, short password) | Password with 1-2 characters | 1. Send a POST request to `api/users`. | Status code is 400, { 'error': 'password must be at least 3 characters long' }, user not created |
| P4-BlAPI2 | Verify POST for users (positive) | Password with 3, 4, 10 characters | 1. Send a POST request to `api/users`. | Status code is 201, new user created |
| P4-BlAPI3 | Verify POST for users (negative) | Existing username | 1. Send a POST request to `api/users`. | Status code is 400, { 'error': 'username must be unique' }, user not created |
| P4-BlAPI4 | Verify GET for users | 3 users | 1. Send a GET request to `api/users`. | Status code is 200, 3 records returned |
| P4-BlAPI5 | Verify POST for login (negative) | Invalid username | 1. Send a POST request to `api/login`. | Status code is 401, { 'error': 'invalid username or password' } |
| P4-BlAPI6 | Verify POST for login (negative) | Invalid password | 1. Send a POST request to `api/login`. | Status code is 401, { 'error': 'invalid username or password' } |
| P4-BlAPI7 | Verify POST for login (positive) | Valid credentials | 1. Send a POST request to `api/login`. | Status code is 200, token returned |
| P4-BlAPI8 | Verify POST for blogs (negative, no authentication) | - | 1. Send a POST request to `api/blogs` with correct data / not authenticated | Status code is 401 |
| P4-BlAPI9 | Verify POST for blogs (negative, no title) | - | 1. Send a POST request to `api/blogs` without title / authenticated | Status code is 400 |
| P4-BlAPI10 | Verify POST for blogs (negative, no url) |  | 1. Send a POST request to `api/blogs` without url / authenticated | Status code is 400 |
| P4-BlAPI11 | Verify POST for blogs (positive) | - | 1. Send a POST request to `api/blogs` | Status code is 201 |
| P4-BlAPI12 | Verify POST for blogs (positive, no likes) | - | 1. Send a POST request to `api/blogs` without likes | Status code is 201 and likes: 0 |
| P4-BlAPI13 | Verify GET for all blogs | - | 1. Send a GET request to `api/blogs`. | Status code is 200, number of records returned is correct, users populated |
| P4-BlAPI14 | Verify PUT for blogs (negative, no authentication) | - | 1. Send a PUT request to `api/blogs/{id}` with new likes count / not authenticated | Status code is 401 and likes count NOT updated |
| P4-BlAPI15 | Verify PUT for blogs (positive) | - | 1. Send a PUT request to `api/blogs/{id}` with new likes count | Status code is 200 and likes count updated |
| P4-BlAPI16 | Verify DELETE for blogs (negative, no authentication) | - | 1. Send a DELETE request to `api/blogs/{id}` without being authenticated | Status code is 401 and the record in NOT deleted |
| P4-BlAPI17 | Verify DELETE for blogs (positive) | - | 1. Send a DELETE request to `api/blogs/{id}` | Status code is 204 and the record is deleted |
| P4-BlAPI18 | Verify schema | - | 1. Create data in `user` and `blogs` collections and verify that GET requests return data according to schema. | Schema is correct |