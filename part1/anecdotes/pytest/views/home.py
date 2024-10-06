from selenium.webdriver.common.by import By
from views.base import BaseView

class HomeView(BaseView):
    app_header = (By.XPATH, '//div[@id="root"]/div/h2')
    all_text = (By.XPATH, f'//div[@id="root"]')
    voted_count_text = (By.XPATH, '//div[@id="root"]/div/div[3]/div')
    voted_anecdote = (By.XPATH, '//div[@id="root"]/div/div[3]')
    url = 'http://localhost:5173/'
    votes = {}

    def get_app_header(self) -> str:
        self.driver.get(self.url)
        return self.wait_for_presence(self.app_header).text

    def select_new_anecdote(self) -> None:
        self.find_multiple((By.TAG_NAME, 'button'))[1].click()

    def vote_anecdote(self) -> None:
        self.find_multiple((By.TAG_NAME, 'button'))[0].click()
        key = self.get_current_anecdote()
        if key not in self.votes:
            self.votes[key] = 1
        else:
            self.votes[key] += 1

    def get_whole_page_text(self) -> str:
        return self.find(self.all_text).text
    
    def get_current_anecdote(self) -> str:
        text = self.get_whole_page_text()
        return text.strip().split('\n')[1]
    
    def get_current_vote_count(self) -> int:
        text = self.get_whole_page_text()
        votes = text.strip().split('\n')[2]
        return int(votes.removeprefix('has').removesuffix('votes'))
    
    def get_most_voted_count_text(self) -> str:
        return self.find(self.voted_count_text).text
    
    def get_most_voted_anecdote(self) -> str:
        votes = self.get_most_voted_count_text()
        anecdote = self.find(self.voted_anecdote).text        
        return anecdote.removeprefix('Anecdote with most votes\n').removesuffix(f'\n{votes}')

    def get_most_voted_count(self) -> int:
        votes = self.get_most_voted_count_text()
        return int(votes.removeprefix('has').removesuffix('votes'))
