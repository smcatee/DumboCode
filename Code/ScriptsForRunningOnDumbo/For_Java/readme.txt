# To run a Java MapReduce program, put all of these script files in the
# directory where you mapper and reducer files reside on Dumbo.

# Note that you may need to change the input file name and the Java file names -
#  - The input file name is assumed to be input.txt
#  - The Java file names are assumed to be MaxTemperatureMapper.java, MaxTemperatureReducer.java,
#    and MaxTemperature.java

# This script cleans out the HDFS directory, creates a fresh new HDFS direcrory, 
# fills the new HDFS directory with the data file, runs the code, and
# outputs the result (assuming 1 reducer was used).
./go.sh

# Your output will be in this directory in HDFS: hw/output


