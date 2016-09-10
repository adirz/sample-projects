"""
The code module is used for translating a command to a hack instruction
uses hardcoded mapping between mnemonics to their hack version.
"""
ADDRESS_SIZE_IN_BITS = 15

"""
Translate the "dest" mnemonic of C instruction from it's ASM version
to its hack version. Used when translating a C instruction in the second pass.
"""
def _get_dest(dest_mnemonic):
    d1 = "1" if "A" in dest_mnemonic else "0"
    d2 = "1" if "D" in dest_mnemonic else "0"
    d3 = "1" if "M" in dest_mnemonic else "0"
    return d1 + d2 + d3

"""
Translate the "comp" mnemonic of C instruction from it's ASM version
to its hack version. Used when translating a C instruction in the second pass.
"""
def _get_comp(comp_mnemonic):
    return {
        "1"   : "110111111",
        "-1"  : "110111010",
        "D"   : "110001100",
        "A"   : "110110000",
        "!D"  : "110001101",
        "!A"  : "110110001",
        "-D"  : "110001111",
        "-A"  : "110110011",
        "D+1" : "110011111",
        "1+D" : "110011111",
        "A+1" : "110110111",
        "1+A" : "110110111",
        "D-1" : "110001110",
        "A-1" : "110110010",
        "D+A" : "110000010",
        "A+D" : "110000010",
        "D-A" : "110010011",
        "A-D" : "110000111",
        "D&A" : "110000000",
        "D|A" : "110010101",
        "M"   : "111110000",
        "!M"  : "111110001",
        "-M"  : "111110011",
        "M+1" : "111110111",
        "M-1" : "111110010",
        "D+M" : "111000010",
        "M+D" : "111000010",
        "D-M" : "111010011",
        "M-D" : "111000111",
        "D&M" : "111000000",
        "M&D" : "111000000",
        "D|M" : "111010101",
        "A<<" : "010100000",
        "D<<" : "010110000",
        "M<<" : "011100000",
        "A>>" : "010000000",
        "D>>" : "010010000",
        "M>>" : "011000000"
    }[comp_mnemonic]

"""
Translate the "jump" mnemonic of C instruction from it's ASM version
to its hack version. Used when translating a C instruction in the second pass.
"""
def _get_jump(jump_mnemonic):
    return {
        ""    : "000",
        "JGT" : "001",
        "JEQ" : "010",
        "JGE" : "011",
        "JLT" : "100",
        "JNE" : "101",
        "JLE" : "110",
        "JMP" : "111"
    }[jump_mnemonic]


"""
Generates an C instruction of given mnemonics of the command.
"""
def generate_c(comp_mnemonic, dest_mnemonic, jump_mnemonic):
    return "1" + _get_comp(comp_mnemonic) + _get_dest(dest_mnemonic) + _get_jump(jump_mnemonic)

"""
Generates an A instruction of given address.
Assumes address is less than 2^15
"""
def generate_a(address):
    return "0" + "{0:b}".format(address).zfill(ADDRESS_SIZE_IN_BITS)