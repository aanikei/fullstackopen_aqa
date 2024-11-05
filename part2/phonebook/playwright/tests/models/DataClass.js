export class DataClass {
  constructor(page) {
    this.page = page
    this.db = require('../../../db.json')
  }

  async getEntriesOnPage() {
    const itemsOnPage = await this.page.getByRole('paragraph').all()
    const actualValues = []

    for (const item of itemsOnPage) {
      const text = await item.textContent()
      actualValues.push(text.replace('delete', ''))
    }

    return actualValues
  }

  getEntriedFromDB() {
    return (this.db['persons']).map(i => `${i.name} ${i.number}`)
  }
}
