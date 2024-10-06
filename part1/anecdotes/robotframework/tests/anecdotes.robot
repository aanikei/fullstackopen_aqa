#robot -d .\results\ tests\anecdotes.robot

*** Settings ***
#Documentation   Testing Anecdotes app from Full Stack Open course / part1
Resource        ../resources/common.resource
Resource        ../resources/AnecdotesApp.robot

Test Setup      Begin Test
Test Teardown   End Test

*** Variables ***
${URL} =        http://localhost:5173/
${BROWSER} =    firefox
${VOTES} =      ${5}

*** Test Cases ***
Verify an anecdote is present
    AnecdotesApp.Verify that the page is loaded

    AnecdotesApp.Verify that the displayed anecdote is correct    

Verify that the "Next Anecdote" button works
    AnecdotesApp.Verify that the page is loaded

    AnecdotesApp."Next Anecdote" functionality returns new anecdote

Verify that the "Vote" functionality works
    AnecdotesApp.Verify that the page is loaded

    AnecdotesApp.Vote for anecdote              ${VOTES}
    AnecdotesApp.Current Vote Count Is Correct  ${VOTES}
    AnecdotesApp.Most Voted Anecdote Is Correct

    AnecdotesApp."Next Anecdote" functionality returns new anecdote
    AnecdotesApp.Vote for anecdote              ${VOTES - 1}
    AnecdotesApp.Current Vote Count Is Correct  ${VOTES - 1}
    AnecdotesApp.Most Voted Anecdote Is Correct

    AnecdotesApp.Vote for anecdote              2
    AnecdotesApp.Current Vote Count Is Correct  ${VOTES + 1}
    AnecdotesApp.Most Voted Anecdote Is Correct

    AnecdotesApp.Verify Number Of Votes