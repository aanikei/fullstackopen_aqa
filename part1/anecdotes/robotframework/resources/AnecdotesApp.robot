*** Settings ***
Resource        ../resources/PO/Home.robot
Variables       ./data/data.py

*** Variables ***
${TRIES} =  3
&{DICT}  = Create Dictionary    ${EMPTY}=-1

*** Keywords ***
Verify that the page is loaded
    Home.App Header Is Correct

Verify that the displayed anecdote is correct
    ${anecdote} =         Home.Get Current Anecdote
    List Should Contain Value   ${ANECDOTES}    ${anecdote}

"Next Anecdote" functionality returns new anecdote
    ${old_anecdote} =   Home.Get Current Anecdote
    FOR     ${index}    IN RANGE    ${TRIES}
        Home.Get New Anecdote
        ${new_anecdote} =   Home.Get Current Anecdote
        IF  '$old_anecdote' != '$new_anecdote'
            RETURN    ${new_anecdote}
        END
    END
    
    Fail    The anecdote was not changed

Vote for anecdote
    [Arguments]             ${votes}
    Repeat Keyword          ${votes}    Home.Vote
    
    #save current anecdote and its vote to DICT for future comparison
    ${current_anecdote} =   Home.Get Current Anecdote
    ${current_votes} =      Home.Get Current Vote Count
    Set To Dictionary       ${DICT}     ${current_anecdote}=${current_votes}

Most Voted Count Is Correct
    [Arguments]         ${votes}
    ${most_votes} =     Home.Get Most Voted Count
    ${votes} =          Convert To Integer      ${votes}
    Should Be Equal     ${most_votes}     ${votes}

Current Vote Count Is Correct
    [Arguments]         ${votes}
    ${current_votes} =  Home.Get Current Vote Count
    ${votes} =          Convert To Integer      ${votes}
    Should Be Equal     ${current_votes}     ${votes}

Most Voted Anecdote Is Correct
    # ${value} = 	Get Dictionary Keys 	${DICT}
    # Log To Console  ${value}
    ${max_key} =    Set Variable    ABC
    ${max_value} =  Set Variable   -1
    
    FOR    ${key}    ${value}    IN    &{DICT}
        IF    ${value} > ${max_value}
            ${max_key} =  Set Variable   ${key}
            ${max_value} =  Set Variable  ${value}
        END
    END

    ${most_votes} =             Home.Get Most Voted Count
    Should Be Equal             ${most_votes}     ${max_value}

    ${most_voted_anecdote} =    Home.Get Most Voted Anecdote
    Should Be Equal             ${most_voted_anecdote}     ${max_key}

Verify Number Of Votes
    ${most_votes} =             Home.Get Most Voted Count
    FOR    ${index}    IN RANGE    100
        ${new_anecdote} =    "Next Anecdote" functionality returns new anecdote
        ${current_votes} =  Home.Get Current Vote Count
        IF    ${current_votes} < ${most_votes} 
            IF    ${current_votes} > 0
                ${current_anecdote} =    Home.Get Current Anecdote
                Dictionary Should Contain Key    ${DICT}    ${current_anecdote}
                ${vote_from_dict} =    Get From Dictionary 	${DICT} 	${current_anecdote}
                ${current_votes} =  Home.Get Current Vote Count
                Should Be Equal    ${vote_from_dict}    ${current_votes}
                BREAK
            ELSE
                Should Be Equal    ${current_votes}    ${0}
            END
        END

        IF    ${index} == ${99}
            Fail    The votes on a second most voted anecdote are not the same as was recorded in DICT
        END
    END