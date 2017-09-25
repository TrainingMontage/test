#!/usr/bin/env bash
for f in $(find $1 -name '*.tex'); do
    echo $f
    OUT_DIR=$2/$(dirname "${f#./}")
    echo "mkdir -p $OUT_DIR"
    echo "latexmk -output-directory=$OUT_DIR -pdf $f"
    echo "latexmk -c -output-directory=$OUT_DIR $f"
done