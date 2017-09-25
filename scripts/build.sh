#!/usr/bin/env bash
# usage: build.sh <src_dir> <build_dir>
for f in $(find $1 -name '*.tex'); do
    OUT_DIR=$2/$(dirname "${f#./}")
    tree
    echo "mkdir -p $OUT_DIR"
    mkdir -p $OUT_DIR
    echo "latexmk -output-directory=$OUT_DIR -pdf $f"
    latexmk -output-directory=$OUT_DIR -pdf $f
    echo "latexmk -c -output-directory=$OUT_DIR $f"
    latexmk -c -output-directory=$OUT_DIR $f
done
echo "gen_filetree"
./scripts/gen_filetree.py $2