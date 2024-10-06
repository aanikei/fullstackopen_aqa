#pytest -s
import views.anecdotes_app as app

def test_anecdote_is_correct(home):
    app.verify_page_loaded(home)
    app.verify_anecdote_in_list(home)

def test_next_anecdote_button(home):
    app.verify_page_loaded(home)
    app.next_anecdote(home)
    
def test_vote_functionality(home):
    app.verify_page_loaded(home)

    votes = 5
    app.vote_for_anecdote(home, votes)
    app.verify_current_vote_is_correct(home, votes)
    app.verify_most_voted_anecdote_correct(home)

    app.next_anecdote(home)
    new_votes = votes - 1
    app.vote_for_anecdote(home, new_votes)
    app.verify_current_vote_is_correct(home, new_votes)
    app.verify_most_voted_anecdote_correct(home)

    app.vote_for_anecdote(home, 2)
    app.verify_current_vote_is_correct(home, new_votes + 2)
    app.verify_most_voted_anecdote_correct(home)

    app.verify_number_of_votes(home)