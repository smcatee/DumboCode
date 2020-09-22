Java scripts
go - runs following scripts
clean - hdfs dfs -rm -r -f hw
setup - hdfs dfs -mkdir hw/input; hdfs dfs -put input.txt hw/input; hdfs dfs -ls hw/input
run - rm *.class; rm *.jar; javac -classpath `yarn classpath` -d . MaxTemperatureMapper.java;
    javac -classpath `yarn classpath` -d . MaxTemperatureReducer.java;
    javac -classpath `yarn classpath`:. -d . MaxTemperature.java; jar -cvf maxTemp.jar *.class;
    hadoop jar maxTemp.jar MaxTemperature /user/"$USER"/hw/input/input.txt /user/"$USER"/hw/output
dump - hdfs dfs -cat hw/output/part-r-00000




Py scripts - need to chmod 777 all py files
go - runs the next four scripts
clean - hdfs dfs -rm -r -f hw
setup - hdfs dfs -mkdir hw/code; hdfs dfs -mkdir hw/input; hdfs dfs -put Mapper.py hw/code; hdfs dfs -put Reducer.py hw/code;
    hdfs dfs -chmod 777 hw/code/*.py; hdfs dfs -put input.txt hw/input; hdfs dfs -ls hw/code; hdfs dfs -ls hw/input
run - hadoop jar /opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/hadoop-streaming.jar -D mapreduce.job.reduces=1 -files hdfs://dumbo/user/"$USER"/hw/code/Mapper.py,hdfs://dumbo/user/"$USER"/hw/code/Reducer.py -mapper "python Mapper.py" -reducer "python Reducer.py" -input /user/"$USER"/hw/input/input.txt -output /user/"$USER"/hw/output
dump - hdfs dfs -cat hw/output/part-00000

catTest - cat ./input.txt| python ./Mapper.py | sort | python ./Reducer.py




MINE!!!!

useful variables
$USER
$HOME

useful info
in streaming jobs parameters are converted from . to _ spaced
    e.g. mapreduce.job.jar is mapreduce_job_jar
logs for stdout, stderr, syslog of tasks logged to ${HADOOP_LOG_DIR}/userlogs