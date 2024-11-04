import { expect } from '@playwright/test'

export class PersonsAPI {
  constructor(request, apiUrl) {
    this.request = request
    this.apiUrl = apiUrl
    this.db = require('../../../db.json')
  }

  async getAll(db) {
    const response = await this.request.get(this.apiUrl)
    expect(response.ok()).toBeTruthy()
    expect(await response.json()).toEqual(db.persons)
  }

  async createNew(entry) {
    const response = await this.request.post(this.apiUrl, {
      data: entry
    })
    expect(response.ok()).toBeTruthy()
    const newEntry = await response.json()
    expect(newEntry).toEqual(expect.objectContaining({
      name: entry.name,
      number: entry.number
    }))
    return newEntry.id
  }

  async checkEntryPresent(entry) {
    const response = await this.request.get(this.apiUrl)
    expect(response.ok()).toBeTruthy()
    expect(await response.json()).toContainEqual(entry)
  }

  async updateExisting(entry) {
    const response = await this.request.put(`${this.apiUrl}/${entry.id}`, {
      data: entry
    })
    expect(response.ok()).toBeTruthy()
    expect(await response.json()).toEqual(entry)
  }

  async deleteExisting(entry) {
    const response = await this.request.delete(`${this.apiUrl}/${entry.id}`)
    expect(response.ok()).toBeTruthy()
    expect(await response.json()).toEqual(entry)
  }
}