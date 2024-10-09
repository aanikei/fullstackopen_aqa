// @ts-check
const { test, expect } = require('@playwright/test')
const { HomePage } = require('./homePage')

test('anecdote is correct', async ({ page }) => {
  const homePage = new HomePage(page)
  await homePage.goto()
  await homePage.verifyPageLoaded()
  await homePage.verifyAnecdotePresentInList()
})

test('next anecdote button works', async ({ page }) => {
  const homePage = new HomePage(page)
  await homePage.goto()
  await homePage.verifyPageLoaded()
  await homePage.selectNextAnecdote()
})

test('vote functionality works', async ({ page }) => {
  const homePage = new HomePage(page)
  await homePage.goto()
  await homePage.verifyPageLoaded()

  const votes = 5
  await homePage.voteForAnecdote(votes)
  await homePage.verifyCurrentVoteIsCorrect(votes)
  await homePage.verifyMostVotedIsCorrect()

  await homePage.selectNextAnecdote()
  const newVotes = votes - 1
  await homePage.voteForAnecdote(newVotes)
  await homePage.verifyCurrentVoteIsCorrect(newVotes)
  await homePage.verifyMostVotedIsCorrect()

  await homePage.voteForAnecdote(2)
  await homePage.verifyCurrentVoteIsCorrect(newVotes + 2)
  await homePage.verifyMostVotedIsCorrect()

  await homePage.verifyNumberOfVotes()
})