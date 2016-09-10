A_INSTRUCTION = "@"
ASSIGN_DELIMITER = "="
JUMP_DELIMITER = ";"
SHIFT_RIGHT_OPERATOR = ">>"
SHIFT_LEFT_OPERATOR = "<<"
LABEL_PREFIX = "("
COMMENT_PREFIX = "//"

"""
Parse the input asm file, line by line.
Used by the assmbler object, which does a double pass over the file.
Cleans comments and can get mnemonic from a command and the type of a command
"""
class Parser(object):
    def __init__(self, input_file_path):
        self.lines = open(input_file_path, "r").readlines()
        self.current_line = -1
        self.A_COMMAND = "A_COMMAND"
        self.C_COMMAND = "C_COMMAND"
        self.L_COMMAND = "L_COMMAND"

    def start_over(self):
        self.current_line = -1

    def _clean_line(self, line):
        return line.split(COMMENT_PREFIX)[0].strip().replace(" ","")

    def reset_symbol(self, address):
        self.lines[self.current_line] = A_INSTRUCTION + str(address)

    def _get_next_command_line(self):
        temp_current_line = self.current_line + 1
        while True:
            if temp_current_line == len(self.lines):
                return None
            line = self.lines[temp_current_line]
            if len(self._clean_line(line)) > 0:
                return temp_current_line
            temp_current_line += 1

    def has_more_commands(self):
        return self._get_next_command_line() is not None

    def advance(self):
        self.current_line = self._get_next_command_line()

    def get_symbol(self):
        if self.command_type() == self.A_COMMAND:
            return self.get_current_line()[1:]
        #assumed L command
        return self.get_current_line()[1:-1]

    def command_type(self):
        if self.get_current_line().startswith(A_INSTRUCTION):
            return self.A_COMMAND
        if self.get_current_line().startswith(LABEL_PREFIX):
            return self.L_COMMAND
        return self.C_COMMAND

    def get_dest(self):
        if ASSIGN_DELIMITER not in self.get_current_line():
            return ""
        return self.get_current_line().split(ASSIGN_DELIMITER)[0]

    def get_comp(self):
        if ASSIGN_DELIMITER not in self.get_current_line():
            return self.get_current_line().split(JUMP_DELIMITER)[0]
        return self.get_current_line().split(ASSIGN_DELIMITER)[1].split(JUMP_DELIMITER)[0]

    def get_jump(self):
        if JUMP_DELIMITER not in self.get_current_line():
            return ""
        return self.get_current_line().split(JUMP_DELIMITER)[1]

    def get_current_line(self):
        return self._clean_line(self.lines[self.current_line])