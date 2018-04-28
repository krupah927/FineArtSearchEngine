#!/bin/bash
if [ $# -eq 0 ]
  then
    echo "Usage: ./buildIndex.sh outputDirectory inputDirectory"
    exit 1
fi

java -jar  LuceneIndexer.jar $1 $2                   
