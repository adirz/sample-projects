def mystery_computation(number):
    """ The function sums all of the numbers between 0 and 'number' that divide
        without any remainder the 'number
    """
    mys_sum = 0
    for devider in range(1,number):
        if number%devider == 0:
            mys_sum += devider
    return mys_sum
