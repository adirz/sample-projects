def get_alignment_score(dna1, dna2, match = 1, mismatch = -1, gap = -2):
    """
    The function recievs 2 DNA strands and returns their alignment score
    by the formula given
    """
    score = 0
    for base in range(len(dna1)):
        if dna1[base] == dna2[base]:
            score += match
        elif dna1[base] == '-' or dna2[base] == '-' :
            score += gap
        else:
            score += mismatch
        if dna1[base] == '-' and dna2[base] == '-':
            return None
    return score

# checks to see If we got the same arguments in the past, so we could use them
def remember_results(past_best_alignment):
    past_results = {}
    def result_of(*arguments):
        if arguments not in past_results:
            past_results[arguments] = past_best_alignment(*arguments)
        return past_results[arguments]
    return result_of

def get_best_alignment_score(dna1, dna2, match = 1, mismatch = -1, gap = -2):
    """
    finds the best alignment of the two given DNA strands
    """
    if dna1 == '':
        return (gap*len(dna2), '-'*len(dna2) , dna2)
    if dna2 == '':
        return (gap*len(dna1), dna1 , '-'*len(dna1))
    
    best_case = list(get_best_alignment_score(dna1[1:], dna2[1:], match, \
                                              mismatch, gap))
    best_case[0] = get_alignment_score(dna1[0],dna2[0], match, mismatch,\
                                         gap) + best_case[0]
    best_case[1] = dna1[0] + best_case[1]
    best_case[2] = dna2[0] + best_case[2]
    
    best_case_attempt = list(get_best_alignment_score(dna1, dna2[1:], match, \
                                                      mismatch, gap))
    
    best_case_attempt[0] = get_alignment_score('-',dna2[0], match, mismatch,\
                                         gap) + best_case_attempt[0]
    best_case_attempt[1] = '-' + best_case_attempt[1]
    best_case_attempt[2] = dna2[0] + best_case_attempt[2]

    if best_case[0] < best_case_attempt[0]:
        best_case = best_case_attempt

    best_case_attempt = list(get_best_alignment_score(dna1[1:], dna2, match, \
                                                      mismatch, gap))
    best_case_attempt[0] = get_alignment_score(dna1[0],'-', match, mismatch,\
                                         gap) + best_case_attempt[0]
    best_case_attempt[1] = dna1[0] + best_case_attempt[1]
    best_case_attempt[2] = '-' + best_case_attempt[2]
   
    if best_case[0] < best_case_attempt[0]:
        best_case = best_case_attempt
        
    return tuple(best_case)
    
get_best_alignment_score = remember_results(get_best_alignment_score)
