ARITH_COMMANDS = ["add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"]
COMMANDS = ["push", "pop", "label", "goto", "if-goto", "function", "return", "call"]

STACK_BASE_ADDRESS = 256
REGISTERS_BASE_ADDRESS = 0
LCL = 1
ARG = 2
THIS = 3
THAT = 4
TEMP = 5
REG = 13
STATIC_BASE_ADDRESS = 16
STACK_BASE_ADDRESS = 256
HEAP_BASE_ADDRESS = 2048
MEM_IO_BASE_ADDRESS = 16384

class CodeWriter(object):

    def __init__(self, output_file_path):
        self._output_file = open(output_file_path, "w")
        #SP = 256
        self._w_mem_loc(str(STACK_BASE_ADDRESS))
        self._w_manipulate("D", "A", None)
        self._set_var("SP")
        self.labelCount = 1

    #setting a variable with name
    def _set_var(self, symbol):
        self._w_mem_loc(symbol)
        self._w_manipulate("M", "D", None)

    #writing simplest known commands
    def _w_mem_loc(self, address):
        self._output_file.write("@" + address + "\n")

    def _w_manipulate(self, dest, comp, jump):
        if dest is not None:
            self._output_file.write(dest + "=")
        self._output_file.write(comp)
        if jump is not None:
            self._output_file.write(";" + jump)
        self._output_file.write("\n")
        
    def _w_label(self, symbol):
        self._output_file.write("(" + symbol + ")\n")

    #setting _file_name (for private variable use)
    def _set_file_name(self, fileName):
        self._file_name = fileName

    #retrieving wht in SP to A
    def _get_sp(self):
        self._w_mem_loc("SP")
        self._w_manipulate("A", "M", None)

    #popping from stack to dest
    def _pop_to_dest(self, dest):
        self._w_mem_loc("SP")
        self._w_manipulate("M", "M-1", None)
        self._get_sp()
        self._w_manipulate(dest, "M", None)

    #pushing from dest to stack
    def _push_dest(self, dest):
        self._get_sp()
        self._w_manipulate("M", dest, None)
        self._w_mem_loc("SP")
        self._w_manipulate("M", "M+1", None)

    #comands using two argument
    def _binary_command(self, action):
        self._output_file.write("\n")
        self._pop_to_dest("D")
        self._pop_to_dest("A")
        self._w_manipulate("D", "A" + action + "D", None)
        self._push_dest("D")
        self._output_file.write("\n")

    #comands using one argument
    def _unary_command(self, comp):
        self._output_file.write("\n")
        self._pop_to_dest("A")
        self._w_manipulate("D", comp +"A", None)
        self._push_dest("D")
        self._output_file.write("\n")

    #in case of comp use jump
    def _jump(self, comp, jump):
        self.labelCount += 1
        self._w_mem_loc("LBL"+str(self.labelCount))
        self._w_manipulate(None, comp, jump)
        return 'LBL'+str(self.labelCount)

    #commands using jumps
    def _compare_command(self, action):
        self._output_file.write("\n")
        self._pop_to_dest("D")
        self._pop_to_dest("A")
        #checking if the signs are different
        self._w_manipulate("D", "A-D", None) # putting their diference in D
        self._push_dest("-1")
        true_jump_lable = self._jump("D", action)   #jump to true label if true
        #else does the following code-putting 0 in the stack
        self._w_mem_loc("SP")
        self._w_manipulate("M", "M-1", None)
        self._push_dest("0")
        self._w_label(true_jump_lable)    #true label where to skip to
        self._output_file.write("\n")

    #by the given command_type call the correct writer function
    def WriteArithmetic(self, command_type):
        self._output_file.write("\n")
        if command_type == "add":
            self._binary_command("+")
        elif command_type == "sub":
            self._binary_command("-")
        elif command_type == "neg":
            self._unary_command("-")
        elif command_type == "eq":
            self._compare_command("JEQ")
        elif command_type == "gt":
            self._compare_command("JGT")
        elif command_type == "lt":
            self._compare_command("JLT")
        elif command_type == "and":
            self._binary_command("&")
        elif command_type == "or":
            self._binary_command("|")
        elif command_type == "not":
            self._unary_command("!")
        self._output_file.write("\n")

    #a coomon between some push commands
    def pushRegs(self, segment, index):
        self._w_mem_loc(segment)
        self._w_manipulate("D", "M", None)
        self._w_mem_loc(str(index))
        self._w_manipulate("A", "A+D", None)
        self._w_manipulate("D", "M", None)

    #a coomon between some pop commands
    def popRegs(self, segment, index):
        self._w_mem_loc(segment)
        self._w_manipulate("D", "M", None)
        self._w_mem_loc(str(index))
        self._w_manipulate("D", "A+D", None)
        self._set_var("R"+str(REG))

    #by the given command, segment, index call the correct writer function
    def WritePushPop(self, command, segment, index):
        self._output_file.write("\n")
        if command == "push":
            if segment == "argument" :
                self.pushRegs(str(ARG), index)
            elif segment == "local":
                self.pushRegs(str(LCL), index)
            elif segment == "static":
                self._w_mem_loc(self._file_name+str(index))
                self._w_manipulate("D", "M", None)
            elif segment == "constant":
                self._w_mem_loc(str(index))
                self._w_manipulate("D", "A", None)
            elif segment == "this":
                self.pushRegs(str(THIS), index)
            elif segment == "that":
                self.pushRegs(str(THAT), index)
            elif segment == "pointer":
                self._w_mem_loc(str(THIS + index))
                self._w_manipulate("D", "M", None)
            elif segment == "temp":
                self._w_mem_loc(str(TEMP + index))
                self._w_manipulate("D", "M", None)
            self._push_dest("D")
        elif command == "pop":
            if segment == "argument":
                self.popRegs(str(ARG), str(index))
            elif segment == "local":
                self.popRegs(str(LCL), str(index))
            elif segment == "static":
                self._w_mem_loc(self._file_name+str(index))
                self._w_manipulate("D", "A", None)
                self._set_var("R"+str(REG))
            elif segment == "this":
                self.popRegs(str(THIS), str(index))
            elif segment == "that":
                self.popRegs(str(THAT), str(index))
            elif segment == "pointer":
                self._w_mem_loc(str(THIS + index))
                self._w_manipulate("D", "A", None)
                self._set_var("R"+str(REG))
            elif segment == "temp":
                self._w_mem_loc(str(TEMP + index))
                self._w_manipulate("D", "A", None)
                self._set_var("R"+str(REG))
            self._pop_to_dest("D")
            self._w_mem_loc("R"+str(REG))
            self._w_manipulate("A", "M", None)
            self._w_manipulate("M", "D", None)
        self._output_file.write("\n")

    #closing the file we written
    def close(self):
        self._output_file.close()
