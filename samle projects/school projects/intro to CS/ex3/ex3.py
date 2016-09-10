#############################################################
# FILE: ex3.py
# WRITER: Adir Zagury
# EXERCISE : intro2cs ex3 2013-2014
# Description:
#     
#############################################################

# A function that recieves salary, save, growth_rate, years and returns a list
# of how much you have at the end of each year
def constant_pension(salary, save, growth_rate, years):
    if salary < 0 or save < 0 or save >100 or growth_rate < -100 or years < 0:
        return None
    income = salary*save/100 # the anual income to the fund
    c_p = [] # a list of the capital at the end of each year in the fund
    funds = 0 # The current amount of funds in the pension plan
    # a loop, growing the funds in the pesion by growth_rate and saving in c_p
    for i in range(years):
        funds = funds*(1+growth_rate/100)+income
        c_p.append(funds)
    return c_p

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

# a function that recieves a string of the line on the pension plan and returns
# a list, where the first object is the fund's name and the others are the
# growth rates
def get_funds_details(info_line):
    details=[]
    name = '' # the name of the fund
    temp_num = '' # strings' char combaining the growth rates
    first_word = True # is it the first word (the name)
    
    # skips the first char because it is '#' which is not a part of the name
    # goes threw the line seperating to the objects information parts
    for c in info_line[1:]:
        if not c == ',':
            if first_word:
                name += c
            else:
                temp_num +=c
        else:
            if first_word:
                first_word = False
                details.append(name)
            else:
                details.append(float(temp_num))
                temp_num =''
    details.append(float(temp_num))
    return details

# The function recievs your salary and how much you saves, and a file with
# a list of funds and growth rates, and returns which is the best fund
def choose_best_fund(salary,save,funds_file):
    if salary < 0 or save < 0 or save >100:
        return None
    
    funds_list = open(funds_file , 'r').readlines()
    # best_fund is a list made out of the fund's details
    # best total is the total amount of the best fund
    best_fund = get_funds_details(funds_list[0])
    best_total = variable_pension(salary, save, best_fund[1:])[-1]
    
    for current_fund in funds_list[1:]:
        temp_fund = get_funds_details(current_fund)
        current_total = variable_pension(salary, save, temp_fund[1:])[-1]
        if best_total < current_total:
            best_total = current_total
            best_fund = temp_fund
    answer = (best_fund[0] , best_total)
    return answer

# a function that recives a list of groeth rates and a year, and returning the
# growth rate in that year
def growth_in_year(growth_rates,year):
    if len(growth_rates) <= year or year < 0:
        return None
    return growth_rates[year]

#a function that implements the inflation to adjust the growth rates, to
# its real value
def inflation_growth_rates(growth_rates,inflation_factors):
    adjusted_g = [] # the adjusted growth rate
    place = 0 # the place in the inflation list

    # g is each one of the objects in growth_rates
    # i is each one of the objects in inflation_factors
    for g in growth_rates:
        if g < -100:
            return None
        if place < len(inflation_factors):
            i = inflation_factors[place]
            if i <= -100:
                return None
            place += 1
            adjusted_g.append(100*((100+g)/(100+i)-1))
        else:
            adjusted_g.append(g)
    while(place < len(inflation_factors)):
        i = inflation_factors[place]
        place += 1
        if i <= -100:
            return None
    return adjusted_g

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
