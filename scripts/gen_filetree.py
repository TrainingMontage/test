#!/usr/bin/python

"""
used to build a directory tree into usable static web pages

args:
    source directory
    output directory

example usage:
    python3 scripts/build.py work_packages build/docs
"""

# python2 and python3 compatible
from __future__ import print_function

import os

import click
from mako.template import Template


INDEX_TEMPLATE = r"""
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
</head>
<body>
    <h2>${header}</h2>
    <div class="list-group">
    % for name in names:
        <a href="${name}" class="list-group-item">
            ${name}
        </a>
    % endfor
    </div>

    <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
</body>
</html>
"""

EXCLUDED = ['index.html']


def gen_index(directory, header=None):
    """generate an index.html file listing other files in that directory"""
    fnames = [fname for fname in sorted(os.listdir(directory))
              if fname not in EXCLUDED]
    header = header or os.path.basename(directory)
    with open(os.path.join(directory, "index.html"), 'w') as fout:
        print(Template(INDEX_TEMPLATE).render(names=fnames, header=header), file=fout)


# @click.option('--count', default=2, help='number of tex compilations per file')
@click.command()
@click.argument('directory')
def build(directory):
    # make an index.html file for all directories processed
    for subdir, dirs, files in os.walk(directory):
        gen_index(subdir)


if __name__ == '__main__':
    build()
