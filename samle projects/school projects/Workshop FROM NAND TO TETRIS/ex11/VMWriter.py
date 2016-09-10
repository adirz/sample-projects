class VMWriter(object):
    def __init__(self, output_file):
        self._output_file = output_file

    def write_push(self, segment, index):
        self.write_command("push", segment, index)

    def write_pop(self, segment, index):
        self.write_command("pop", segment, index)

    def write_arithmetic(self, command):
        self.write_command(command)

    def write_label(self, label):
        self.write_command("label", label)

    def write_goto(self, label):
        self.write_command("goto", label)

    def write_if(self, label):
        self.write_command("if-goto", label)

    def write_call(self, name, args_number):
        self.write_command("call", name, args_number)

    def write_function(self, name, locals_name):
        self.write_command("function", name, locals_name)

    def write_return(self):
        self.write_command("return")

    def write_command(self, command, arg1 = None, arg2 = None):
        if arg2 != None:
            self._output_file.write("%s %s %s\n" % (command, arg1, arg2))
            return
        if arg1 != None:
            self._output_file.write("%s %s\n" % (command, arg1))
            return
        self._output_file.write("%s\n" % (command))


    #specific stack commands

    def push_const(self, val):
        self.write_push('constant', val)

    def push_arg(self, arg_num):
        self.write_push('argument', arg_num)

    def push_this_address(self):
        self.write_push('pointer', 0)

    def push_that(self):
        self.write_push('that', 0)

    def push_temp(self, temp_num):
        self.write_push('temp', temp_num)

    def pop_this_address(self):
        self.write_pop('pointer', 0)

    def pop_that_address(self):
        self.write_pop('pointer', 1)

    def pop_that(self):
        self.write_pop('that', 0)

    def pop_temp(self, temp_num):
        self.write_pop('temp', temp_num)