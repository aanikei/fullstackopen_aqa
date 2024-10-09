package com.anecdotes;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import org.testng.Assert;

public class HomeApp {
  private static final Set<String> ANECDOTES = Set.of(
    "If it hurts, do it more often.",
    "Adding manpower to a late software project makes it later!",
    "The first 90 percent of the code accounts for the first 90 percent of the development time...The remaining 10 percent of the code accounts for the other 90 percent of the development time.",
    "Any fool can write code that a computer can understand. Good programmers write code that humans can understand.",
    "Premature optimization is the root of all evil.",
    "Debugging is twice as hard as writing the code in the first place. Therefore, if you write the code as cleverly as possible, you are, by definition, not smart enough to debug it.",
    "Programming without an extremely heavy use of console.log is same as if a doctor would refuse to use x-rays or blood tests when diagnosing patients.",
    "The only way to go fast, is to go well."
  );

  protected Home home;

  public HomeApp(Home home) {
    this.home = home;
  }

  public void verifyPageLoaded() {
    Assert.assertEquals(home.getAppHeader(), "Anecdote of the day");
  }

  public void verifyAnecdotePresentInList() {
    Assert.assertTrue(ANECDOTES.contains(home.getCurrentAnecdote()));
  }

  public void selectNextAnecdote() {
    int tries = 3;
    String initialAnecdote = home.getCurrentAnecdote();
    for (int i = 0; i < tries; i++) {
      home.selectNewAnecdote();
      String currentAnecdote = home.getCurrentAnecdote();
      if (!initialAnecdote.equals(currentAnecdote)) {
        return;
      }
    }

    Assert.fail("The anecdote was not changed");
  }

  void voteForAnecdote(int count) {
    for (int i = 0; i < count; i++) {
      home.voteCurrentAnecdote();
    }
  }

  void verifyCurrentVoteIsCorrect(int count) {
    Assert.assertEquals(home.getCurrentVoteCount(), count);
  }

  void verifyMostVotedIsCorrect() {
    HashMap <String, Integer> votesMap = home.getVotesMap();
    String anecdote = Collections.max(votesMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    int votes = votesMap.get(anecdote);
    Assert.assertEquals(home.getMostVotedAnecdote(), anecdote);
    Assert.assertEquals(home.getMostVotedCount(), votes);
  }

  void verifyNumberOfVotes() {
    int mostVoted = home.getMostVotedCount();
    for (int i = 0; i < 100; i++) {
      home.selectNewAnecdote();
      int currentVote = home.getCurrentVoteCount();
      if (currentVote < mostVoted) {
        if (currentVote > 0) {
          String currentAnecdote = home.getCurrentAnecdote();
          int savedVote = home.getVotesMap().get(currentAnecdote);
          Assert.assertEquals(currentVote, savedVote);
          break;
        }
      }

      if (99 == i) {
        Assert.fail("The votes on a second most voted anecdote are not the same as was recorded in saved votes");
      }
    }
  }
}
