# Test the Python code without using the MapReduce framework
cat ./input.txt| python ./Mapper.py | sort | python ./Reducer.py

