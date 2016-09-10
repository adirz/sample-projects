#!/usr/bin/env python3
def square_printing(n):
    size = 2*n +1
    temp_line = "" #temporery line - the string to be printed in that line
    x_count = 0 # point on the X axis
    y_count = 0 # point on the y axis
    while y_count < size:
        if y_count == 0 or y_count == size-1:
            while x_count < size:
                temp_line += "#"
                x_count += 1
        else:
            while x_count < size:
                if x_count == 0 or x_count == size - 1:
                   temp_line += "#"
                else:
                    if x_count + y_count == n+1 or x_count + y_count ==3*n-1 \
                       or x_count - y_count == n-1 or y_count - x_count == n-1:
                        temp_line += "*"
                    else:
                        temp_line += " "
                x_count +=1
        print(temp_line)
        temp_line = ""
        x_count = 0
        y_count +=1
        

if __name__=="__main__":  #If we are the main script, and not imported
    from sys import argv
    try:
        n = int(argv[1])
    except:
        n = int(input("Please enter a positive integer: "))
    square_printing(n)
