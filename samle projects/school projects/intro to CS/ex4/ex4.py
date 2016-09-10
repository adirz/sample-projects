#############################################################
# FILE: ex3.py
# WRITER: Adir Zagury
# EXERCISE : intro2cs ex4 2013-2014
# Description
#############################################################

def live_like_a_king(salary, save, pre_retire_growth_rates,
                  post_retire_growth_rates, epsilon):
   
    """ Find the maximal expenses you may expend during your lifetime  

    A function that calculates what is the maximal annual expenses you may
    expend each year and not enter into debts
    You may Calculate it using binary search or using arithmetics
    Specify in your README in which method you've implemnted the function

    Args:  
    -salary: the amount of money you make each year-a non negative float.
    -save: the percent of your salary to save in the investment account
    each working year -  a non negative float between 0 and 100
    -pre_retire_growth_rates: a list of annual growth percentages in your
    investment account - a list of floats larger than or equal to -100.
    -post_retire_growth_rates: a list of annual growth percentages
    on investments while you are retired. a list of floats larger
    than or equal to -100. In case of empty list return None
    - epsilon: an upper bound on the money must remain in the account
    on the last year of retirement. A float larger than 0

    Returns the maximal expenses value you found (such that the amount of
    money left in your account will be positive but smaller than epsilon)

    In case of bad input: values are out of range returns None

    You can assume that the types of the input arguments are correct."""
    
    # checking vars:
    for cheking_var in pre_retire_growth_rates:
        if cheking_var <-100:
            return None
    if len(post_retire_growth_rates) == 0:
        return None
    if len(pre_retire_growth_rates) == 0:
        return 0
    for cheking_var in post_retire_growth_rates:
        if cheking_var <-100:
            return None
    if (salary < 0 or save < 0 or save > 100 or epsilon < 0):
        return None
    if epsilon <= 0:
        return None
    
    a_r = variable_pension(salary, save, pre_retire_growth_rates)[-1]
    # a_r is the money you have at retirement

    # The money you have at retirement ( = a) is growing each year by
    # post_retire_growth_rates ( = g, in %) and so is your spending so the math
    # of calculating your spending ( = s) is:
    # a*(1+g[0])*(1+g[1])*(1+g[2])... - s*(1+g[1])*(1+g[2])*(1+g[3])...
    #- s*(1+g[2])*(1+g[3])*... - s = 0

    r_g_s = 1 #retirement growth sum
    spending_devider = 1
    num_of_growth = 1
    for growth in post_retire_growth_rates[1:]:
        num_of_growth+=1
        r_g_s = r_g_s*(1 + growth/100)
        mediate_growth = 1
        for growth_two in post_retire_growth_rates[num_of_growth-1:]:
            mediate_growth = mediate_growth*(1+growth_two/100)
        spending_devider += mediate_growth
    
    allowed_spending = a_r*r_g_s*(1+post_retire_growth_rates[0]/100)
    allowed_spending = allowed_spending/spending_devider
    return allowed_spending
   
#    allowed_spending =2*(a_r*(1+post_retire_growth_rates[0]/100)*r_g_s)/((1+r_g_s)*len(post_retire_growth_rates))

 
def bubble_sort_2nd_value(tuple_list):
    
    """sort a list of tuples using bubble sort algorithm

    Args:
    tuple_list - a list of tuples, where each tuple is composed of a string
    value and a float value - ('house_1',103.4)

    Return: a NEW list that is sorted by the 2nd value of the tuple,
    the numerical one. The sorting direction should be from the lowest to the
    largest. sort should be stable (if values are equal, use original order)

    You can assume that the input is correct."""
    
    n_t_l = list(tuple_list) #n_t_l = new tuple list

    # o_l_p = outer list point
    # i_l_p = inner list point
    for o_l_p in range(len(n_t_l)):
        for i_l_p in range(0, len(n_t_l)- o_l_p -1):
            if n_t_l[i_l_p][1] > n_t_l[i_l_p+1][1] :
                n_t_l[i_l_p],n_t_l[i_l_p+1] = n_t_l[i_l_p+1] , n_t_l[i_l_p]
    return n_t_l


def choosing_retirement_home(savings,growth_rates,retirement_houses):

    """Find the most expensive retirement house one can afford.

    Find the most expensive, but affordable, retiremnt house.
    Implemnt the function using binary search

    Args:
    -savings: the initial amount of money in your savings account.
    -growth_rates: a list of annual growth percentages in your
    investment account - a list of floats larger than or equal to -100.
    -retirement_houses: a list of tuples of retirement_houses, where
    the first value is a string - the name of the house and the
    second is the annual rent of it - nonnegative float.

    Return: a string - the name of the chosen retirement house
    Return None if can't afford any house.

    You need to test the legality of savings and growth_rates
    but you can assume legal retirement_house list 
    You can assume that the types of the input are correct"""
    #checks var:
    if not growth_rates:
        return None
    if not retirement_houses:
        return None
    if len(retirement_houses) == 0:
        return None
    if len(growth_rates) == 0:
        return None
    for check_vars in growth_rates:
        if check_vars < -100:
            return None
    if savings <= 0:
        return None
    
    # sorted_r_h is the sorted list of retirement_houses
    sorted_r_h = bubble_sort_2nd_value(retirement_houses)
    
    your_r_h = None # your_r_h is the name of the retirement home
    low_s = 0 # bottom of the search in the list
    high_s = len(sorted_r_h) # top of the search in the list
    
    # mid is the middle between low_s and high_s
    # mid_val is the value in "mid" point of the list
    while low_s < high_s:
        mid = (low_s+high_s)//2
        mid_val = sorted_r_h[mid]
        if post_retirement(savings, growth_rates, mid_val[1])[-1] >= 0:
            your_r_h = mid_val[0]
            low_s = mid + 1
        else:
            high_s = mid
    return your_r_h


def get_value_key(value):
    
    """returns a function that calculates the new value of a house

    #Args:
    -value: the value added per opponent - a float - the default value is 0

    This function returns a function that accepts triple containing
    (house ,anntual rent,number of opponents) and returns the new value of
    this house - annual_rent+value*opponents

    You can assume that the input is correct."""
    
    # the function that is asked to be returned, by the formula
    def funcky(vals):
        return vals[1]+ value*vals[2]
    return funcky


def choose_retirement_home_opponents(budget,key,retirement_houses):
    
    """ Find the best retiremnt house that is affordable and fun

    A function that returns the best retiremnt house to live in such that:
    the house is affordable and
    his value (annual_rent+value*opponents) is the highest

    Args:
    -budget: positive float. The amount of money you can
    expand per year.
    -key: a function of the type returned by get_value_key
    -retirement_houses: a list of houses (tuples), where  the first value
    is a string - the name of the house,
    the second is the annual rent on it - a non negative float, and the third
    is the number of battleship opponents the home hosts - non negative int
    
    Returns the name of the retirement home which provides the best value and
    which is affordable.

    You need to test the legality of annual_budget,
    but you can assume legal retirement_house list 
    You can assume that the types of the input are correct"""
    
    if budget <= 0:
        return None
    #s_r_h = sorted retirement houses, by value
    s_r_h = sorted(retirement_houses,key = key,reverse = True)
    for home in s_r_h:
        if home[1] <= budget:
            return home[0]
    return None

#############################################################
# From now on its a copy from my ex3
#############################################################

# A function that recieves salary, save, growth_rates and returns a list
# of how much you have at the end of each year
def variable_pension(salary, save, growth_rates):
    if salary < 0 or save < 0 or save >100:
        return None
    income = salary*save/100
    v_p = [] # a list of the capital at the end of each year in the fund
    funds = 0 # The current amount of funds in the pension plan
    # a loop, growing the funds in the pesion by yearly_growth, saving in v_p
    for yearly_growth in growth_rates:
        if yearly_growth < -100:
            return None
        funds = funds*(1+yearly_growth/100)+income
        v_p.append(funds)
    return v_p

#checks how much money you got left at the end of each year, after retiring
def post_retirement(savings, growth_rates, expenses):
    funds_left = []
    if savings <= 0 or expenses < 0:
        return None
    # g is each one of the objects in growth_rates
    for g in growth_rates:
        if g < -100:
            return None
        savings = savings*(1+g/100)-expenses
        funds_left.append(savings)
    return funds_left

    
