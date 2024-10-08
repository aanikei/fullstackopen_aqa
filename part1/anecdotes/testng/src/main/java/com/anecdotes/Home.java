package com.anecdotes;

import java.util.HashMap;

import org.openqa.selenium.By;

public class Home extends Base {
  private By appHeader = By.xpath("//div[@id='root']/div/h2");
  private By votedCountText = By.xpath("//div[@id='root']/div/div[3]/div");
  private By votedAnecdote = By.xpath("//div[@id='root']/div/div[3]");
  private By allText = By.xpath("//div[@id='root']");
  private By voteButton = By.xpath("//button[text()=\"vote\"]");
  private By nextButton = By.xpath("//button[text()=\"next anecdote\"]");

  private HashMap <String, Integer> hMap = new HashMap<>();

  private String getWholePageText() {
    String text = find(allText).getText();
    return text;
  }

  private int votesStringToInt(String votes) {
    String strippedVotes = votes.replace("has ","").replace(" votes","");
    return Integer.parseInt(strippedVotes);
  }

  public HashMap<String, Integer> getVotesMap () {
    return hMap;
  }

  public String getAppHeader() {
    return (waitForPresence(appHeader)).getText();
  }

  public void selectNewAnecdote() {
    find(nextButton).click();
  }

  public void voteCurrentAnecdote() {
    find(voteButton).click();
    Integer value = hMap.get(getCurrentAnecdote());
    if (null != value) {
      hMap.put(getCurrentAnecdote(), ++value);
    } else {
      hMap.put(getCurrentAnecdote(), 1);
    }
  }

  public String getCurrentAnecdote() {
    String text = getWholePageText();
    return text.split("\n")[1];
  }

  public int getCurrentVoteCount() {
    String text = getWholePageText();
    String votesString = text.split("\n")[2];
    return votesStringToInt(votesString);
  }
  
  public String getMostVotedAnecdote() {
    String anecdote = find(votedAnecdote).getText();
    String votes = find(votedCountText).getText();
    return anecdote
        .replace("Anecdote with most votes\n" ,"")
        .replace("\n" + votes, "");
  }

  public int getMostVotedCount() {
    String votes = find(votedCountText).getText();
    return votesStringToInt(votes);
  }

  
}
