package com.anecdotes;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
  protected Base base;
  protected Home home;
  protected HomeApp app;
  private WebDriver driver;
  private static String url = "http://localhost:5173/";

  @BeforeTest
  public void setUp() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.get(url);

    base = new Base();
    base.init(driver);
    home = new Home();
    app = new HomeApp(home);
  }

  @AfterTest
  public void tearDown() {
    driver.quit();
  }
  
  @Test
  public void testAnecdoteIsCorrect() {
    app.verifyPageLoaded();
    app.verifyAnecdotePresentInList();
  }

  @Test
  public void testNextAnecdoteButton() {
    app.verifyPageLoaded();
    app.selectNextAnecdote();
  }

  @Test
  public void testVoteFunctionality() {
    app.verifyPageLoaded();

    int votes = 5;
    app.voteForAnecdote(votes);
    app.verifyCurrentVoteIsCorrect(votes);
    app.verifyMostVotedIsCorrect();

    app.selectNextAnecdote();
    int new_votes = votes - 1;
    app.voteForAnecdote(new_votes);
    app.verifyCurrentVoteIsCorrect(new_votes);
    app.verifyMostVotedIsCorrect();

    app.voteForAnecdote(2);
    app.verifyCurrentVoteIsCorrect(new_votes + 2);
    app.verifyMostVotedIsCorrect();

    app.verifyNumberOfVotes();
  }

    // Assert.assertEquals(anecdotes[0], home.getCurrentAnecdote());

    // int tries = 5;
    // int counter = 0;
    // for (int i = 0; i < tries; i++) {
    //   home.selectNewAnecdote();
    //   String anecdote = home.getCurrentAnecdote();
    //   if (!anecdotes[0].equals(anecdote)) {
    //     break;
    //   }
    //   counter++;
    // }

    // Assert.assertNotEquals(counter, tries, "Anecdote was not changed\n");

    // int votes = 5;
    // for (int i = 0; i < votes; i++) {
    //   home.voteCurrentAnecdote();
    // }
    // Assert.assertEquals(votes, home.getVotesCount());

    // String votedAnecdote = home.getVotedAnecdote();
    // Assert.assertNotEquals(votedAnecdote, anecdotes[0]);

    // Predicate<String> predicate = s -> s.equals(votedAnecdote);
    // Assert.assertListContains(Arrays.asList(anecdotes), predicate, "votedAnecdote " + votedAnecdote);
    // //Assert.assertTrue(Arrays.asList(anecdotes).contains(votedAnecdote));

}
