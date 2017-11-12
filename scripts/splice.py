import sys
import re

# THIS IS WHAT YOU WANT TO EDIT!
# Just add or uncomment the name of your file in this list - WITHOUT THE '.tex'!
# This will add your file to the 'complete.tex' when it is spliced together.
# Be sure to also add a comment in the 'system.tex' file with the exact same string.
# Your work will not be in the complete document unless you do both.
INDIV = [
    # 'ctc_model',
    # 'wayside',
    'track_model',
    # 'train_controller'
]
SYSTEM = 'system'

def concat(dir, file):
    return '{dir}/{f}.tex'.format(dir=dir, f=file)

def read_in(file):
    return open(file).read()

def inside(text):
    later = text.split(r'\begin{document}')[1]
    middle = later.split(r'\end{document}')[0]
    return middle

def main(args):
    base_dir = args[1]
    out_name = args[2]
    sys_file = open(concat(base_dir, SYSTEM))
    indiv_files = {}
    for i in INDIV:
        bare = read_in(concat(base_dir, i))
        indiv_files[i] = inside(bare)

    with open(concat(base_dir, out_name), 'w') as out_file:
        for line in sys_file:
            out_file.write(line)
            for guy in INDIV:
                if re.match('\s*%\s*{}\s*'.format(guy), line):
                    out_file.write(indiv_files[guy])
    sys_file.close()


if __name__ == '__main__':
    main(sys.argv)