# Testing

### Results
```
> mvn package
...
[INFO] --- surefire:3.2.5:test (default-test) @ anecdotes ---
[INFO] Using auto detected provider org.apache.maven.surefire.testng.TestNGProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.anecdotes.BaseTest
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Oct 08, 2024 10:26:04 AM org.openqa.selenium.devtools.CdpVersionFinder findNearestMatch
WARNING: Unable to find an exact match for CDP version 129, returning the closest version; found: 127; Please update to a Selenium version that supports CDP version 129
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.959 s -- in com.anecdotes.BaseTest
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- jar:3.4.1:jar (default-jar) @ anecdotes ---
[INFO] Building jar: C:\Users\Artur\OneDrive\Documents\fullstackopen_aqa\part1\anecdotes\testng\target\anecdotes-1.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
...
```