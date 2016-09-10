KEYWORDS = ['class' , 'constructor' , 'function' , 'method' , 'field' , 'static' ,
            'var' , 'int' , 'char' , 'boolean' , 'void' , 'true' , 'false' , 'null' , 'this' ,
            'let' , 'do' , 'if' , 'else' , 'while' , 'return']
SYMBOLS = ['{' , '}' , '(' , ')' , '[' , ']' , '.' , ',' , ';' , '+' , '-',
           '*' , '/' , '&' , ',' , '<' , '>' , '=' , '~' ]

#token types
KEYWORD = "KEYWORD"
SYMBOL = "SYMBOL"
IDENTIFIER = "IDENTIFIER"
INT_CONST = "INT_CONST"
STRING_CONST = "STRING_CONST"

#keywords
CLASS = "CLASS"
METHOD = "METHOD"
FUNCTION = "FUNCTION"
CONSTRUCTOR = "CONSTRUCTOR"
INT = "INT"
BOOLEAN = "BOOLEAN"
CHAR = "CHAR"
VOID = "VOID"
VAR = "VAR"
STATIC = "STATIC"
FIELD = "FIELD"
LET = "LET"
DO = "DO"
IF = "IF"
ELSE = "ELSE"
WHILE = "WHILE"
RETURN = "RETURN"
TRUE = "TRUE"
FALSE = "FALSE"
NULL = "NULL"
THIS = "THIS"

class BadFormatError(Exception):
    def __init__(self, position):
        self.position = position

class JackTokenizer(object):
    def __init__(self, jack_file_path):
        self._jack_data = open(jack_file_path, "r").read()
        self._current_index = 0

    def _get_next_token_start(self):
        #import pdb;pdb.set_trace()
        temp_index = self._current_index
        while temp_index < len(self._jack_data):
            #whitespaces
            if self._jack_data[temp_index].isspace():
                temp_index += 1
            #block comment
            elif self._jack_data[temp_index:].startswith(r"/**"):
                temp_index += self._jack_data[temp_index:].find(r"*/") + 2
            #comment
            elif self._jack_data[temp_index:].startswith(r"//"):
                temp_index += self._jack_data[temp_index:].find("\n") + 1
            else:
                break

        return temp_index

    def _get_next_token(self):
        temp_index = self._get_next_token_start()
        if temp_index >= len(self._jack_data):
            #end of file
            return
        rest_of_jack = self._jack_data[temp_index:]
        #fixed tokens
        for token in KEYWORDS + SYMBOLS:
            if not rest_of_jack.startswith(token):
                continue
            temp_index += len(token)
            if token in KEYWORDS:
                return token, KEYWORD, temp_index
            return token, SYMBOL, temp_index
        #strings
        if rest_of_jack[0] == '"':
            end_of_string = rest_of_jack[1:].find('"')
            temp_index += end_of_string + 2
            return rest_of_jack[1:end_of_string + 1], STRING_CONST, temp_index

        #identifier or int
        start_of_token = temp_index
        while temp_index < len(self._jack_data) and \
              (self._jack_data[temp_index].isalnum() or \
               self._jack_data[temp_index] == "_"):
            temp_index += 1
        token = rest_of_jack[:temp_index - start_of_token]
        try:
            if 0 <= int(token) < 32767:
                return token, INT_CONST, temp_index
            #int not in range
            raise BadFormatError(start_of_token)
        except ValueError:
            #identifier (couldn't convert to int)
            if token[0].isdigit():
                #identifier doesn't start with number
                raise BadFormatError(start_of_token)
            return token, IDENTIFIER, temp_index

    def has_more_tokens(self):
        return self._get_next_token() is not None

    def advance(self):
        self._current_token, self._current_type, self._current_index = self._get_next_token()

    def token_type(self):
        return self._current_type

    def get_token(self):
        return self._current_token
