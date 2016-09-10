"""
keep a table (dictionary) of known symbols.
the table is mapping name to address of the stored value
the addresses of the symbols are decided in the first pass
and used in the second pass

"""
PREDEFINED_SYMBOLS = {
    "SP"     : 0x0000,
    "LCL"    : 0x0001,
    "ARG"    : 0x0002,
    "THIS"   : 0x0003,
    "THAT"   : 0x0004,
    "R0"     : 0x0000,
    "R1"     : 0x0001,
    "R2"     : 0x0002,
    "R3"     : 0x0003,
    "R4"     : 0x0004,
    "R5"     : 0x0005,
    "R6"     : 0x0006,
    "R7"     : 0x0007,
    "R8"     : 0x0008,
    "R9"     : 0x0009,
    "R10"    : 0x000A,
    "R11"    : 0x000B,
    "R12"    : 0x000C,
    "R13"    : 0x000D,
    "R14"    : 0x000E,
    "R15"    : 0x000F,
    "SCREEN" : 0x4000,
    "KBD"    : 0x6000
}

class SymbolTable(object):
    #_table is the dictionary that stores the table
    def __init__(self):
        self._table = dict(PREDEFINED_SYMBOLS)

    def add_entry(self, symbol, address):
        self._table[symbol] = address

    def contains(self, symbol):
        return symbol in self._table

    def get_address(self, symbol):
        return self._table[symbol]
