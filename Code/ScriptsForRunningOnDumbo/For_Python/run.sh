# Run the program
# CDH (currently, as of Sep. 2018) is a link to: /opt/cloudera/parcels/CDH-5.15.0-1.cdh5.15.0.p0.21

hadoop jar /opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/hadoop-streaming.jar -D mapreduce.job.reduces=1 -files hdfs://dumbo/user/"$USER"/hw/code/Mapper.py,hdfs://dumbo/user/"$USER"/hw/code/Reducer.py -mapper "python Mapper.py" -reducer "python Reducer.py" -input /user/"$USER"/hw/input/input.txt -output /user/"$USER"/hw/output
