# Testing

### Installation
```
pip install -U pytest
pip install pytest-selenium
```

### Results
```
> pytest -s
===================================== test session starts ======================================
platform win32 -- Python 3.12.3, pytest-8.3.3, pluggy-1.5.0
sensitiveurl: .*
rootdir: C:\Users\Artur\OneDrive\Documents\fullstackopen_aqa\part1\anecdotes\pytest
plugins: base-url-2.1.0, html-4.1.1, metadata-3.1.1, selenium-4.1.0, variables-3.1.0
collected 3 items

test_anecdotes.py
DevTools listening on ws://127.0.0.1:51474/devtools/browser/b7b2795a-1615-4acf-b9fc-f5ec1f034456
.
DevTools listening on ws://127.0.0.1:51495/devtools/browser/632baa98-e196-4f7f-bdb8-5afdf7b501ef
.
DevTools listening on ws://127.0.0.1:51518/devtools/browser/faf8b2e9-3469-44bb-a59f-78562e9aa153
.

====================================== 3 passed in 23.15s ======================================
```