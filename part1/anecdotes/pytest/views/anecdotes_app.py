from data import anecdotes
from pytest import fail

def verify_page_loaded(home):
    assert "Anecdote of the day" == home.get_app_header()
    
def verify_anecdote_in_list(home):
	assert home.get_current_anecdote() in anecdotes

def next_anecdote(home):
    tries = 3
    current_anecdote = home.get_current_anecdote()
    for _ in range(tries):
        home.select_new_anecdote()
        new_anecdote = home.get_current_anecdote()
        if current_anecdote != new_anecdote:
            return new_anecdote
        
    fail("The anecdote was not changed")
    
def vote_for_anecdote(home, num: int):
    for _ in range(num):
        home.vote_anecdote()
        
def get_most_voted_anecdote_data(home):
	keys = list(home.votes.keys())
	values = list(home.votes.values())
	max_value = max(values)
	max_value_index = values.index(max_value)
	return keys[max_value_index], max_value

def verify_most_voted_anecdote_correct(home):
	anecdote, max_votes = get_most_voted_anecdote_data(home)
	assert anecdote == home.get_most_voted_anecdote()
	assert max_votes == home.get_most_voted_count()
     
def verify_current_vote_is_correct(home, votes: int):
	assert votes == home.get_current_vote_count()
    
def verify_number_of_votes(home):
	most_voted = home.get_most_voted_count()
	for i in range(100):
		home.select_new_anecdote()
		current_vote = home.get_current_vote_count()
		if current_vote < most_voted:
			if current_vote > 0:
				current_anecdote = home.get_current_anecdote()
				assert current_anecdote in home.votes
				vote_from_dict = home.votes[current_anecdote]
				assert vote_from_dict == current_vote
				break
			else:
				assert current_vote == 0

		if i == 99:
			fail("The votes on a second most voted anecdote are not the same as was recorded in votes dict")