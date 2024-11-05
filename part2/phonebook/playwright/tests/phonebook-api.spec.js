import { test, expect } from '@playwright/test'
const { PersonsAPI } = require('./models/PersonsAPI');

test.describe('Persons CRUD', () => {
  const baseApiUrl = 'http://localhost:3001/persons'
  const db = require('../../db.json')
  let entry = { name: 'Test User1', number: '12345'}
  const newNumber = '0987654'

  test('Get all entries in database', async ({ request }) => {
    const personsAPI = new PersonsAPI(request, baseApiUrl)
    await personsAPI.getAll(db)
  })

  test('Create new entry', async ({ request }) => {
    const personsAPI = new PersonsAPI(request, baseApiUrl)
    const newEntryId = await personsAPI.createNew(entry)
    entry = { ...entry, id: newEntryId }
  })

  test('Verify new entry present in database', async ({ request }) => {
    const personsAPI = new PersonsAPI(request, baseApiUrl)
    await personsAPI.checkEntryPresent(entry)
  })

  test('Update entry using PUT method', async ({ request }) => {
    const personsAPI = new PersonsAPI(request, baseApiUrl)
    entry = {...entry, number: newNumber }
    await personsAPI.updateExisting(entry)
  })

  test('Delete entry', async ({ request }) => {
    const personsAPI = new PersonsAPI(request, baseApiUrl)
    await personsAPI.deleteExisting(entry)
  })

  test('Confirm entry was deleted from database', async ({ request }) => {
    const personsAPI = new PersonsAPI(request, baseApiUrl)
    await personsAPI.getAll(db)
  })
})
