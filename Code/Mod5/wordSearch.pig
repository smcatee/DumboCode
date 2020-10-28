textLines = LOAD '/user/hadoop/HDFS_File.txt' AS (line:chararray);
words = FOREACH textLines GENERATE FLATTEN(TOKENIZE(line)) as word;
uniqueWords = GROUP words BY word;
nWords = FOREACH uniqueWords GENERATE group, COUNT(words);
DUMP nWords;
