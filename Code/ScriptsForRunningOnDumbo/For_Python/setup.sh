# Setup the HDFS directory stucture and populate it. 

# Create new directory structure in HDFS
hdfs dfs -mkdir hw
hdfs dfs -mkdir hw/code
hdfs dfs -mkdir hw/input

# Populate the code directory in HDFS and set the file permissions
hdfs dfs -put Mapper.py hw/code
hdfs dfs -put Reducer.py hw/code
hdfs dfs -chmod 777 hw/code/*.py

# Populate the input directory in HDFS with the input file
hdfs dfs -put input.txt hw/input

# Verify what is in the code directory
hdfs dfs -ls hw/code

# Verify what is in the input data directory
hdfs dfs -ls hw/input

