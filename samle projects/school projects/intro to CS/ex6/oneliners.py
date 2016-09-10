import random
import string
import re

def is_two_palindrome(pali):
    '''
    The function recieves a list of some sort and checks if there is a char that
    don't match its matching counter
    pali is the suspected two palindrome
    is_pali is a list containing "False" every time it doesn't match
    place is the ponit the function checks like so:
    0,1,2,2,1,0,<->,0,1,2,2,1,0
    '''
    is_pali = [False for place in range(len(pali)//4) \
               if(pali[place] != pali[len(pali)//2 - place -1] or \
                  pali[place + len(pali)//2 + len(pali)%2] != \
                  pali[len(pali) - place -1])]
    return not (False in is_pali)

def uni_sort(unsorted1,unsorted2):
    '''
    The function combines two unsorted lists of integers into ine sorted list,
    in ascending order, without multiple ints of the same value
    united_lists is the initial united lists
    place is the index of the number we currently check
    '''
    united_lists = sorted([item for item in unsorted1 + unsorted2])
    return [united_lists[0] for i in range(1) if len(united_lists) > 0] \
           +[united_lists[place] for place in range(1, len(united_lists))\
            if not united_lists[place] == united_lists[place -1]]

def dot_product(vector1, vector2):
    '''
    The function retuns the multification of each int in the matching indexes of
    two lists, until the shorter one ends
    axis is the index on witch we do the multifacation
    '''
    return sum(vector1[axis]*vector2[axis] for axis in\
            range(min(len(vector1),len(vector2))))

def list_intersection(unsorted1,unsorted2):
    '''
    The function gets as input two lists of integers and returns a new list sorted
    in ascending order containing only those integers who are in both lists
    item is the ints
    '''
    return sorted(list(set([item for item in unsorted1 if item in unsorted2])))

def list_difference(unsorted1,unsorted2):
    '''
    The function sort two lists into one, in an ascending order, containg only the
    integers who can only be found in one of the lists
    '''
    return sorted(list(set([item for item in unsorted1 \
                            if not item in unsorted2] + \
                           [item for item in unsorted2 \
                            if not item in unsorted1])))

def random_string(length):
    '''
    The function return a random string of the length "length"
    '''
    return "".join([chr(random.randint(97,122)) for char in range(length)])

def word_mapper(words):
    '''
    The function returns a dictionary containg one of every word in the string
    recieved (words)
    The words are the same in capital and lowercase
    Each word is derived by removing all none exceptable chars
    words is the recieved string
    word is each word in that string
    '''
    words = [word for word in sorted([word.lower() for word \
                        in re.findall(r"[\w]+", ''.join(words.split('_')))])]
    if words == []: return {}
    return dict(zip([words[word] for word in range(len(words)-1)\
                     if not words[word] == words[word+1]] + [words[-1]],\
                    [words.count(words[word]) for word in range(len(words)-1)\
                     if not words[word] == words[word+1]] + \
                    [words.count(words[-1])]))

def gimme_a_value(f,x0):
    '''
    This is a generator whitch, every time its called
    returns the function that was recieved activated on the last result it returned
    The first result is the x0 recieved
    f  - the activated function
    x0 - the initial result
    '''
    result = x0
    while True:
        yield result
        result = f(x0)
        x0 = result
    
