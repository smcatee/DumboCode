#!/bin/bash

## LOCALLY

# Download * files from ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/
wget -P ~/tmp/ ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed20n1*

# For each file downloaded check md5
md5sum --check ~/tmp/pubmed*.xml.gz.md5

# Unzip .gz files
gzip -d ~/tmp/pubmed*.gz

# Remove md5 files
rm -f ~/tmp/pubmed*.xml.gz.md5

# Move files to HDFS
#hdfs dfs -put *.xml /user/sm9553/project