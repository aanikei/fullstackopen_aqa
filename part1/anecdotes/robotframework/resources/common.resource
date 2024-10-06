*** Settings ***
Library                 SeleniumLibrary

*** Keywords ***
Begin Test
    Open Browser    ${URL}    ${BROWSER}

End Test
    #Sleep 	2s
    Close All Browsers