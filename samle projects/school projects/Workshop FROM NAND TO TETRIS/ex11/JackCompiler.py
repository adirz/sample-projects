import sys, os
import JackTokenizer
import CompilationEngine

def compile_jack(jack_file_path):
    jack_tokenizer = JackTokenizer.JackTokenizer(jack_file_path)
    with open(jack_file_path.replace(".jack", ".vm"), "w") as vm_out_file:
        compilation_engine = CompilationEngine.CompilationEngine(jack_tokenizer, vm_out_file)
        compilation_engine.compile()

def main():
    if not len(sys.argv) == 2:
        print("Usage: JackCompiler.py <input.jack|dir_path>")
        return
    in_path = sys.argv[1]

    #single file
    if not os.path.isdir(in_path):
        compile_jack(in_path)
        return

    #directory
    for file_name in os.listdir(in_path):
        if file_name.endswith(".jack"):
            compile_jack(os.path.join(in_path, file_name))

if "__main__" == __name__:
    main()
