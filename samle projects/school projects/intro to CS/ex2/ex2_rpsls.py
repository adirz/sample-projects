#!/usr/bin/env python3
import random

# return 1 if player has won the round, 0 if the computer has
# "player" represents the choice, and "comp"represnts the computer's
def who_won(player , comp): 
    if player == 1:
        if comp == 2:
            return 0
        if comp == 3:
            return 1
        if comp == 4:
            return 1
        if comp == 5:
            return 0
    if player == 2:
        if comp == 1:
            return 1
        if comp == 3:
            return 0
        if comp == 4:
            return 0
        if comp == 5:
            return 1
    if player == 3:
        if comp == 1:
            return 0
        if comp == 2:
            return 1
        if comp == 4:
            return 1
        if comp == 5:
            return 0
    if player == 4:
        if comp == 1:
            return 0
        if comp == 2:
            return 1
        if comp == 3:
            return 0
        if comp == 5:
            return 1
    else:
        if comp == 1:
            return 1
        if comp == 2:
            return 0
        if comp == 3:
            return 1
        else:
            return 0

# the function to play a single game of rock-paper-scissors-lizard-spok
# returns 1 if player has won, and -1 if the computer has won.
# game is until an advantage of two
def rpsls_game():
    choices = ("Rock","Paper","Scissors","Lizard","Spock")
    participents =("Computer","Player")
    p_score = 0 # player's score in current set
    c_score = 0 # computer's score in current set
    draws = 0

    while abs(p_score - c_score) < 2:
        comp_choice = random.randint(1,5)
        temp_input =  int(input("    Please enter your selection: 1 (Rock)," +
                                " 2 (Paper), 3 (Scissors), 4 (Lizard) or 5 " +
                                "(Spock): "))
        while not temp_input in range(1,6):
            print("    Please select one of the available options.\n")
            temp_input =  int(input("    Please enter your selection:" +
                                    " 1 (Rock), 2 (Paper), 3 (Scissors)," +
                                    " 4 (Lizard) or 5 (Spock): "))
        players_choice = temp_input
        print("    Player has selected: %s." %choices[players_choice -1])
        print("    Computer has selected: %s." %choices[comp_choice -1])
        if comp_choice == players_choice :
            print("    This round was drawn\n")
            draws += 1
        else:
            temp_win = who_won(players_choice , comp_choice)
            print("    The winner for this round is:" +
                  " %s\n" %participents[temp_win])
            p_score += temp_win
            c_score += (1-temp_win)
    if p_score - c_score == 2:
        print("The winner for this game is: %s" %participents[1])
        print("Game score: Player " +
              "%d, Computer %d, draws %d"%(p_score,c_score,draws))
        return 1
    else:
        print("The winner for this game is: %s" %participents[0])
        print("Game score: Player " +
              "%d, Computer %d, draws %d"%(p_score,c_score,draws))
        return -1

# function to play sets of games acording to the player wishes
def rpsls_play():
    print("Welcome to the Rock-Scissors-Paper-Lizard-Spock game!")
    num_of_sets = 1 # how many sets are being played
    action = 3      # what to do: ation == 1 quits ; action === 2 restart; ation == 3 continue
    sets_wins = 0   # how many sets the player has won
    set_length = int(input("Select set length: "))
    while action == 3:
        till_dif = 0 #would the set continue untill a diffrence of 2
        game_num = 0 # The currently played game
        p_wins = 0 # The number of wins of the player
        c_wins = 0 # The number of wins of the computer
        while (game_num < set_length and \
               (p_wins <= set_length/2 and c_wins <= set_length/2)) or \
              (abs(p_wins - c_wins) < 2 and till_dif):
            game_num += 1
            print("Now beginning game %d" %game_num)
            winner = rpsls_game()
            if winner == 1:
                p_wins += 1
            else:
                c_wins += 1
            print("Set score: Player %d, Computer %d" %(p_wins , c_wins))
            if p_wins == c_wins and game_num == set_length:
                till_dif = 1
            
        if p_wins > c_wins:
            sets_wins += 1
            print("Congratulations! You have won in %d games." %game_num)
        else:
            print("Too bad! You have lost in %d games." %game_num)
        print("You have played " +
              "%d sets, and won %d!\n" %(num_of_sets, sets_wins))
        action = int(input("Do you want to: 1 - quit, 2 - reset scores or " +
                           "3 - continue? "))
        if action == 3:
            num_of_sets +=1
        elif action == 2:
            print("Resetting scores")
            num_of_sets = 1
            action = 3
            sets_wins = 0
            set_length = int(input("Select set length: "))
            action == 3
        else:
            quit
