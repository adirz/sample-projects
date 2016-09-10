STATIC = "STATIC"
FIELD = "FIELD"
ARG = "ARG"
VAR = "VAR"



class SymbolTable(object):
    def __init__(self):
        self._class_symbol_table = {}
        self._subroutine_symbol_table = {}
        self._indexes = {ARG: 0, VAR : 0, STATIC: 0, FIELD: 0}

    def start_subroutine(self):
        self._subroutine_symbol_table = {}
        self._indexes[ARG] = 0
        self._indexes[VAR] = 0

    def define(self, name, identifier_type, kind):
        kind = kind.upper()
        if kind in (VAR, ARG):
            self._subroutine_symbol_table[name] = (identifier_type, kind, self._indexes[kind])
            self._indexes[kind] += 1
        else:
            self._class_symbol_table[name] = (identifier_type, kind, self._indexes[kind])
            self._indexes[kind] += 1

    def var_count(self, kind):
        count = 0
        table =  self._subroutine_symbol_table if kind in (VAR, ARG) else self._class_symbol_table
        for name in table:
            if self.kind_of(name) == kind:
                count += 1
        return count

    def type_of(self, name):
        if name in self._subroutine_symbol_table:
            return self._subroutine_symbol_table[name][0]
        if name in self._class_symbol_table:
            return self._class_symbol_table[name][0]

    def kind_of(self, name):
        if name in self._subroutine_symbol_table:
            return self._subroutine_symbol_table[name][1]
        if name in self._class_symbol_table:
            return self._class_symbol_table[name][1]

    def index_of(self, name):
        if name in self._subroutine_symbol_table:
            return self._subroutine_symbol_table[name][2]
        if name in self._class_symbol_table:
            return self._class_symbol_table[name][2]
