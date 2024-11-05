import { test } from '@playwright/test'
import { HomePageVerification } from './models/HomePageVerification'

test.describe('Phonebook test', () => {
  test('Verify Entries Are Correct', async ({ page }) => {
    const hpv = new HomePageVerification(page)
    await hpv.goto()
    await hpv.verifyPageLoaded()
    await hpv.verifyAllEntriesLoaded()
  })

  test('Verify Add And Delete Buttons', async ({ page }) => {
    const hpv = new HomePageVerification(page)
    await hpv.goto()
    await hpv.verifyPageLoaded()
    await hpv.verifyNewEntryAdded()
    await hpv.verifyAddNewNotification('Added')
    await hpv.verifyEntryPresentAfterRefresh(true)
    await hpv.verifyDuplicateNotInserted()
    await hpv.verifyUpdate(false) //cancel update
    await hpv.verifyUpdate(true)
    await hpv.verifyAddNewNotification('Updated')
    await hpv.verifyEntryPresentAfterRefresh(true)
    await hpv.verifyDelete(false) //cancel deletion
    await hpv.verifyDelete(true)
    await hpv.verifyEntryPresentAfterRefresh(false)
  })

  test('Verify Filter Functionality', async ({ page }) => {
    const hpv = new HomePageVerification(page)
    await hpv.goto()
    await hpv.verifyPageLoaded()
    await hpv.verifyFilter('d')
    await hpv.verifyFilter('da')
    await hpv.verifyFilter('dan')
    await hpv.verifyFilter('DAN')
    await hpv.verifyNewEntryAdded()
    await hpv.verifyAddNewNotification('Added')
    await hpv.verifyDelete(true)
    await hpv.verifyFilter('')
  })
})