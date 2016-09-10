ARITH_COMMANDS = ["add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"]

C_PUSH          = "C_PUSH"
C_POP           = "C_POP"
C_ARITHMETIC    = "C_ARITHMETIC"

COMMANDS = {
                "push"      : C_PUSH,
                "pop"       : C_POP
            }

COMMENT_PREFIX = "//"

"""
Parse the input vm file, line by line.
Used by the VMtranslator object, which does a double pass over the file.
Cleans comments and can get mnemonic from a command and the type of a command
"""
class Parser(object):

    def __init__(self, input_file_path):
        with open(input_file_path, 'r') as input_file:
            self.temp_lines = input_file.readlines()
        self.place = -1
        self._clear_lines()

    def has_more_commands(self):
        return self.place < len(self.lines) -1

    def _clear_lines(self):
        cleared_lines = []
        for line in self.temp_lines:
            cleared_line = self._clear_line(line)
            if cleared_line is not "" and cleared_line is not "\n":
                cleared_lines.append(cleared_line)
        self.lines = cleared_lines

    def _clear_line(self, line):
        return line.split(COMMENT_PREFIX)[0].replace("\n","")

    def _line_to_command(self, line):
        return line.split(" ")

    def advance(self):
        self.place += 1
        self.current_commands = self._line_to_command(self.lines[self.place])

    def command_type(self):
        if self.current_commands[0] in ARITH_COMMANDS:
            return C_ARITHMETIC
        if self.current_commands[0] == "push":
            return C_PUSH
        if self.current_commands[0] == "pop":
            return C_POP
        
    def arg1(self):
        if self.command_type() == C_ARITHMETIC:
            return self.current_commands[0]
        return self.current_commands[1]

    def arg2(self):
        return int(self.current_commands[2])
