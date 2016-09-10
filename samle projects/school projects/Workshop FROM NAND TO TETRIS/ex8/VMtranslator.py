#!/usr/bin/python
import Parser
from CodeWriter import CodeWriter
import sys, os

class VMtranslator(object):
    def __init__(self, input_path):
        self.input_path = input_path
        if not os.path.isdir(input_path):
            self.input_path = os.path.dirname(input_path)
            self.code_writer = CodeWriter(input_path.replace(".vm", ".asm"))
        else:
            ouptut_path = os.path.join(input_path, os.path.basename(input_path) + ".asm")
            self.code_writer = CodeWriter(ouptut_path)

    def translate(self, input_file_name):
        self.parser = Parser.Parser(os.path.join(self.input_path, input_file_name))
        self.code_writer._set_file_name(input_file_name.split(".vm")[0])
        while self.parser.has_more_commands():
            self.parser.advance()
            self._gen_command()

    def _gen_command(self):
        command_type = self.parser.command_type()
        command_handler, args_amount = {
            Parser.C_PUSH       : [self.code_writer.write_push, 2],
            Parser.C_POP        : [self.code_writer.write_pop, 2],
            Parser.C_ARITHMETIC : [self.code_writer.write_arithmetic, 1],
            Parser.C_IF         : [self.code_writer.write_if, 1],
            Parser.C_LABEL      : [self.code_writer.write_label, 1],
            Parser.C_RETURN     : [self.code_writer.write_return, 0],
            Parser.C_GOTO       : [self.code_writer.write_goto, 1],
            Parser.C_FUNCTION   : [self.code_writer.write_function, 2],
            Parser.C_CALL       : [self.code_writer.write_call, 2]
        }[command_type]
        self._handle_command(command_handler, args_amount)

    def _handle_command(self, command_handler, args_amount):
        if args_amount == 2:
            command_handler(self.parser.arg1(), self.parser.arg2())
            return
        if args_amount == 1:
            command_handler(self.parser.arg1())
            return
        command_handler()

    def close(self):
        self.code_writer.close()

def main():
    if not len(sys.argv) == 2:
        print("Usage: assembler.py <input.vm>")
        return
    in_path = sys.argv[1]

    #single file
    if not os.path.isdir(in_path):
        translator = VMtranslator(in_path)
        translator.translate(os.path.basename(in_path))
        translator.close()
        return
    if in_path.endswith(r"/"):
        in_path = in_path[:-1]
    translator = VMtranslator(in_path)
    for file_name in os.listdir(in_path):
        if file_name.endswith(".vm"):
            translator.translate(file_name)
    translator.close()

if "__main__" == __name__:
    main()
