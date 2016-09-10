def is_solvable(start,board):
    """
    a function that recievs a board, and a location on the board
    board is of the form of a list of positive ints except one '0' ,
    location is of the form int.
    returns true if you can get from starting position to the '0' by the jumps
    given by the numbers on the board, false if you can't
    """
    solvable = a_path_is_possible(start,board)
    for corridor in range(len(board)):
        if board[corridor] < 0:
            board[corridor] = -board[corridor]
    return solvable

def a_path_is_possible(start,board):
    """
    a rucurrsive way to solve 'is_solvable', goes through the board
    and check any possible way
    """
    if start < 0 or start >= len(board):
        return False
    if board[start] < 0:
        return False
    if board[start] == 0:
        return True
    board[start] = -board[start]
    return a_path_is_possible(start + board[start],board) or\
           a_path_is_possible(start - board[start],board)
