print("Welcome to the Einstein puzzle")
num1_str = input("Please enter a three digit number:")
num1_int = int(num1_str)

##########################################################################
# while(num1_int/999>=1 or num1_int//100 == num1_int%10):
#    if num1_int/999>=1:
#        print ("It requairs three digit number")
#    if num1_int//100 == num1_int%10:
#        print("The first and last digits must differ by at least one")
#    num1_str = input("please enter a three digit number:")
#    num1_int = int(num1_str)
##########################################################################

num1_rev = num1_int//100 +((num1_int//10)%10)*10 + (num1_int%10)*100
print("For the number:",num1_int, "the reverse number is:",num1_rev)
num2_int = abs(num1_int-num1_rev)
print("The difference between",num1_int, "and",num1_rev, "is", num2_int)
num2_rev = num2_int//100 +((num2_int//10)%10)*10 + (num2_int%10)*100
print("The reverse difference is:",num2_rev)
magic_sum= num2_int + num2_rev
print("The sum of:",num2_int,"and",num2_rev,"is:",magic_sum)
