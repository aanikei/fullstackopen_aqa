# Testing

### Installation
```
pip install robotframework --user
pip install --upgrade robotframework-seleniumlibrary
```

### Results
```
> robot -d .\results\ tests\anecdotes.robot
==============================================================================
Anecdotes
==============================================================================
Verify an anecdote is present                                         | PASS |
------------------------------------------------------------------------------
Verify that the "Next Anecdote" button works                          | PASS |
------------------------------------------------------------------------------
Verify that the "Vote" functionality works                            | PASS |
------------------------------------------------------------------------------
Anecdotes                                                             | PASS |
3 tests, 3 passed, 0 failed
==============================================================================
```