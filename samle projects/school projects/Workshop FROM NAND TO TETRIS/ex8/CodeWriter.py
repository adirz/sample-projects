import os
ARITH_COMMANDS = ["add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"]
COMMANDS = ["push", "pop", "label", "goto", "if-goto", "function", "return", "call"]

STACK_BASE_ADDRESS = 256
REGISTERS_BASE_ADDRESS = 0
LCL = 1
ARG = 2
THIS = 3
THAT = 4
TEMP = 5
REG = "R13"
STATIC_BASE_ADDRESS = 16
STACK_BASE_ADDRESS = 256
HEAP_BASE_ADDRESS = 2048
MEM_IO_BASE_ADDRESS = 16384

class CodeWriter(object):

    def __init__(self, output_file_path):
        self._output_file = open(output_file_path, "w")
        self._file_name = os.path.basename(os.path.splitext(output_file_path)[0])
        self._label_count = 0
        self._curr_func = "Sys.init"
        self._init_instructions()

    #setting _file_name (for private variable use)
    def _set_file_name(self, fileName):
        self._file_name = fileName

    def _remark(self, msg):
        self._output_file.write("//" + msg +"\n")
        
    def _init_instructions(self):
        #init stack (SP) variable
        self._a_instruction(STACK_BASE_ADDRESS)
        self._c_instruction("D", "A")
        self._set_var("SP")
        self.write_call(self._curr_func, 0)

    #setting variable with given name (symbol) with D
    #symbol = D
    def _set_var(self, symbol):
        self._a_instruction(symbol)
        self._c_instruction("M", "D")

    #write A instruction to this address
    def _a_instruction(self, address):
        self._output_file.write("@" + str(address) + "\n")

    #write C instruction with given mnomics
    def _c_instruction(self, dest, comp, jump = None):
        if dest is not None:
            self._output_file.write(dest + "=")
        self._output_file.write(comp)
        if jump is not None:
            self._output_file.write(";" + jump)
        self._output_file.write("\n")

    #write label instruction
    def write_label(self, symbol, outer = True):
        if outer:
            self._output_file.write("(" + self._curr_func+"."+symbol + ")\n")
        else:
            self._output_file.write("(" + symbol + ")\n")

    #A = SP
    def _get_sp(self):
        self._a_instruction("SP")
        self._c_instruction("A", "M")

    #popping from stack to dest (dest = *SP)
    def _pop_to_dest(self, dest, extra = ""):
        self._a_instruction("SP")
        self._c_instruction(extra + "M", "M-1")
        self._c_instruction(dest, "M")

    #pushing from dest to stack
    def _push_dest(self, dest):
    #    self._a_instruction("SP")
        self._get_sp()
        self._c_instruction("M", dest)
        self._a_instruction("SP")
        self._c_instruction("M", "M+1")

    #commands using two argument
    def _binary_command(self, action):
        self._output_file.write("\n")
        self._pop_to_dest("D", "A")
        self._pop_to_dest("M", "A")
    #    self._push_dest("M" + action + "D")
        self._c_instruction("M", "M" + action + "D")
        self._a_instruction("SP")
        self._c_instruction("M", "M+1")
        self._output_file.write("\n")

    #command using one argument
    def _unary_command(self, comp):
        self._output_file.write("\n")
        self._pop_to_dest("A")
        self._c_instruction("D", comp + "A")
        self._push_dest("D")
        self._output_file.write("\n")

    #in case of comp use jump to go to next LBL
    def _jump(self, comp, jump):
        self._label_count += 1
    #    if(reverse):
    #        if(jump == JGT): jump = JLT
    #        if(jump == JLT): jump = JGT
        self._a_instruction("LBL" + str(self._label_count))
        self._c_instruction(None, comp, jump)
        return "LBL" + str(self._label_count)

    def set_sp(self, to_bool):
        self._get_sp()
        self._c_instruction("M",to_bool)
            
    #commands using jumps
    def _compare_command(self, jump_action):
        self._output_file.write("\n")
        self._pop_to_dest("D", "A")
        first_sign_neg = self._jump("D", "JLT")
        self._pop_to_dest("D", "A")
        eq_signs = self._jump("D", "JGT")
        self.set_sp("-1")
        finish = self._jump("0", "JMP")
        self.write_label(first_sign_neg, False)
        self._pop_to_dest("D", "A")
        self._a_instruction(eq_signs)
        self._c_instruction(None, "D", jump_action)
        self.set_sp("0")
        self._a_instruction(finish)
        self._c_instruction(None, "0", "JMP")
        self.write_label(eq_signs, False)
        self._a_instruction("SP")
        self._c_instruction("M", "M+1")
        self._c_instruction("A","M")
        self._c_instruction("D", "M-D")
        self._a_instruction("SP")
        self._c_instruction("AM","M-1")
        self._c_instruction("M","0")
        self._a_instruction(finish)
        self._c_instruction(None, "D", "JLE")
        self.set_sp("-1")
        self.write_label(finish, False)
        self._a_instruction("SP")
        self._c_instruction("M", "M+1")
        self._output_file.write("\n")

    #by the given command_type call the correct writer function
    def write_arithmetic(self, command_type):
        self._output_file.write("\n")
        self._remark("start arithmetic "+command_type)
        func, arg = {
            "add"   : [self._binary_command,    "+"],
            "sub"   : [self._binary_command,    "-"],
            "neg"   : [self._unary_command,     "-"],
            "eq"    : [self._compare_command,   "JEQ"],
            "gt"    : [self._compare_command,   "JGT"],
            "lt"    : [self._compare_command,   "JLT"],
            "and"   : [self._binary_command,    "&"],
            "or"    : [self._binary_command,    "|"],
            "not"   : [self._unary_command,     "!"]
        }[command_type]
        func(arg)
        self._remark("end arithmetic "+command_type)
        self._output_file.write("\n")
    #D= segment + index
    def _push_regs(self, segment, index):
        self._a_instruction(segment)
        self._c_instruction("D", "M")
        self._a_instruction(index)
        self._c_instruction("A", "A+D")
        self._c_instruction("D", "M")

    #REG = segment + index
    def _pop_regs(self, segment, index):
        self._a_instruction(segment)
        self._c_instruction("D", "M")
        self._a_instruction(index)
        self._c_instruction("D", "A+D")
        self._set_var(REG)

    #Jump if *SP is not 0
    def write_if(self, label):
        self._remark("start if")
        self._pop_to_dest("D", "A")
        self._a_instruction(self._curr_func+"."+label)
        self._c_instruction(None, "D", "JNE")
        self._remark("end if")

    #write a pop command
    def write_pop(self, segment, index):
        self._output_file.write("\n")
        self._remark("start pop "+segment+" "+str(index))
        if segment == "argument":
            self._pop_regs(ARG, index)
        elif segment == "local":
            self._pop_regs(LCL, index)
            self._pop_to_dest("D", "A")
        elif segment == "static":
            self._pop_to_dest("D", "A")
            self._set_var(self._file_name + "."+str(index))
        elif segment == "this":
            self._pop_regs(THIS, index)
        elif segment == "that":
            self._pop_regs(THAT, index)
        elif segment == "pointer":
            self._a_instruction(THIS + index)
            self._c_instruction("D", "A")
            self._set_var(REG)
        elif segment == "temp":
            self._a_instruction(TEMP)
            self._c_instruction("D", "A")
            self._a_instruction(index)
            self._c_instruction("D", "D+A")
            self._set_var(REG)
            self._pop_to_dest("D", "A")
            self._a_instruction(REG)
            self._c_instruction("A", "M")
            self._c_instruction("M", "D")
        self._remark("end pop "+segment+" "+str(index))
        self._output_file.write("\n")

    def write_push(self, segment, index):
        self._output_file.write("\n")
        self._remark("start push "+segment+" "+str(index))
        if segment == "argument" :
            self._push_regs(ARG, index)
        elif segment == "local":
            self._push_regs(LCL, index)
        elif segment == "static":
            self._a_instruction(self._file_name +"."+ str(index))
            self._c_instruction("D", "M")
        elif segment == "constant":
            self._a_instruction(index)
            self._c_instruction("D", "A")
        elif segment == "this":
            self._push_regs(THIS, index)
        elif segment == "that":
            self._push_regs(THAT, index)
        elif segment == "pointer":
            self._a_instruction(THIS + index)
            self._c_instruction("D", "M")
        elif segment == "temp":
            self._a_instruction(TEMP + index)
            self._c_instruction("D", "M")
        self._push_dest("D")
        self._remark("end push "+segment+" "+str(index))
        self._output_file.write("\n")

    def write_goto(self, label):
        self._a_instruction(self._curr_func+"."+label)
        self._c_instruction(None, "0", "JMP")

    def move_var(self, reg, count):
        self._a_instruction("LCL")
        self._c_instruction("D", "M")
        self._a_instruction(str(count))
        self._c_instruction("D", "D-A")
        self._c_instruction("A", "D")
        self._c_instruction("D", "M")
        self._set_var(reg)

    def move_it(self, arg):
        self._set_var(arg)
        self._c_instruction("D", "D-1")
        
    def write_return(self):
        self._output_file.write("\n")
        self._remark("start return")
        #setting RETURN to be the current
        self._remark("setting RETURN to be the current")
        self._a_instruction("LCL")
        self._c_instruction("D", "M")
        self._set_var(REG)

        self._a_instruction(TEMP)
        self._c_instruction("A", "D-A")
        self._c_instruction("D", "M")
        self._a_instruction("R14")
        #POP
        self._remark("pop")
        self._c_instruction("M", "D")
        self._a_instruction("SP")
        self._c_instruction("AM", "M-1")

        self._c_instruction("D", "M")
        self._a_instruction("ARG")
        self._c_instruction("A", "M")
        self._c_instruction("M", "D")
        #setting SP to our arguments
        self._remark("setting SP to our arguments")
        self._a_instruction("ARG")
        self._c_instruction("D", "M+1")
        self._a_instruction("SP")
        self._c_instruction("M", "D")
        #moving the variables
        self._remark("moving the variables")
        self._frame("THAT")
        self._frame("THIS")
        self._frame("ARG")
        self._frame("LCL")
        #jumping to the RETURN
        self._remark("jumping to the RETURN")
        self._a_instruction("R14")
        self._c_instruction("A", "M")
      #  self._curr_func = ""
        self._c_instruction(None,"0", "JMP")
        self._remark("end return")
        self._output_file.write("\n")

    def _frame(self, arg):
        self._remark("moving var" + arg)
        self._a_instruction("R13")
        self._c_instruction("AM", "M-1")
        self._c_instruction("D", "M")
        self._a_instruction(arg)
        self._c_instruction("M", "D")

    def write_function(self, name, args_amount):
        self._remark("start function")
        self._curr_func = name
        self.write_label(name, False)
        for i in range(args_amount):
            self._push_dest("0")
        self._remark("end function")

    def save_reg(self, reg):
        self._a_instruction(reg)
        self._c_instruction("D", "M")
        self._push_dest("D")

    def write_call(self, label, argsCount):
        self._remark("start call")
        #putting the return address in stack
        self._remark("putting the return address in stack")
        self._label_count += 1
        ret_lbl = self._curr_func +".RETURN" + str(self._label_count)
        self._a_instruction(ret_lbl)
        self._c_instruction("D", "A")
        self._push_dest("D")
        #saving registers into stack
        self._remark("saving registers into stack")
        self.save_reg("LCL")
        self.save_reg("ARG")
        self.save_reg("THIS")
        self.save_reg("THAT")
        #setting the new positions of ARG
        self._remark("setting the new positions of ARG")
        self._a_instruction("SP")
        self._c_instruction("D", "M")
        self._a_instruction("LCL")
        self._c_instruction("M", "D")
        self._a_instruction(TEMP+argsCount)
        self._c_instruction("D", "D-A")
        self._a_instruction("ARG")
        self._c_instruction("M", "D")
        #returnning
        self._remark("returnning")
        self._a_instruction(label)
    #    self._a_instruction(label+"."+self._file_name)
        self._c_instruction(None, "0", "JMP")
        self.write_label(ret_lbl, False)
        self._remark("end call")
        self._output_file.write("\n")

    #closing the file we written
    def close(self):
        self._output_file.close()
