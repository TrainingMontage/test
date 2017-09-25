#!/usr/bin/python3

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

import errno
import os
from subprocess import call

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

    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
</body>
</html>
"""

EXCLUDED = ['index.html']


def mkdir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:  # Python >2.5
        if exc.errno == errno.EEXIST and os.path.isdir(path):
            pass
        else:
            raise


def gen_index(directory, header=None):
    """generate an index.html file listing other files in that directory"""
    fnames = [fname for fname in sorted(os.listdir(directory))
              if fname not in EXCLUDED]
    header = header or os.path.basename(directory)
    with open(os.path.join(directory, "index.html"), 'w') as fout:
        print(Template(INDEX_TEMPLATE).render(names=fnames, header=header), file=fout)


# @click.option('--count', default=2, help='number of tex compilations per file')
@click.command()
@click.argument('src_dir')
@click.argument('build_dir')
def build(src_dir, build_dir):
    # discover and build latex files in subdirectories
    for subdir, dirs, files in os.walk(src_dir):
        for file in files:
            filepath = os.path.join(subdir, file)
            if filepath.endswith(".tex"):
                out_dir = os.path.join(build_dir, subdir)

                # make directory tree, compile latex files, cleanup
                mkdir_p(out_dir)
                call(["latexmk", "-output-directory={}".format(out_dir), "-pdf", filepath])
                call(["latexmk", "-c", "-output-directory={}".format(out_dir), filepath])

    # make an index.html file for all directoriesprocessed
    for subdir, dirs, files in os.walk(build_dir):
        gen_index(subdir)


if __name__ == '__main__':
    build()
