#############################################################
# FILE: battleship.py
# WRITER:
# EXERCISE : intro2cs ex4 2013-2014
# Description
#############################################################

"""Implement the following function according the description in ex4"""



def new_board(width = 10 ,height = None):
    """creates a new board game for a Battleship game.

    Args:
    -width: a positive int - the width of the board - default value 10
    -height: a positive int - the height of the board - if not spcified
    should be as width


    return: a NEW enpty board - each inner arrays is a list of 'None's.

    n case of bad input: values are out of range returns None

    You can assume that the types of the input arguments are correct."""
    if width <= 0:
        return None
    if height == None:
        height = width
    if height <= 0:
        return None
    board = []
    #adds the x axis lists to the board
    for i in range(height):
        x = [None]*width
        board.append(x)
    return board

def place_ship(board,ship_length,bow,ship_direction):
    """Put a new ship on the board

    put a new ship (with unique index) on the board.
    in case of successful placing edit the board according to the definitions
    in the ex description.

    Args:
    -board - battleshipe board - you can assume its legal
    -ship_length: a positive int the length of the ship
    -bow: a tuple of ints the index of the ship's bow
    -ship_direction: a tuple of ints representing the direction the ship
    is facing (dx,dy) - should be out of the 4 options(E,N,W,S):
    (1,0) -facing east, rest of ship is to west of bow,
    (0,-1) - facing north, rest of ship is to south of bow, and etc.

    return: the index of the placed ship, if the placement was successful,
    and 'None' otherwise.

    In case of bad input: values are out of range returns None

    
     You can assume the board is legal. You can assume the other inputs
     are of the right form. You need to check that they are legal."""
    #checks validity:
    if ship_length <= 0:
        return None
    if bow[0] < 0 or bow[0] >= len(board[0]) or \
       bow[1] < 0 or bow[1] >= len(board):
        return None
    posible_directions = ((1,0),(0,1),(-1,0),(0,-1))
    if not ship_direction in posible_directions:
        return None
    
    # checking the index:
    index = 0
    for height in board:
        for width in height:
            if not width == None:
                if index < width[0]:
                    index = width[0]
    
    index += 1
    size = [ship_length]
    # p_l = part location
    
    if ship_direction[0] == 0:
        for p_l in  range(ship_length):
            #check if it is occupide
            if bow[1]-p_l*ship_direction[1] >= 0 and \
               bow[1]-p_l*ship_direction[1] < len(board):
                if board[bow[1]-p_l*ship_direction[1]][bow[0]]:
                    return None
            else:
                return None
        for p_l in  range(ship_length):
            board[bow[1]-p_l*ship_direction[1]][bow[0]] = (index,p_l,size)
    else:
        for p_l in  range(ship_length):
            #check if it is occupide
            if bow[0]-p_l*ship_direction[0] >= 0 and \
               bow[0]-p_l*ship_direction[0] < len(board[0]):
                if board[bow[1]][bow[0]-p_l*ship_direction[0]]:
                    return None
            else:
                return None
        for p_l in  range(ship_length):
            board[bow[1]][bow[0]-p_l*ship_direction[0]] = (index,p_l,size)
    return index

def fire(board,target):
    
    """implement a fire in battleship game

    Calling this function will try to destroy a part in one of the ships on the
    board. In case of successful fire destroy the relevant part
    in the damaged ship by deleting it from the board. deal also with the case
    of a ship which was completely destroyed

    -board - battleshipe board - you can assume its legal
    -target: a tuple of ints (x,y) indices on the board
    in case of illegal target return None

    returns: a tuple (hit,ship), where hit is True/False depending if the the
    shot hit, and ship is the index of the ship which was completely
    destroyed, or 0 if no ship was completely destroyed. or 0 if no ship
    was completely destroyed.

    Return None in case of bad input

    You can assume the board is legal. You can assume the other inputs
    are of the right form. You need to check that they are legal."""
    
    ship = 0
    hit = False
    if not board or (not board[0]):
        return None
    if len(board) == 0:
        return None
    if len(board[0]) == 0:
        return None
    if target[1] < 0 or target[1] >= len(board) or \
       target[0] < 0 or target[0] >= len(board[0]):
        return None
    if board[target[1]][target[0]]:
        hit = True
        if board[target[1]][target[0]][2][0] > 1:
            board[target[1]][target[0]][2][0] -= 1
            ship = 0
            board[target[1]][target[0]] = None
        else:
            ship = board[target[1]][target[0]][0]
            board[target[1]][target[0]] = None
    return (hit,ship)
