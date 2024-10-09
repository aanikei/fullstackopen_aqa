import { expect } from '@playwright/test'
import { anecdotes } from './data'

export class HomePage {
  constructor(page) {
    this.page = page
    this.appHeader = page.locator('//div[@id="root"]/div/h2')
    this.currentAnecdote = '//div[@id="root"]/div/h2/following-sibling::text()'
    this.currentVoteCount = page.getByText(/has [0-9]+ votes/i).first()
    this.mostVotedCount = page.getByText(/has [0-9]+ votes/i).last()
    this.mostVotedAnecdote = page.locator('//div[@id="root"]/div/div[3]')    
    this.voteButton = page.getByRole('button', { name: 'vote' })
    this.nextButton = page.getByRole('button', { name: 'next anecdote' })
    this.map = new Map()
  };

  async goto() {
    await this.page.goto('http://localhost:5173/')
  }

  async verifyPageLoaded() {
    await expect(this.appHeader).toHaveText('Anecdote of the day')
  }

  async getCurrentAnecdote() {
    const anecdote = await this.page.evaluate((x) => 
                    document.evaluate(x, document, null, XPathResult.STRING_TYPE).stringValue,
                    this.currentAnecdote)
    //console.log(anecdote)
    return anecdote
  }

  async getMostVotedCount() {
    const textVote = await this.mostVotedCount.textContent()
    return parseInt(textVote.replace(' votes', '').replace('has ', ''))
  }

  async getCurrentVoteCount() {
    const textVote = await this.currentVoteCount.textContent()
    return parseInt(textVote.replace(' votes', '').replace('has ', ''))
  }

  async verifyAnecdotePresentInList() {
    const anecdote = await this.getCurrentAnecdote()
    //console.log(anecdote)
    expect(anecdotes.indexOf(anecdote) > -1).toBeTruthy()
  }

  async selectNextAnecdote() {
    const tries = 3
    const initialAnecdote = await this.getCurrentAnecdote()
    for (let i = 0; i < tries; i++) {
      await this.nextButton.click()
      const currentAnecdote = await this.getCurrentAnecdote()
      if (initialAnecdote !== currentAnecdote) {
        return
      }
    }

    throw new Error('The anecdote was not changed');
  }

  async voteForAnecdote(count) {
    for (let i = 0; i < count; i++) {
      await this.voteButton.click()

      const currentAnecdote = await this.getCurrentAnecdote()
      const currentVote = this.map.get(currentAnecdote)

      // console.log(currentAnecdote)
      // console.log(currentVote)

      if (undefined !== currentVote) {
        this.map.set(currentAnecdote, currentVote + 1)
      } else {
        this.map.set(currentAnecdote, 1)
      }
    }
  }

  async verifyCurrentVoteIsCorrect(count) {
    const vote = await this.getCurrentVoteCount()
    //console.log(vote)
    expect(vote).toBe(count)
  }

  async verifyMostVotedIsCorrect() {
    const mostVotedAnecdoteEntry = [...this.map.entries()].reduce((a, b) => b[1] > a[1] ? b : a)
    const mostVotedAnecdoteMap = mostVotedAnecdoteEntry[0]
    const mostVotedCountMap = mostVotedAnecdoteEntry[1]

    // console.log(this.map)
    const mostVotedCount = await this.getMostVotedCount()
    const textVote = `has ${mostVotedCount} votes`

    const mostVotedAnecdoteText = await this.mostVotedAnecdote.textContent()
    const mostVotedAnecdote = mostVotedAnecdoteText
                                    .replace('Anecdote with most votes', '')
                                    .replace(textVote, '')

    expect(mostVotedCount).toBe(mostVotedCountMap)
    expect(mostVotedAnecdote.replace(textVote, '')).toBe(mostVotedAnecdoteMap)
  }

  async verifyNumberOfVotes() {
    const mostVoted = await this.getMostVotedCount()
    for (let i = 0; i < 100; i++) {
      await this.selectNextAnecdote()
      const currentVoteCount = await this.getCurrentVoteCount()
      if (currentVoteCount < mostVoted && currentVoteCount > 0) {
        const currentAnecdote = await this.getCurrentAnecdote()
        const savedVote = this.map.get(currentAnecdote)
        expect(currentVoteCount).toBe(savedVote)
        break
      }

      if (99 === i) {
        throw new Error('The votes on a second most voted anecdote are not the same as was recorded in saved votes');
      }
    }
  }
}