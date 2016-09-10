import sys
import JackTokenizer
import SymbolTable
import VMWriter


ARRAY_TEMP_INDEX = 0
RETURN_TEMP_INDEX = 0

CLASS_VAR_DEC_TOKENS = ['static', 'field']
SUBROUTINE_DEC_TOKENS = ['constructor', 'function', 'method']
OP_TOKENS = ['+', '-', '*' , '/' , '&', '|', '<', '>', '=']
UNARY_OP_TOKENS = ['-', '~']
SEGMENTS = {'STATIC' : 'static',
            'FIELD' : 'this',
            'ARG' : 'argument',
            'VAR' : 'local'}

JACK_TO_VM_OPS={'+':'add',
                '-':'sub',
                '*':'call Math.multiply 2',
                '/':'call Math.divide 2',
                '<':'lt',
                '>':'gt',
                '=':'eq',
                '&':'and',
                '|':'or'}
JACK_TO_VM_UNARY_OPS = {
                '-':'neg',
                '~':'not'}

'''
compiles jack file into xml file
uses the jack_tokenizer to pull tokens from the jack files.
'''
class CompilationEngine(object):

    def __init__(self, jack_tokenizer, output_vm_file):
        self._jack_tokenizer = jack_tokenizer
        self._vm_writer = VMWriter.VMWriter(output_vm_file)
        self._symbols = SymbolTable.SymbolTable()
        self._ifs_count = 0
        self._whiles_count = 0


    #create XML data that looks like given examples
    def _element_to_pretty_xml(self, element):
        rough_string = ElementTree.tostring(element, 'utf-8')
        reparsed = minidom.parseString(rough_string)
        pretty_xml_str = reparsed.toprettyxml(indent='  ')
        #remove XML version tag added by minidom
        pretty_xml_str = pretty_xml_str[pretty_xml_str.find('\n')+1:]
        #carriage fix, to look like examples
        pretty_xml_str = pretty_xml_str.replace('\n', '\r\n')
        return pretty_xml_str

    #this function will compile the jack file. called from JackAnalyzer.py
    def compile(self):
        self._jack_tokenizer.advance() #to first token
        self.compile_class()

    #write token in output file, and advances the tokenizer
    def _consume_token(self, expected = None):
        token = self._jack_tokenizer.get_token()
        assert expected == None or expected == token
        self._jack_tokenizer.advance()
        return token

    def compile_class(self):
        self._consume_token("class")
        self._class_name = self._consume_token() # className
        self._consume_token("{")
        while self._jack_tokenizer.get_token() in (CLASS_VAR_DEC_TOKENS):
            self.compile_var_dec()
        while self._jack_tokenizer.get_token() in (SUBROUTINE_DEC_TOKENS):
            self.compile_subroutine_dec()
        #self._consume_token("}")



    def compile_var_dec(self):
        var_kind = self._consume_token() # (static | field | var)
        var_type = self._consume_token() # type
        var_name = self._consume_token() # varName

        self._symbols.define(var_name, var_type, var_kind)

        while self._jack_tokenizer.get_token() is not ';':
            self._consume_token(",")
            var_name = self._consume_token() # varName
            self._symbols.define(var_name, var_type, var_kind)
        self._consume_token(";")

    def compile_subroutine_dec(self):
        subroutine_kind = self._consume_token() #(constructor | function | method)
        subroutine_type = self._consume_token() #(void | type)
        subroutine_name = self._consume_token() # subroutineName

        self._subroutine_name = subroutine_name
        self._symbols.start_subroutine()
        #handle this

        if subroutine_kind  == 'method':
            self._symbols.define('this', self._class_name, SymbolTable.ARG)

        self._consume_token("(")
        self.compile_parameter_list()
        self._consume_token(")")
        self.compile_subroutine_body(subroutine_kind)


    def compile_parameter_list(self):
        if self._jack_tokenizer.get_token() is not ')':
            self.compile_arg()
        while self._jack_tokenizer.get_token() is not ')':
            self._consume_token(",")
            self.compile_arg()

    def compile_arg(self):
        var_type = self._consume_token() # type
        var_name = self._consume_token() # varName
        self._symbols.define(var_name, var_type, SymbolTable.ARG)

    def compile_subroutine_body(self, subroutine_kind):
        self._consume_token("{")
        self._ifs_count = 0
        self._whiles_count = 0
        while self._jack_tokenizer.get_token() is 'var':
            self.compile_var_dec()
        function_name = "%s.%s" % (self._class_name, self._subroutine_name)
        self._vm_writer.write_function(function_name, self._symbols.var_count("VAR"))
        if subroutine_kind == "method":
            self._vm_writer.push_arg(0)
            self._vm_writer.pop_this_address()
        elif subroutine_kind == "constructor":
            fields_amount = self._symbols.var_count("FIELD")
            self._vm_writer.push_const(fields_amount)
            self._vm_writer.write_call('Memory.alloc', 1)
            self._vm_writer.pop_this_address()
        self.compile_statements()
        self._consume_token("}")

    def compile_statements(self):
        while self._jack_tokenizer.get_token() is not '}':
            {'let': self.compile_let,
             'if': self.compile_if,
             'while': self.compile_while,
             'do': self.compile_do,
             'return': self.compile_return,
            }[self._jack_tokenizer.get_token()]()

    def compile_do(self):
        self._consume_token("do")
        self.compile_subroutine_call(self._consume_token())
        self._vm_writer.pop_temp(RETURN_TEMP_INDEX)
        self._consume_token(";")

    def compile_let(self):
        self._consume_token("let")
        var_name = self._consume_token() # varName
        is_array = False
        if self._jack_tokenizer.get_token() is '[':
            is_array = True
            self._consume_token("[")
            self.compile_expression()
            self._consume_token("]")
            self._push_var(var_name)
            self._vm_writer.write_command('add') #in stack: base + offset
        self._consume_token("=")
        self.compile_expression()
        self._consume_token(";")

        #Assignment: pop expression to new var
        if is_array:
            # pop expression value to assign into temp
            self._vm_writer.pop_temp(ARRAY_TEMP_INDEX)
            # pop base + offset as 'that' address
            self._vm_writer.pop_that_address()
            # push expression value to assign
            self._vm_writer.push_temp(ARRAY_TEMP_INDEX)
            # *(base + offset) = expression value
            self._vm_writer.pop_that()
            return
        self._pop_var(var_name)


    def _push_var(self, var_name):
        var_kind = self._symbols.kind_of(var_name)
        var_index = self._symbols.index_of(var_name)
        self._vm_writer.write_push(SEGMENTS[var_kind], var_index)

    def _pop_var(self, var_name):
        var_kind = self._symbols.kind_of(var_name)
        var_index = self._symbols.index_of(var_name)
        self._vm_writer.write_pop(SEGMENTS[var_kind], var_index)


    def _compile_condition_statement(self, true_label, false_label):

        self._consume_token("(")

        self.compile_expression()
        self._vm_writer.write_if(true_label)
        self._vm_writer.write_goto(false_label)
        self._vm_writer.write_label(true_label)

        #gets here if condition was true
        self._consume_token(")")
        self._consume_token("{")
        self.compile_statements()
        self._consume_token("}")


        #gets here if condition was false

    def compile_while(self):
        while_id =  self._whiles_count
        self._whiles_count += 1
        self._consume_token("while")
        self._vm_writer.write_label("WHILE_EXP%s" % while_id)

        self._consume_token("(")

        self.compile_expression()
        self._vm_writer.write_command("not")
        self._vm_writer.write_if("WHILE_END%s" % while_id)

        self._consume_token(")")
        self._consume_token("{")
        self.compile_statements()
        self._consume_token("}")
        self._vm_writer.write_goto("WHILE_EXP%s" % while_id)
        self._vm_writer.write_label("WHILE_END%s" % while_id)

    def compile_return(self):
        self._consume_token("return")
        if self._jack_tokenizer.get_token() is not ';':
            self.compile_expression()
        else:
            self._vm_writer.push_const(0)
        self._vm_writer.write_return()
        self._consume_token(";")

    def compile_if(self):
        if_id = self._ifs_count
        self._ifs_count += 1
        self._consume_token("if")
        self._compile_condition_statement("IF_TRUE%s" % if_id, "IF_FALSE%s" % if_id)
        if self._jack_tokenizer.get_token() is 'else':
            self._consume_token("else")

            self._vm_writer.write_goto("IF_END%s" % if_id)
            self._vm_writer.write_label("IF_FALSE%s" % if_id)
            self._consume_token("{")
            self.compile_statements()
            self._consume_token("}")
            self._vm_writer.write_label("IF_END%s" % if_id)
        else:
            self._vm_writer.write_label("IF_FALSE%s" % if_id)

    def compile_expression(self):
        self.compile_term()
        while self._jack_tokenizer.get_token() in OP_TOKENS:
            op = self._consume_token() # op
            self.compile_term()
            self._vm_writer.write_command(JACK_TO_VM_OPS[op])

    def compile_term(self):
        if self._jack_tokenizer.get_token() is '(':
            self._consume_token("(")
            self.compile_expression()
            self._consume_token(")")
            return
        if self._jack_tokenizer.get_token() in UNARY_OP_TOKENS:
            unary_op = self._consume_token() # unaryOp
            self.compile_term()
            self._vm_writer.write_command(JACK_TO_VM_UNARY_OPS[unary_op])
            return
        if self._jack_tokenizer.token_type() is JackTokenizer.INT_CONST:
            int_const = self._consume_token()
            self._vm_writer.push_const(int_const)
            return
        if self._jack_tokenizer.token_type() is JackTokenizer.STRING_CONST:
            self._vm_writer.push_const(len(self._jack_tokenizer.get_token()))
            self._vm_writer.write_call('String.new', 1)
            const_string = self._consume_token()
            for character in const_string:
                self._vm_writer.push_const(ord(character))
                self._vm_writer.write_call('String.appendChar', 2)
            return
        if self._jack_tokenizer.get_token() == "this":
            self._vm_writer.push_this_address()
            self._consume_token()
            return


        if self._jack_tokenizer.get_token() == "true":
            self._vm_writer.push_const(0)
            self._vm_writer.write_command('not')
            self._consume_token()
            return
        if self._jack_tokenizer.get_token() in ("false", "null"):
            self._vm_writer.push_const(0)
            self._consume_token()
            return
        #token is an identifier
        identifier = self._consume_token()
        if self._jack_tokenizer.get_token() == '[':
            #array access
            self._consume_token("[")
            self.compile_expression()
            self._consume_token("]")
            self._push_var(identifier)
            self._vm_writer.write_arithmetic('add')
            self._vm_writer.pop_that_address()
            self._vm_writer.push_that()
            return
        if self._jack_tokenizer.get_token() in ('(', '.'):
            #subroutine call
            self.compile_subroutine_call(identifier)
            return
        #identifier is a variable
        self._push_var(identifier)


    def compile_const(self):
        const = self._consume_token()
        if tok == T_NUM:
            self.vm.push_const(val)                 # push constant val
        elif tok == T_STR:
            self.write_string_const_init(val)       # initialize string & push str addr
        elif tok == T_KEYWORD:
            self.compile_kwd_const(val)             # push TRUE, FALSE, NULL etc.

    def _gen_subroutine_full_name(self, subroutine_name):
        args_amount = 0
        object_name = subroutine_name
        if self._jack_tokenizer.get_token() == '.':
            subroutine_type = self._symbols.type_of(subroutine_name)
            if subroutine_type is not None:
                args_amount = 1
                self._push_var(object_name)
                object_name = subroutine_type
            self._consume_token(".")
            subroutine_name = self._consume_token() #subroutineName
        else:
            args_amount = 1
            self._vm_writer.push_this_address()
            object_name = self._class_name

        full_subroutine_name = "%s.%s" % (object_name, subroutine_name)
        return full_subroutine_name, args_amount

    def compile_subroutine_call(self, subroutine_name):
        full_subroutine_name, args_amount = self._gen_subroutine_full_name(subroutine_name)
        #Arguments
        self._consume_token("(")
        args_amount += self.compile_expression_list()
        self._consume_token(")")
        self._vm_writer.write_call(full_subroutine_name, args_amount)


    def compile_expression_list(self):
        args_amount = 0
        if self._jack_tokenizer.get_token() is not ')':
            self.compile_expression()
            args_amount += 1

        while self._jack_tokenizer.get_token() is not ')':
            self._consume_token(",")
            self.compile_expression()
            args_amount += 1
        return args_amount



