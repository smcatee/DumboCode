# Build the jar file and run

# Remove class and jar files
rm *.class
rm *.jar

# Compile
javac -classpath `yarn classpath` -d . MaxTemperatureMapper.java
javac -classpath `yarn classpath` -d . MaxTemperatureReducer.java
javac -classpath `yarn classpath`:. -d . MaxTemperature.java

# Create jar file
jar -cvf maxTemp.jar *.class

# Run the program
hadoop jar maxTemp.jar MaxTemperature /user/"$USER"/hw/input/input.txt /user/"$USER"/hw/output

