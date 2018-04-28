#!/bin/bash
if [ $# -eq 0 ]
  then
    echo "Usage: ./start.sh seedfile <int>numberOfPages outputDir"
    exit 1
fi

java -jar  Driver.jar $1 $2 $3
