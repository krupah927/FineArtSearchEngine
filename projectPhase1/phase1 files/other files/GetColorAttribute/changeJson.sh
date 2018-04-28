#!/bin/bash
if [ $# -eq 0 ]
  then
    echo "Usage: ./changeJson.sh inputJsonfile inputImageTextfile"
    exit 1
fi

java -jar  changeJson.jar $1 $2 
