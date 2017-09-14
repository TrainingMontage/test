""" Build index from directory listing

gen_index.py </path/to/directory> [--header <header text>]
"""

# python2 and python3 compatible
from __future__ import print_function

import os
import argparse

# May need to do "pip install mako"
from mako.template import Template

INDEX_TEMPLATE = r"""
<html>
<body>
<h2>${header}</h2>
<p>
% for name in names:
    <li><a href="${name}">${name}</a></li>
% endfor
</p>
</body>
</html>
"""

EXCLUDED = ['index.html']


def main():
    """main"""
    parser = argparse.ArgumentParser()
    parser.add_argument("directory")
    parser.add_argument("--header")
    args = parser.parse_args()
    fnames = [fname for fname in sorted(os.listdir(args.directory))
              if fname not in EXCLUDED]
    header = (args.header if args.header else os.path.basename(args.directory))
    with open(os.path.join(args.directory, "index.html"), 'w') as fout:
        print(Template(INDEX_TEMPLATE).render(names=fnames, header=header), file=fout)

if __name__ == '__main__':
    main()
