#!/usr/bin/env bash
# usage: build.sh <src_dir> <build_dir>
for f in $(find $1 -name '*.tex'); do
    OUT_DIR=$2/$(dirname "${f#./}")
    mkdir -p $OUT_DIR
    latexmk -quiet -output-directory=$OUT_DIR -pdf $f --shell-escape
    latexmk -quiet -c -output-directory=$OUT_DIR $f
done
python scripts/gen_filetree.py $2