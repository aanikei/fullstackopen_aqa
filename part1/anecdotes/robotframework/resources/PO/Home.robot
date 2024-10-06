*** Settings ***
Library     SeleniumLibrary
Library     String
Library     Collections
Variables   ../data/data.py

*** Variables ***
${HOME_APP_HEADER} =                //div[@id="root"]/div/h2
${ALL_TEXT} =                       //div[@id="root"]
${MOST_VOTED_COUNT_TEXT} =          //div[@id="root"]/div/div[3]/div
${MOST_VOTED_ANECDOTE} =            //div[@id="root"]/div/div[3]

*** Keywords ***
App Header Is Correct
    Wait Until Element Contains     ${HOME_APP_HEADER}  Anecdote of the day

Get New Anecdote
    Click Button                //div[@id="root"]/div/div[2]/button[2]

Vote
    Click Button                //div[@id="root"]/div/div[2]/button[1]

Get Whole Text
    ${whole_text} =             Get Text    ${ALL_TEXT}
    RETURN    ${whole_text}

Get Current Anecdote
    ${whole_text} =             Get Whole Text
    ${2nd_line} =               Get Line    ${whole_text}    1
    RETURN  ${2nd_line}

Get Current Vote Count
    ${whole_text} =             Get Whole Text
    ${3rd_line} =               Get Line    ${whole_text}    2
    ${current_votes_count} =    Remove String   ${3rd_line}  has 
    ${current_votes_count} =    Remove String   ${current_votes_count}   votes
    ${current_votes_count} =    Convert To Integer  ${current_votes_count}
    RETURN  ${current_votes_count}

Get Most Voted Section Text
    ${text} =                   Get Text    ${MOST_VOTED_COUNT_TEXT}
    RETURN    ${text}

Get Most Voted Anecdote
    ${most_votes_count} =       Get Most Voted Section Text
    ${anecdote} =               Get Text    ${MOST_VOTED_ANECDOTE}
    ${anecdote} =               Remove String   ${anecdote}     Anecdote with most votes\n  \n${most_votes_count}
    RETURN  ${anecdote}

Get Most Voted Count
    ${most_votes_count} =        Get Most Voted Section Text
    ${most_votes_count} =        Remove String   ${most_votes_count}  has    votes
    ${most_votes_count} =        Convert To Integer  ${most_votes_count}
    RETURN  ${most_votes_count}