export class HomePage {
  constructor(page) {
    this.page = page
    
    this.appHeader = page.locator('//h2[1]')

    this.nameInput = page.locator('//form/div[1]/input')
	  this.numberInput = page.locator('//form/div[2]/input')
    this.addButton = page.locator('//button[@type=\'submit\']')

    this.filterInput = page.getByRole('textbox').first();
  }

  async getHeader() {
    return this.appHeader
  }

  async addNewEntry(name, number) {
    await this.nameInput.fill(name)
    await this.numberInput.fill(number)
    await this.addButton.click()
  }

  async deleteLastEntry() {
    const deleteButton = await this.page.getByRole('button').last()
    await deleteButton.click()
  }

  async setFilter(str) {
    await this.filterInput.press("Control+KeyA")
    await this.filterInput.press("Delete")

    if (str) {
      await this.filterInput.fill(str);
    }
  }
}
