import collections
import copy 
from operator import itemgetter
import math
import bisect
from collections import deque

'''
This module contains several function for compress and decompress data, using
the Huffman code algorithm.
'''

MAGIC = b"i2cshcfv1"
BIN_BASE = 2
BYTE_LEN = 8

def symbol_count(data):
    '''
    converts data into a counter'''
    
    return collections.Counter(data)

def make_huffman_tree(counter):
    '''
    converts a counter into a huffman tree. A huffman tree
    is tuples inside of tuples, recursivly, representing
    the commonness of each note'''
    
    if not counter:
        return None
    list_of_elements = []
    for element in counter:
        list_of_elements.append((element, counter[element]))
    list_of_elements.sort(key = lambda tup: tup[0], reverse = True)
    list_of_elements.sort(key = lambda tup: tup[1])
    
    while len(list_of_elements) > 1:
        smallest_1 = list_of_elements.pop(0)
        smallest_2 = list_of_elements.pop(0)
        sum_of_elements = smallest_1[1]+smallest_2[1]
        branch = ((smallest_2[0],smallest_1[0]),sum_of_elements)
        counts = [num_of_time[1] for num_of_time in list_of_elements]
        list_of_elements.insert(bisect.bisect(counts, sum_of_elements),\
                                branch)
    return list_of_elements[0][0]

def build_codebook(huff_tree):
    '''
    converts a huffnman tree into a codebook, which maps
    each note into a tuple holding the length of the code
    representation and the code'''
    
    if not huff_tree:
        return {}
    codebook = {}
    code = ""
    def rec_code(codebook, huff_tree, code):
        if type(huff_tree) != tuple:
            length = len(code)
            if length == 0:
                length = 1
                code = '0'
            codebook[huff_tree] = (length, int(code,BIN_BASE))
        else:
            rec_code(codebook, huff_tree[1], code + "1")
            rec_code(codebook, huff_tree[0], code + "0")
    rec_code(codebook, huff_tree, code)
    return codebook
   
def add_by_len(num, len_a, len_b):
    ##  adds one in value and than adss zeroes to the binary
    ##  representation, by length
    return (num+1)<<(len_a - len_b)

def build_canonical_codebook(codebook):
    '''
    Return a canonical coding table, from a codebook. The
    canonical coding table holdes 256 notes, each note we
    use is in the index of its value. Notes' values are
    given by their commonness.
    The first character receives coding with the same
    length where it all zeros. Each character gets the code
    of the previous character in the series, plus one
    (adding value, not a bit concatenation). If the
    character encoding length is longer than the length of 
    previous character encoding, after adding one, there 
    concatenate zeros right to the desired length encoding'''
    
    if not codebook:
        return {}
    canonical_codebook = {}
    list_of_elements = []
    for element in codebook:
        list_of_elements.append((element, codebook[element]))
    list_of_elements.sort(key = lambda tup: tup[0])
    list_of_elements.sort(key = lambda tup: tup[1][0])
    
    temp_num = (list_of_elements[0][1][0],0)
    canonical_codebook[list_of_elements[0][0]] = (temp_num[0],temp_num[1])
    next_one = 0
    for element, info in list_of_elements[1:]:
        next_one = add_by_len(next_one, info[0], temp_num[0])
        canonical_codebook[element] = (info[0], next_one)
        temp_num = info
    return canonical_codebook
    
def build_decodebook(codebook):
    '''
    Maps the bit sequance into its corresponding value'''
    
    decodebook = {}
    for element in codebook:
        decodebook[codebook[element]] = element
    return decodebook

def compress(corpus, codebook):
    '''
    Returns iterator that goes through the bits of the
    encoding of the corpus ‪by the table codebook.
    Iterator's output is 0 or 1 as int values.'''
    
    for char in corpus:
        coding = codebook[char]
        code = "{0:b}".format(coding[1])
        code = '0'*(coding[0] - len(code)) + code
        for bit in code:
            yield int(bit)

def decompress(bits, decodebook):
    '''
    Returns iterator that goes through the restoration of
    the original character sequence from a the encoded 
    sequence of bits using the recovery ‪table decodebook.'''
    
    sign = ''
    for bit in bits:
        sign += str(bit)
        sign_val = (len(sign),int(sign,BIN_BASE))
        if sign_val in decodebook:
            yield decodebook[sign_val]
            sign = ''

def pad(bits):
    '''
    Return an iterator which goes through each 8 bits and
    returns the integer value of it. After reaching the
    last bits (under 8) it adds 1 and zero to reach 8 and
    returns its value.'''
    
    bit_loc = 0
    bin_byte = ''
    for bit in bits:
        bit_loc += 1
        bin_byte += str(bit)
        if not bit_loc%BYTE_LEN:
            yield int(bin_byte, BIN_BASE)
            bin_byte = ''
    bin_byte = bin_byte + '1'+ '0'*(BYTE_LEN -1 -bit_loc%BYTE_LEN)
    yield int(bin_byte, BIN_BASE)

def unpad(byteseq):
    '''
    Recieves "pad" output, and returns an iterator which returns its input.'''
    
    byteseq_rep = ''
    bin_rep = ''
    for byte in byteseq:
        byteseq_rep += bin_rep
        bin_rep = ''
        bin_rep = bin(byte)[2:]
        bin_rep = '0'*(BYTE_LEN -len(bin_rep)) + bin_rep
    if bin_rep:
        for bit_loc in range(BYTE_LEN):
            if bin_rep[-bit_loc-1] == '1':
               break
        byteseq_rep += bin_rep[:-bit_loc-1]
    for bit in byteseq_rep:
        yield int(bit)
        

def join(data, codebook):
    '''
    Returns an iterator which returns the bit sequance of
    a canonical codebook and than the data'''
    
    byte_sec_len = 256
    full = [0]*byte_sec_len
    for element in codebook:
        full[element] = codebook[element][0]
    full += data
    for value in full:
        yield value

def split(byteseq):
    '''
    Recieves "join" output, and returns an iterator which returns its input.'''
    byte_sec_len = 256
    codebook = {}
    data = []
    canon_number = 0
    for char in byteseq:
        canon_number += 1
        if canon_number <= byte_sec_len:
            if char != 0:
                codebook[canon_number-1] = [char, None]
        else:
            data.append(char)
    codebook = build_canonical_codebook(codebook)
    return (iter(data), codebook)
