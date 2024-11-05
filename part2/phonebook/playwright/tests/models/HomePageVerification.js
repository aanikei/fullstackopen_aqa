import { expect } from '@playwright/test'
import { DataClass } from './DataClass'
import { HomePage } from './HomePage'

export class HomePageVerification {
  constructor(page) {
    this.page = page
    this.testUser = { name: 'Danny', number: '1234567'}
    this.testUserText = this.testUserToText()
    this.newNumber = '09876543'
  }

  async goto() {
    await this.page.goto('http://localhost:5173/')
  }

  async verifyPageLoaded() {
    const homePage = new HomePage(this.page)
    const header = await homePage.getHeader()
    await expect(header).toContainText('Phonebook')
  }

  async verifyAllEntriesLoaded() {
    const dataClass = new DataClass(this.page)
    expect(await dataClass.getEntriesOnPage()).toEqual(dataClass.getEntriedFromDB())
  }

  async verifyNewEntryAdded() {
    const homePage = new HomePage(this.page)
    await homePage.addNewEntry(this.testUser.name, this.testUser.number)
    await expect(this.page.getByText(this.testUserText)).toBeVisible()
  }

  async verifyAddNewNotification(str) {
    const notificationText = `${str} '${this.testUser.name}'`
    await expect(this.page.getByText(notificationText)).toBeVisible()
    await expect(this.page.getByText(notificationText)).toBeHidden({ timeout: 6000 })
  }

  async verifyEntryPresentAfterRefresh(present) {
    await this.page.reload()
    if (present) {
      await expect(this.page.getByText(this.testUserText)).toBeVisible()
    } else {
      await expect(this.page.getByText(this.testUserText)).toBeHidden()
    }
  }

  async verifyDuplicateNotInserted() {
    const dataClassBefore = new DataClass(this.page)
    const numEntriesBefore = await dataClassBefore.getEntriesOnPage()

    this.page.once('dialog', async dialog => {
      expect(dialog.message()).toEqual(`${this.testUser.name} is already added to phonebook!`)
      await dialog.accept()
    })

    const homePage = new HomePage(this.page)
    await homePage.addNewEntry(this.testUser.name, this.testUser.number)

    const dataClassAfter = new DataClass(this.page)
    const numEntriesAfter = await dataClassAfter.getEntriesOnPage()

    expect(numEntriesBefore).toEqual(numEntriesAfter)
  }

  async verifyUpdate(confirm) {
    this.page.once('dialog', async dialog => {
      expect(dialog.message()).toEqual(`${this.testUser.name} is already added to the phonebook, replate the ole number with a new one?`)
      await confirm ? dialog.accept() : dialog.dismiss()
    })

    const homePage = new HomePage(this.page)
    await homePage.addNewEntry(this.testUser.name, this.newNumber)

    if (confirm) {
      await expect(this.page.getByText(`${this.testUser.name} ${this.newNumber}`)).toBeVisible()
    } else {
      await expect(this.page.getByText(this.testUserText)).toBeVisible()
    }

    this.testUser = { ...this.testUser, number: this.newNumber }
    this.testUserText = this.testUserToText()
  }

  async verifyDelete(confirm) {
    this.page.once('dialog', async dialog => {
      expect(dialog.message()).toEqual(`Do you really want to delete ${this.testUser.name}?`)
      await confirm ? dialog.accept() : dialog.dismiss()
    })

    const homePage = new HomePage(this.page)
    await homePage.deleteLastEntry()

    if (confirm) {
      await expect(this.page.getByText(this.testUserText)).toBeHidden()
    } else {
      await expect(this.page.getByText(this.testUserText)).toBeVisible()
    }
  }

  async verifyFilter(str) {
    const homePage = new HomePage(this.page)
    await homePage.setFilter(str)

    const dataClass = new DataClass(this.page)
    const filteredPage = (await dataClass.getEntriesOnPage()).filter(i => i.includes(str))
    const filteredDb = (dataClass.getEntriedFromDB()).filter(i => i.includes(str))

    expect(filteredPage).toEqual(filteredDb)
  }

  testUserToText() {
    return `${this.testUser.name} ${this.testUser.number}`
  }
}
