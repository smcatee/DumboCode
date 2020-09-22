# To run a Python MapReduce program, put all of these script files in the
# directory where you Mapper.py and Reducer.py files reside on Dumbo.

# Note that you may need to change the input file name and the Python file names -
#  - The input file name is assumed to be input.txt
#  - The Python file names are assumed to be Mapper.py and Reducer.py

# To make all .py files executable, type:
chmod 755 *.py

# This script cleans out the HDFS directory, creates a fresh new HDFS direcrory, 
# fills the new HDFS directory with data and code files, runs the code, and
# outputs the result (assuming 1 reducer was used).
./go.sh


# Your output will be in this directory in HDFS: hw/output

# Use catTest.sh to test your Python program without using MapReduce


