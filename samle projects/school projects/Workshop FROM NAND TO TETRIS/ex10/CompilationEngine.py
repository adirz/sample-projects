import sys
from xml.etree import ElementTree
import xml.etree.cElementTree as ET
from xml.dom import minidom
import JackTokenizer

TYPE_TO_STRING = {
    JackTokenizer.KEYWORD : "keyword",
    JackTokenizer.SYMBOL : "symbol",
    JackTokenizer.IDENTIFIER : "identifier",
    JackTokenizer.INT_CONST : "integerConstant",
    JackTokenizer.STRING_CONST : "stringConstant"
}

CLASS_VAR_DEC = "classVarDec"
SUBRUTINE_DEC = "subroutineDec"
SUBRUTINE_BODY = "subroutineBody"
PARAMETER_LIST = "parameterList"
VAR_DEC = "varDec"
STATEMENTS = "statements"
DO = "doStatement"
LET = "letStatement"
WHILE = "whileStatement"
RETURN = "returnStatement"
IF = "ifStatement"
EXPRESSION = "expression"
EXPRESSION_LIST = "expressionList"
TERM = "term"

CLASS_VAR_DEC_TOKENS = ["static", "field"]
SUBRUTINE_DEC_TOKENS = ["constructor", "function", "method"]
OP_TOKENS = ['+', '-', '*' , '/' , '&', '|', '<', '>', '=']
UNARY_OP_TOKENS = ['-', '~']
class BadStructureError(Exception):
    pass

"""
compiles jack file into xml file
uses the jack_tokenizer to pull tokens from the jack files.
"""
class CompilationEngine(object):

    def __init__(self, jack_tokenizer, output_xml_file_path):
        self._jack_tokenizer = jack_tokenizer
        self._output_xml_file_path = output_xml_file_path
        self._elements_stack = []


    #create XML data that looks like given examples
    def _element_to_pretty_xml(self, element):
        rough_string = ElementTree.tostring(element, 'utf-8')
        reparsed = minidom.parseString(rough_string)
        pretty_xml_str = reparsed.toprettyxml(indent="  ")
        #remove XML version tag added by minidom
        pretty_xml_str = pretty_xml_str[pretty_xml_str.find("\n")+1:]
        #carriage fix, to look like examples
        pretty_xml_str = pretty_xml_str.replace("\n", "\r\n")
        return pretty_xml_str

    #this function will compile the jack file. called from JackAnalyzer.py
    def compile(self):
        if not self._jack_tokenizer.has_more_tokens():
            #file without tokens. Just in case.
            return
        #the first token, class will be the XML root
        self._jack_tokenizer.advance()
        if self._jack_tokenizer.get_token() is not "class":
            raise BadStructureError()

        self.compile_class()
        open(self._output_xml_file_path, "w").write(self._element_to_pretty_xml(self._elements_stack[0]))

    #adds current token to the first element in the elements stack
    def _add_token(self):
        element_type = self._jack_tokenizer.token_type()
        ET.SubElement(self._elements_stack[0], TYPE_TO_STRING[element_type]).text = " %s " % self._jack_tokenizer.get_token()


    #push new element to stack, so next elements will be added to
    def _add_program_tree(self, name):
        self._elements_stack.insert(0, ET.SubElement(self._elements_stack[0], name))

    #pops single element from elements_stack. is it is empty, adds text so the XML will be similar
    #to given examples
    def _go_up_in_program_tree(self):
        popped_element = self._elements_stack[0]
        self._elements_stack = self._elements_stack[1:]
        #makes this: "<XX/>" look like this: "<XX>\n<\XX>"
        if len(popped_element.getchildren()) == 0:
            popped_element.text = "\n" + "  " * len(self._elements_stack)

    #write token in output file, and advances the tokenizer
    def _add_tokens_to_program(self, amount):
        for i in range(amount):
            self._add_token()
            if self._jack_tokenizer.has_more_tokens():
                self._jack_tokenizer.advance()

    def compile_class(self):
        self._elements_stack.insert(0, ET.Element(self._jack_tokenizer.get_token()))
        self._add_tokens_to_program(3) #class className {
        while self._jack_tokenizer.get_token() in (CLASS_VAR_DEC_TOKENS):
            self.compile_class_var_dec()
        while self._jack_tokenizer.get_token() in (SUBRUTINE_DEC_TOKENS):
            self.compile_subrutine_dec()
        self._add_tokens_to_program(1) #}


    def compile_class_var_dec(self):
        self._add_program_tree(CLASS_VAR_DEC)
        self._add_tokens_to_program(3) #(static | field) type varName
        while self._jack_tokenizer.get_token() is not ";":
            self._add_tokens_to_program(2) #, varName
        self._add_tokens_to_program(1) #;
        self._go_up_in_program_tree()

    def compile_subrutine_dec(self):
        self._add_program_tree(SUBRUTINE_DEC)
        #(constructor | function | method) (void | type) subroutineName (
        self._add_tokens_to_program(4)
        self.compile_parameter_list()
        self._add_tokens_to_program(1) #)
        self.compile_subrutine_body()
        self._go_up_in_program_tree()

    def compile_subrutine_body(self):
        self._add_program_tree(SUBRUTINE_BODY)
        self._add_tokens_to_program(1) #{
        while self._jack_tokenizer.get_token() is "var":
            self.compile_var_dec()
        self.compile_statements()
        self._add_tokens_to_program(1) #}
        self._go_up_in_program_tree()

    def compile_parameter_list(self):
        self._add_program_tree(PARAMETER_LIST)
        if self._jack_tokenizer.get_token() is not ")":
            self._add_tokens_to_program(2) # type varName
        while self._jack_tokenizer.get_token() is not ")":
            self._add_tokens_to_program(3) #, type varName
        self._go_up_in_program_tree()


    def compile_var_dec(self):
        self._add_program_tree(VAR_DEC)
        self._add_tokens_to_program(3) # ( static | field ) type varName
        if self._jack_tokenizer.get_token() is not ";":
            self._add_tokens_to_program(2) #, varName
        self._add_tokens_to_program(1) #;
        self._go_up_in_program_tree()

    def compile_statements(self):
        self._add_program_tree(STATEMENTS)
        while self._jack_tokenizer.get_token() is not "}":
            {"let": self.compile_let,
             "if": self.compile_if,
             "while": self.compile_while,
             "do": self.compile_do,
             "return": self.compile_return,
            }[self._jack_tokenizer.get_token()]()
        self._go_up_in_program_tree()

    def compile_do(self):
        self._add_program_tree(DO)
        self._add_tokens_to_program(1) #do
        self.compile_subroutine_call()
        self._add_tokens_to_program(1) #;
        self._go_up_in_program_tree()

    def compile_let(self):
        self._add_program_tree(LET)
        self._add_tokens_to_program(2) #let varName
        if self._jack_tokenizer.get_token() is "[":
            self._add_tokens_to_program(1) #[
            self.compile_expression()
            self._add_tokens_to_program(1) #]

        self._add_tokens_to_program(1) #=
        self.compile_expression()
        self._add_tokens_to_program(1) #;
        self._go_up_in_program_tree()

    def compile_while(self):
        self._add_program_tree(WHILE)
        self._add_tokens_to_program(2) #while(
        self.compile_expression()
        self._add_tokens_to_program(2) #){
        self.compile_statements()
        self._add_tokens_to_program(1) #}
        self._go_up_in_program_tree()

    def compile_return(self):
        self._add_program_tree(RETURN)
        self._add_tokens_to_program(1) #return
        if self._jack_tokenizer.get_token() is not ";":
            self.compile_expression()
        self._add_tokens_to_program(1) #;
        self._go_up_in_program_tree()

    def compile_if(self):
        self._add_program_tree(IF)
        self._add_tokens_to_program(2) #if (
        self.compile_expression()
        self._add_tokens_to_program(2) #){
        self.compile_statements()
        self._add_tokens_to_program(1) #}
        if self._jack_tokenizer.get_token() is "else":
            self._add_tokens_to_program(2) #else {
            self.compile_statements()
            self._add_tokens_to_program(1) #}
        self._go_up_in_program_tree()

    def compile_expression(self):
        self._add_program_tree(EXPRESSION)
        self.compile_term()
        while self._jack_tokenizer.get_token() in OP_TOKENS:
            self._add_tokens_to_program(1) #op
            self.compile_term()
        self._go_up_in_program_tree()

    def compile_term(self):
        self._add_program_tree(TERM)
        if self._jack_tokenizer.get_token() is '(':
            self._add_tokens_to_program(1) #(
            self.compile_expression()
            self._add_tokens_to_program(1) #)
        elif self._jack_tokenizer.get_token() in UNARY_OP_TOKENS:
            self._add_tokens_to_program(1) #unaryOp
            self.compile_term()
        else:
            self.compile_subroutine_call()
        self._go_up_in_program_tree()

    def compile_subroutine_call(self):
        self._add_tokens_to_program(1) #subroutineName | className | varName
        if self._jack_tokenizer.get_token() is '.':
            self._add_tokens_to_program(3) #. function (
            self.compile_expression_list()
            self._add_tokens_to_program(1) #)
        elif self._jack_tokenizer.get_token() is '(':
            self._add_tokens_to_program(1) #(
            self.compile_expression_list()
            self._add_tokens_to_program(1) #)
        elif self._jack_tokenizer.get_token() is '[':
            self._add_tokens_to_program(1) #[
            self.compile_expression()
            self._add_tokens_to_program(1) #]

    def compile_expression_list(self):
        self._add_program_tree(EXPRESSION_LIST)
        if self._jack_tokenizer.get_token() is not ')':
            self.compile_expression()
        while self._jack_tokenizer.get_token() is not ')':
            self._add_tokens_to_program(1) #,
            self.compile_expression()
        self._go_up_in_program_tree()



