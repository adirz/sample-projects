"""
Contains a main entry point.
Assemble given .asm file to .hack file
"""
import sys, os
import re
import code
from Parser import Parser
from SymbolTable import SymbolTable

"""
address of the block of variables (symbol)
variable will receive address in this block, one after the other.
"""
VARIABLES_BASE_ADDRESS = 0x0010
VARIABLE_SIZE_IN_WORDS = 1
INSTRUCTION_SIZE_IN_WORDS = 1
ROM_BASE_ADRESS = 0

class Assembler(object):

    def __init__(self, input_file_path):
        self.output_file = open(input_file_path.replace('.asm', '.hack'), 'w')

        """
        The ROM address is the address of the current instruction written in the
        .hack file. The first instruction is 0, second is 1, etc. Label is not a instruction.
        """
        self.current_rom_address = ROM_BASE_ADRESS

        self.parser = Parser(input_file_path)
        self.symbol_table = SymbolTable()

        """
        The RAM address of the next free memory that a new variable
        should be at.
        """
        self.next_free_var_address = VARIABLES_BASE_ADDRESS

    #Does the double pass assemble
    def assemble(self):
        self.first_pass()
        self.parser.start_over()
        self.second_pass()

    """
    Find Label commands adds all labels to the symbol table
    so jumps to labels yet-to-be-parsed could work.
    """
    def first_pass(self):
        self.current_rom_address = ROM_BASE_ADRESS
        while self.parser.has_more_commands():
            self.parser.advance()
            if self.parser.command_type() == self.parser.L_COMMAND:
                self.symbol_table.add_entry(self.parser.get_symbol(),
                                            self.current_rom_address)
            else:
                self.current_rom_address += INSTRUCTION_SIZE_IN_WORDS

    """
    Use the symbol table (if symbol is not a number) to get the
    address of the symbol. Create new variable if not in the table.
    """
    def _symbol_to_address(self, symbol):
        if symbol.isdigit():
            return int(symbol)
        if not self.symbol_table.contains(symbol):
            #new var
            self.symbol_table.add_entry(symbol, self.next_free_var_address)
            self.next_free_var_address += VARIABLE_SIZE_IN_WORDS
        return self.symbol_table.get_address(symbol)
    """
    Translate the A and C instructions to hack and write in the output file
    and skip labels
    """
    def second_pass(self):
        self.current_rom_address = ROM_BASE_ADRESS
        while self.parser.has_more_commands():
            self.parser.advance()
            #C instruction
            if self.parser.command_type() == self.parser.C_COMMAND:
                self._write_to_output(code.generate_c(self.parser.get_comp(),
                                                      self.parser.get_dest(),
                                                      self.parser.get_jump()))
            #A instruction
            elif self.parser.command_type() == self.parser.A_COMMAND:
                address = self._symbol_to_address(self.parser.get_symbol())
                self._write_to_output(code.generate_a(address))
            #Lable is not an instruction
            if not self.parser.command_type() == self.parser.L_COMMAND:
                self.current_rom_address += INSTRUCTION_SIZE_IN_WORDS
    """
    Write a single hack instruction in the output .hack file,
    add a newline after it
    """
    def _write_to_output(self, hack_instruction):
        self.output_file.write(hack_instruction + "\n")


def main():
    if not len(sys.argv) == 2:
        print "Usage: assembler.py <input.asm>"
        return
    in_path = sys.argv[1]
    if not os.path.isdir(in_path):
        Assembler(in_path).assemble()
        return
    for file_name in os.listdir(in_path):
        if file_name.endswith(".asm"):
            print os.path.join(in_path, file_name)
            Assembler(os.path.join(in_path, file_name)).assemble()

if "__main__" == __name__:
    main()